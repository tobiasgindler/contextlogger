package de.holisticon.util.tracee.contextlogger.json.generator;

import de.holisticon.util.tracee.contextlogger.json.beans.ExceptionCategory;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Factory for Exception context specific data.
 * Created by Tobias Gindler, holisticon AG on 19.12.13.
 */
public final class ExceptionCategoryCreator {

    private ExceptionCategoryCreator() {

    }

    public static ExceptionCategory createExceptionCategory(Throwable throwable) {

        return new ExceptionCategory(throwable.getMessage(), createStacktrace(throwable));

    }

    private static String createStacktrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        throwable.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

}
