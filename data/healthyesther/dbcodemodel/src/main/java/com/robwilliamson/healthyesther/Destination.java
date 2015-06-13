package com.robwilliamson.healthyesther;

import java.io.File;

class Destination {
    public static class DestinationException extends RuntimeException {
        public DestinationException(String message, Object ... parameters) {
            super(String.format(message, parameters));
        }
    }

    private final File mFolder;
    private final String mPackage;

    Destination(String destinationFolder, String destinationPackage) {
        mFolder = new File(Strings.checkNotEmpty(destinationFolder, "destinationFolder"));
        mPackage = Strings.checkNotEmpty(destinationPackage, "destinationPackage");

        if (!mFolder.exists() && !mFolder.mkdirs()) {
            throw new DestinationException("Could not create %s", destinationFolder);
        }
    }
}
