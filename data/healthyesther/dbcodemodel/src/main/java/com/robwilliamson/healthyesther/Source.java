package com.robwilliamson.healthyesther;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.regex.*;

class Source {
    public static class SourceMissingException extends RuntimeException {
        public SourceMissingException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    private final File mJsonFile;

    Source(String sourceDirectory) {
        try {
            mJsonFile =
                    getMostRecentDbJsonFile(Strings.checkNotEmpty(sourceDirectory, "sourceDirectory"));
        } catch (FileNotFoundException fileNotFound) {
            throw new SourceMissingException("Could not find most recent JSON file.", fileNotFound);
        }

        Log.v("Using source JSON file: %s", mJsonFile.getAbsolutePath());
    }

    private File getMostRecentDbJsonFile(String sourceDirectory) throws FileNotFoundException {
        File directory = new File(sourceDirectory);
        if (!directory.exists()) {
            throw new FileNotFoundException(String.format("Could not find %s", sourceDirectory));
        }

        File jsonFile = null;
        int highestVersion = -1;

        File[] files = directory.listFiles();
        if ( files == null) {
            throw new FileNotFoundException("No files found in the source folder.");
        }

        // Search for files that contain a single integer version number, such as an Android DB
        // version.
        Pattern pattern = Pattern.compile(".*(\\d+).*");

        for (File file : files) {
            if (file.isDirectory()) {
                continue;
            }

            String fileName = file.getName();
            Matcher matcher = pattern.matcher(fileName);
            if (!matcher.matches() || matcher.groupCount() < 1) {
                continue;
            }

            int version = Integer.parseInt(matcher.group(1));
            if (version > highestVersion) {
                highestVersion = version;
                jsonFile = file;
            }
        }

        if (jsonFile == null) {
            throw new FileNotFoundException(sourceDirectory);
        }

        return jsonFile;
    }
}
