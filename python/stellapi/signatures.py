############################ BEGIN LICENSE BLOCK #############################
#                                                                            #
# Version: MPL 1.1/GPL 2.0/LGPL 2.1                                          #
#                                                                            #
# The contents of this file are subject to the Mozilla Public License        #
# Version 1.1 (the "License"); you may not use this file except in           #
# compliance with the License. You may obtain a copy of the License at       #
# http://www.mozilla.org/MPL/                                                #
#                                                                            #
# Software distributed under the License is distributed on an "AS IS" basis, #
# WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License   #
# for the specific language governing rights and limitations under the       #
# License.                                                                   #
#                                                                            #
# The Original Code is the STELLA Programming Language.                      #
#                                                                            #
# The Initial Developer of the Original Code is                              #
# UNIVERSITY OF SOUTHERN CALIFORNIA, INFORMATION SCIENCES INSTITUTE          #
# 4676 Admiralty Way, Marina Del Rey, California 90292, U.S.A.               #
#                                                                            #
# Portions created by the Initial Developer are Copyright (C) 1996-2020      #
# the Initial Developer. All Rights Reserved.                                #
#                                                                            #
# Contributor(s):                                                            #
#                                                                            #
# Alternatively, the contents of this file may be used under the terms of    #
# either the GNU General Public License Version 2 or later (the "GPL"), or   #
# the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),   #
# in which case the provisions of the GPL or the LGPL are applicable instead #
# of those above. If you wish to allow use of your version of this file only #
# under the terms of either the GPL or the LGPL, and not to allow others to  #
# use your version of this file under the terms of the MPL, indicate your    #
# decision by deleting the provisions above and replace them with the notice #
# and other provisions required by the GPL or the LGPL. If you do not delete #
# the provisions above, a recipient may use your version of this file under  #
# the terms of any one of the MPL, the GPL or the LGPL.                      #
#                                                                            #
############################# END LICENSE BLOCK ##############################

"""
Support for accessing and translating STELLA function object signatures.
"""

from __future__ import print_function, division, absolute_import

import sys
PY2 = sys.version_info[0] == 2
PY3 = sys.version_info[0] >= 3
if PY3:
    unicode = str
    long = int

import keyword
if PY3:
    from io import StringIO
else:
    from StringIO import StringIO

from stellapi.common import FFI as _ffi
from stellapi.utils import to_bytes, ffi_to_string, ApiException
from stellapi.namespaces import boot, stella
from stellapi.bootstrap import lookupLibraryFunction
from stellapi.gcollect import gcProtectObject


### Basic call wrapper generation support

def stellaSigFunctionName(sig):
    return sig[0]
def stellaSigNativeReturnType(sig):
    return sig[1]
def stellaSigStellaReturnType(sig):
    return sig[2]
def stellaSigArity(sig):
    return len(sig) // 3 - 1
def stellaSigParaNativeName(sig, pi):
    return sig[(pi + 1) * 3]
def stellaSigParaNativeType(sig, pi):
    return sig[(pi + 1) * 3 + 1]
def stellaSigParaStellaType(sig, pi):
    return sig[(pi + 1) * 3 + 2]
def stellaSigIsReturnPara(sig, pi):
    pname = stellaSigParaNativeName(sig, pi)
    ptype = stellaSigParaNativeType(sig, pi)
    return pname.startswith('_Return') and ptype.endswith('&')

def stellaSigNativeCppReturnType(sig):
    # Just like `stellaSigNativeReturnType', but map `int' types that correspond to booleans to `bool' instead.
    # This allows us to selectively handle cases where booleans need to be explicitly cast to int in wrapper funcall contexts.
    # The C++ compiler does the casting automatically, but for that to happen it has to know the type.  In our C-callable
    # wrappers generated by STELLA, that is automatically the case, but for funcalls we have to generate the proper
    # function/method code pointer signature as well as the corresponding wrapper names.
    rtype = stellaSigNativeReturnType(sig)
    if rtype == 'int' and stellaSigStellaReturnType(sig) == '/STELLA/@BOOLEAN':
        rtype = 'bool'
    return rtype

def stellaSigParaNativeCppType(sig, pi):
    # Just like `stellaSigParaNativeType', but map `int' types that correspond to booleans to `bool' instead.
    ptype = stellaSigParaNativeType(sig, pi)
    if ptype.startswith('int') and stellaSigParaStellaType(sig, pi) == '/STELLA/@BOOLEAN':
        ptype = ptype.replace('int', 'bool')
    return ptype

def stellaSigParameterNativeNames(sig):
    names = []
    for i in range(0, stellaSigArity(sig)):
        names.append(stellaSigParaNativeName(sig, i))
    return names

def stellaSigInputParameterNativeNames(sig):
    names = []
    for i in range(0, stellaSigArity(sig)):
        if stellaSigIsReturnPara(sig, i):
            break
        names.append(stellaSigParaNativeName(sig, i))
    return names


def pointerizeReturnParameterType(ptype):
    # CFFI can't handle reference types, map them onto pointers instead.
    if ptype.endswith('&'):
        return ptype[0:-1] + '*'
    else:
        return ptype

def stellaNativeNameToPython(name):
    if keyword.iskeyword(name):
        return name + '_'
    else:
        return name

