package com.robwilliamson.healthyesther;

public final class Main {
    private static final int ARG_COUNT = 3;
    private static final String USAGE =
            "<Source Folder> <Destination Folder> <Destination Package>";

    public static void main(String[] args) {
        if (args.length < ARG_COUNT) {
            System.err.printf("\nExpected %d arguments, %d received.\n", ARG_COUNT, args.length);
            printUsage();
            return;
        }

        if (!valuePresent(args[0], "Source Folder") ||
                !valuePresent(args[1], "Destination Folder") ||
                !valuePresent(args[2], "Destination Package")) {
            printUsage();
            return;
        }

        String sourceFolder = args[0];
        String destinationFolder = args[1];
        String destinationPackage = args[2];

        Log.v("sourceFolder = %s\ndestinationFolder = %s\ndestinationPackage = %s\n",
                sourceFolder,
                destinationFolder,
                destinationPackage);

        Source source = new Source(sourceFolder);
        Destination destination = new Destination(destinationFolder, destinationPackage);
        CodeGenerator generator = new CodeGenerator(source, destination);
        generator.generate();
    }

    private static void printUsage() {
        System.out.println(USAGE);
    }

    private static boolean valuePresent(String value, String name) {
        if (value == null || value.isEmpty()) {
            System.err.printf("\n%s was not provided.\n", name);
            return false;
        }
        return true;
    }
}
