package main.java;

import main.java.app.factories.ViewFactory;

import java.io.File;
import java.io.IOException;

/**
 * @author Dennis Woithe
 */
public class Main {

    public static final String BASE_DATA_PATH = "data" + File.separator;

    /**
     * Creates the views and the manager and starts the application.
     *
     * @param args One argument specifying the view-type to use.
     */
    public static void main(String[] args) throws IOException {
        if (args.length != 1)
            throw new IllegalArgumentException("Please provide a view-type as argument." + "\n" +
                    "Please use one of the following: console, application");
        var view = ViewFactory.createView(args[0]);
        view.start(ViewFactory.createViewManager());
    }

}