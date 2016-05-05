/**
  * © Robert Williamson 2014-2016.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.test;

import com.robwilliamson.healthyesther.App;

public class Strings {
    static public String from(int resource) {
        return App.getInstance().getResources().getString(resource);
    }
}
