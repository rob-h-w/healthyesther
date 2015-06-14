package com.robwilliamson.healthyesther;

import com.google.gson.Gson;
import com.robwilliamson.healthyesther.type.Database;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

class CodeGenerator {
    private final Source mSource;
    private final Destination mDestination;

    CodeGenerator(Source source, Destination destination) {
        mSource = source;
        mDestination = destination;
    }

    public void generate() {
        Database dbFromJson = parseDatabase();
    }

    private Database parseDatabase() {
        Gson gson = new Gson();
        FileInputStream is = null;
        Throwable throwable = null;
        Database dbFromJson = null;

        try {
            is = new FileInputStream(mSource.getJsonFile());
            dbFromJson = gson.fromJson(new InputStreamReader(is), Database.class);
        } catch (FileNotFoundException e) {
            throwable = e;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    throwable = e;
                }
            }
        }

        if (throwable != null) {
            throw new Source.SourceMissingException("Could not open the source JSON.", throwable);
        }

        if (dbFromJson == null) {
            throw new Source.SourceMissingException("Could not parse the JSON.");
        }

        return dbFromJson;
    }
}