def parseCppQualifiedName(cppQualName):
    "Parse 'cppQualName' into its namespace and class portion."
    namespace = None
    name = cppQualName
    cpos = cppQualName.find('::')
    if cpos > 0:
        namespace = cppQualName[0:cpos]
        name = cppQualName[cpos+2:]
    if name.endswith('*'):
        name = name[0:-1]
    return namespace, name


def getStellaFunctionCSignature(qualName):
    sig = boot.lookupFunctionCSignatureByName(to_bytes(qualName))
    if sig == _ffi.NULL:
        raise ApiException('Cannot access C-signature for STELLA function: ' + str(qualName))
    return ffi_to_string(sig).split('\t')

def getStellaMethodCSignature(qualName):
    sig = boot.lookupMethodCSignatureByName(to_bytes(qualName))
    if sig == _ffi.NULL:
        raise ApiException('Cannot access C-signature for STELLA method: ' + str(qualName))
    return ffi_to_string(sig).split('\t')

def getStellaGlobalVariableCSignature(qualName):
    sig = boot.lookupGlobalVariableCSignatureByName(to_bytes(qualName))
    if sig == _ffi.NULL:
        raise ApiException('Cannot access C-signature for STELLA global variable: ' + str(qualName))
    return ffi_to_string(sig).split('\t')

def getStellaFunctionCppSignature(qualName):
    sig = boot.lookupFunctionCppSignatureByName(to_bytes(qualName))
    if sig == _ffi.NULL:
        raise ApiException('Cannot access C++-signature for STELLA function: ' + str(qualName))
    return ffi_to_string(sig).split('\t')

def getStellaMethodCppSignature(qualName):
    sig = boot.lookupMethodCppSignatureByName(to_bytes(qualName))
    if sig == _ffi.NULL:
        raise ApiException('Cannot access C++-signature for STELLA method: ' + str(qualName))
    return ffi_to_string(sig).split('\t')

def getStellaStorageSlotCppSignature(qualName):
    sig = boot.lookupStorageSlotCppSignatureByName(to_bytes(qualName))
    if sig == _ffi.NULL:
        raise ApiException('Cannot access C++-signature for STELLA method: ' + str(qualName))
    return ffi_to_string(sig).split('\t')

def getStellaGlobalVariableCppSignature(qualName):
    sig = boot.lookupGlobalVariableCppSignatureByName(to_bytes(qualName))
    if sig == _ffi.NULL:
        raise ApiException('Cannot access C++-signature for STELLA global variable: ' + str(qualName))
    return ffi_to_string(sig).split('\t')

def getStellaFunctionCode(qualName, error=True):
    # Return the raw code pointer to the C-callable wrapper of the STELLA function `qualName'.
    # If no such wrapper exists, try to access the unwrapped function code of the function directly.
    # TO DO: this doesn't yet work for methods such as stella/primary-class.
    csig  = getStellaFunctionCSignature(qualName)
    fname = stellaSigFunctionName(csig)
    code  = lookupLibraryFunction(fname)
    if code is None or code == _ffi.NULL:
        code = boot.lookupFunctionCodeByName(to_bytes(qualName))
    if code is None or code == _ffi.NULL:
        if error:
            raise ApiException('Cannot access compiled C-code for STELLA function: ' + str(qualName))
        else:
            code = None
    return code

def getStellaMethodCode(qualName, error=True):
    # Return the STELLA-wrapped code pointer for the STELLA method `qualName'
    # (which may be a function or method). If no such wrapper exists, raise an error.
    code = boot.lookupMethodCodeByName(to_bytes(qualName))
    if code is None or code == _ffi.NULL:
        if error:
            raise ApiException('Cannot access compiled C-code for STELLA method: ' + str(qualName))
        else:
            code = None
    return code

def createStellaFunctionCodePointerType(qualName):
    # Create the native function code pointer type signature for function `qualName'.
    csig = getStellaFunctionCSignature(qualName)
    defn = StringIO()
    # we use C++ bool type here, since cffi seems to be smart enough to handle that automatically:
    defn.write(stellaSigNativeCppReturnType(csig))
    defn.write(' (*) (')
    for i in range(stellaSigArity(csig)):
        if i > 0:
            defn.write(', ')
        pname = stellaSigParaNativeName(csig, i)
        ptype = stellaSigParaNativeCppType(csig, i)
        if stellaSigIsReturnPara(csig, i):
            ptype = pointerizeReturnParameterType(ptype)
        defn.write(ptype)
        defn.write(' ')
        defn.write(pname)
    defn.write(')')
    return defn.getvalue()

def getStellaFunctionTypedCode(qualName, code=None):
    """Similar to `getStellaFunctionCode' but properly types the resulting code as a function
       if necessary (based on `qualName's native signature) so it can be called from Python.
       This handles the code creation aspect of low-level CFun code objects."""
    if code is None:
        code = getStellaFunctionCode(qualName)
    if isinstance(code, _ffi.CData) and _ffi.typeof(code).kind == 'function':
        # already a properly typed ffi function pointer, simply return it:
        return code
    if not isinstance(code, _ffi.CData) and hasattr(code, '__call__'):
        # already a Python function or method object, simply return it:
        return code
    fntype = createStellaFunctionCodePointerType(qualName)
    return _ffi.cast(fntype, code)

# to define and link a C-level function we need to do something like this:
# >>> stella.cdef.typeToClass = getStellaFunctionTypedCode('stella/type-to-class')
