//  -*- Mode: Java -*-
//
// oneDFloatArray.java

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

/** 1-dimensional array with float values.
 * @author Stella Java Translator
 */
public class oneDFloatArray extends AbstractDimensionalArray {
    public int dim1;
    public double[] theArray;

  public static oneDFloatArray new1DFloatArray(int dim1) {
    { oneDFloatArray self = null;

      self = new oneDFloatArray();
      self.dim1 = dim1;
      self.theArray = null;
      self.initializeDimensionalArray();
      return (self);
    }
  }

  public double[] theArrayReader() {
    { oneDFloatArray self = this;

      return (self.theArray);
    }
  }

  public void initializeDimensionalArray() {
    { oneDFloatArray self = this;

      self.theArray = new double[self.length()];
    }
  }

  /** Return the total number of elements in <code>self</code>.
   * @return int
   */
  public int length() {
    { oneDFloatArray self = this;

      return (self.dim1);
    }
  }

  /** Initialize the elements of <code>self</code> with <code>initialvalue</code>.
   * @param initialvalue
   */
  public void initializeArray(double initialvalue) {
    { oneDFloatArray self = this;

      { double[] array = self.theArray;

        { int i = Stella.NULL_INTEGER;
          int iter000 = 0;
          int upperBound000 = self.length() - 1;

          for (;iter000 <= upperBound000; iter000 = iter000 + 1) {
            i = iter000;
            array[i] = initialvalue;
          }
        }
      }
    }
  }

  /** Return the element of <code>self</code> at position <code>JiK</code>.
   * @param i
   * @return double
   */
  public double oneDAref(int i) {
    { oneDFloatArray self = this;

      return (self.theArray[i]);
    }
  }

  /** Set the element of <code>self</code> at position <code>JiK</code> to <code>value</code>
   * and return the result.
   * @param value
   * @param i
   * @return double
   */
  public double oneDArefSetter(double value, int i) {
    { oneDFloatArray self = this;

      return (self.theArray[i] = value);
    }
  }

  /** Return the 1D address of the element at position <code>JiK</code>.
   * This is useful for fast element-wise iteration that doesn't need arithmetic.
   * @param i
   * @return int
   */
  public int oneDArefAddress(int i) {
    { oneDFloatArray self = this;

      return (i);
    }
  }

  public Surrogate primaryType() {
    { oneDFloatArray self = this;

      return (Stella.SGT_STELLA_1D_FLOAT_ARRAY);
    }
  }

}

