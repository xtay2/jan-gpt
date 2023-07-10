package app.factories;


import app.managers.frontend.BasicViewManager;
import app.managers.frontend.ViewManager;
import app.views.ApplicationView;
import app.views.ConsoleView;
import app.views.View;

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