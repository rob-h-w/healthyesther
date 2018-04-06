/**
  * © Robert Williamson 2014-2018.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther;

import java.io.File;

public class Destination {

    private final File mFolder;
    private final String mPackage;

    Destination(String destinationFolder, String destinationPackage) {
        mFolder = new File(Strings.checkNotEmpty(destinationFolder, "destinationFolder"));
        mPackage = Strings.checkNotEmpty(destinationPackage, "destinationPackage");

        if (!mFolder.exists() && !mFolder.mkdirs()) {
            throw new DestinationException("Could not create %s", destinationFolder);
        }
    }

    public File getFolder() {
        return mFolder;
    }

    public String getPackage() {
        return mPackage;
    }

    public static class DestinationException extends RuntimeException {
        public DestinationException(String message, Object... parameters) {
            super(String.format(message, parameters));
        }
    }
}
