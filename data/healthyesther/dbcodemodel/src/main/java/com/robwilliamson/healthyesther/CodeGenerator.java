/**
  * © Robert Williamson 2014-2016.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther;

import com.google.gson.Gson;
import com.robwilliamson.healthyesther.type.Database;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JPackage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class CodeGenerator {
    public static final AsyncExecutor ASYNC = new AsyncExecutor();
    private final Source mSource;
    private final Destination mDestination;

    CodeGenerator(Source source, Destination destination) {
        mSource = source;
        mDestination = destination;
    }

    public void generate() {
        Database dbFromJson = parseDatabase();
        JCodeModel codeModel = new JCodeModel();
        JPackage rootPackage = codeModel._package(mDestination.getPackage());
        try {
            new com.robwilliamson.healthyesther.generator.Database(
                    dbFromJson,
                    rootPackage);
            ASYNC.execute();
            codeModel.build(mDestination.getFolder());
        } catch (JClassAlreadyExistsException | IOException e) {
            throw new GenerationException(e);
        }
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

    public static class GenerationException extends RuntimeException {
        public GenerationException(Throwable t) {
            super(t);
        }
    }
}
