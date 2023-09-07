package app.factories;

import app.managers.frontend.BasicViewManager;
import app.managers.frontend.ViewManager;
import app.records.views.ConsoleView;
import app.records.views.View;
import app.records.views.appview.ApplicationView;

/**
 * @author Dennis Woithe
 */
public interface ViewFactory {

    /**
     * Create a view of the given type.
     * Valid types are: console, application
     */
    static View createView(String type) {
        return switch (type) {
            case "console" -> new ConsoleView();
            case "application" -> new ApplicationView();
            default -> throw new IllegalArgumentException("Unknown view-type: " + type + "\n" +
                    "Please use one of the following: console, application");
        };
    }

    /**
     * Create the newest view manager.
     */
    static ViewManager createViewManager() {
        return new BasicViewManager();
    }

}