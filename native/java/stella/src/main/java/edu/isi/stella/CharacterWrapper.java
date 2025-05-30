//  -*- Mode: Java -*-
//
// CharacterWrapper.java

/*
+---------------------------- BEGIN LICENSE BLOCK ---------------------------+
|                                                                            |
| Version: MPL 1.1/GPL 2.0/LGPL 2.1                                          |
|                                                                            |
| The contents of this file are subject to the Mozilla Public License        |
| Version 1.1 (the "License"); you may not use this file except in           |
| compliance with the License. You may obtain a copy of the License at       |
| http://www.mozilla.org/MPL/                                                |
|                                                                            |
| Software distributed under the License is distributed on an "AS IS" basis, |
| WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License   |
| for the specific language governing rights and limitations under the       |
| License.                                                                   |
|                                                                            |
| The Original Code is the STELLA Programming Language.                      |
|                                                                            |
| The Initial Developer of the Original Code is                              |
| UNIVERSITY OF SOUTHERN CALIFORNIA, INFORMATION SCIENCES INSTITUTE          |
| 4676 Admiralty Way, Marina Del Rey, California 90292, U.S.A.               |
|                                                                            |
| Portions created by the Initial Developer are Copyright (C) 1996-2023      |
| the Initial Developer. All Rights Reserved.                                |
|                                                                            |
| Contributor(s):                                                            |
|                                                                            |
| Alternatively, the contents of this file may be used under the terms of    |
| either the GNU General Public License Version 2 or later (the "GPL"), or   |
| the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),   |
| in which case the provisions of the GPL or the LGPL are applicable instead |
| of those above. If you wish to allow use of your version of this file only |
| under the terms of either the GPL or the LGPL, and not to allow others to  |
| use your version of this file under the terms of the MPL, indicate your    |
| decision by deleting the provisions above and replace them with the notice |
| and other provisions required by the GPL or the LGPL. If you do not delete |
| the provisions above, a recipient may use your version of this file under  |
| the terms of any one of the MPL, the GPL or the LGPL.                      |
|                                                                            |
+---------------------------- END LICENSE BLOCK -----------------------------+
*/

package edu.isi.stella;

import edu.isi.stella.javalib.*;

public class CharacterWrapper extends LiteralWrapper {
    public char wrapperValue;

  /** Return a literal object whose value is the CHARACTER 'value'.
   * @param value
   * @return CharacterWrapper
   */
  public static CharacterWrapper wrapCharacter(char value) {
    if (value == Stella.NULL_CHARACTER) {
      return (Stella.NULL_CHARACTER_WRAPPER);
    }
    else {
      return (CharacterWrapper.newCharacterWrapper(value));
    }
  }

  public static CharacterWrapper newCharacterWrapper(char wrapperValue) {
    { CharacterWrapper self = null;

      self = new CharacterWrapper();
      self.wrapperValue = wrapperValue;
      return (self);
    }
  }

  public void javaOutputLiteral() {
    { CharacterWrapper character = this;

      switch (character.wrapperValue) {
        case '\'': 
          ((OutputStream)(Stella.$CURRENT_STREAM$.get())).nativeStream.print("'\\''");
        break;
        case '\\': 
          ((OutputStream)(Stella.$CURRENT_STREAM$.get())).nativeStream.print("'\\\\'");
        break;
        case '\n': 
          ((OutputStream)(Stella.$CURRENT_STREAM$.get())).nativeStream.print("'\\n'");
        break;
        case '\b': 
          ((OutputStream)(Stella.$CURRENT_STREAM$.get())).nativeStream.print("'\\b'");
        break;
        case '\t': 
          ((OutputStream)(Stella.$CURRENT_STREAM$.get())).nativeStream.print("'\\t'");
        break;
        case '\r': 
          ((OutputStream)(Stella.$CURRENT_STREAM$.get())).nativeStream.print("'\\r'");
        break;
        case '\f': 
          ((OutputStream)(Stella.$CURRENT_STREAM$.get())).nativeStream.print("'\\f'");
        break;
        default:
          ((OutputStream)(Stella.$CURRENT_STREAM$.get())).nativeStream.print("'" + character.wrapperValue + "'");
        break;
      }
    }
  }

