package main.java.app.views;

import main.java.app.managers.frontend.ViewManager;

/**
 * @author Dennis Woithe
 */
public interface View {

    /**
     * Start the view.
     *
     * @param manager The component that excepts data.
     */
    void start(ViewManager manager);

}