  public void cppOutputLiteral() {
    { CharacterWrapper character = this;

      { char ch = character.wrapperValue;

        switch (ch) {
          case '\'': 
            ((OutputStream)(Stella.$CURRENT_STREAM$.get())).nativeStream.print("'\\''");
          break;
          case '\\': 
            ((OutputStream)(Stella.$CURRENT_STREAM$.get())).nativeStream.print("'\\\\'");
          break;
          case '\n': 
            ((OutputStream)(Stella.$CURRENT_STREAM$.get())).nativeStream.print("'\\n'");
          break;
          case '\b': 
            ((OutputStream)(Stella.$CURRENT_STREAM$.get())).nativeStream.print("'\\b'");
          break;
          case '\t': 
            ((OutputStream)(Stella.$CURRENT_STREAM$.get())).nativeStream.print("'\\t'");
          break;
          case '\r': 
            ((OutputStream)(Stella.$CURRENT_STREAM$.get())).nativeStream.print("'\\r'");
          break;
          case '\f': 
            ((OutputStream)(Stella.$CURRENT_STREAM$.get())).nativeStream.print("'\\f'");
          break;
          case ' ': 
            ((OutputStream)(Stella.$CURRENT_STREAM$.get())).nativeStream.print("'\\0'");
          break;
          default:
            if (ch == Stella.NULL_CHARACTER) {
              ((OutputStream)(Stella.$CURRENT_STREAM$.get())).nativeStream.print("'\\0'");
            }
            else {
              ((OutputStream)(Stella.$CURRENT_STREAM$.get())).nativeStream.print("'" + ch + "'");
            }
          break;
        }
      }
    }
  }

  public boolean objectEqlP(Stella_Object y) {
    { CharacterWrapper x = this;

      return ((y != null) &&
          (((y.primaryType() == Stella.SGT_STELLA_CHARACTER_WRAPPER) ||
          Stella_Object.isaP(y, Stella.SGT_STELLA_CHARACTER_WRAPPER)) &&
           (x.wrapperValue == ((CharacterWrapper)(y)).wrapperValue)));
    }
  }

  public Stella_Object copyWrappedLiteral() {
    { CharacterWrapper self = this;

      return (CharacterWrapper.wrapCharacter(self.wrapperValue));
    }
  }

  /** Unwrap <code>wrapper</code> and return the result.
   * Return NULL if <code>wrapper</code> is NULL.
   * @param wrapper
   * @return char
   */
  public static char unwrapCharacter(CharacterWrapper wrapper) {
    if (wrapper == null) {
      return (Stella.NULL_CHARACTER);
    }
    else {
      return (wrapper.wrapperValue);
    }
  }

  public int hashCode_() {
    { CharacterWrapper self = this;

      return (((Stella.$HASH_BYTE_RANDOM_TABLE$[(int) (self.wrapperValue)]) ^ 15119378));
    }
  }

  public void printObject(edu.isi.stella.javalib.NativeOutputStream stream) {
    { CharacterWrapper self = this;

      { char value = self.wrapperValue;

        if (value == Stella.NULL_CHARACTER) {
          if (((Boolean)(Stella.$PRINTREADABLYp$.get())).booleanValue()) {
            stream.print(Stella.SYM_STELLA_NULL_CHARACTER);
          }
          else {
            stream.print("|L|NULL-CHARACTER");
          }
        }
        else {
          {
            if (!(((Boolean)(Stella.$PRINTREADABLYp$.get())).booleanValue())) {
              stream.print("|L|");
            }
            Stella.printCharacter(value, stream);
          }
        }
      }
    }
  }

  public static Stella_Object accessCharacterWrapperSlotValue(CharacterWrapper self, Symbol slotname, Stella_Object value, boolean setvalueP) {
    if (slotname == Stella.SYM_STELLA_WRAPPER_VALUE) {
      if (setvalueP) {
        self.wrapperValue = ((CharacterWrapper)(value)).wrapperValue;
      }
      else {
        value = CharacterWrapper.wrapCharacter(self.wrapperValue);
      }
    }
    else {
      { OutputStringStream stream000 = OutputStringStream.newOutputStringStream();

        stream000.nativeStream.print("`" + slotname + "' is not a valid case option");
        throw ((StellaException)(StellaException.newStellaException(stream000.theStringReader()).fillInStackTrace()));
      }
    }
    return (value);
  }

  public Surrogate primaryType() {
    { CharacterWrapper self = this;

      return (Stella.SGT_STELLA_CHARACTER_WRAPPER);
    }
  }

}

