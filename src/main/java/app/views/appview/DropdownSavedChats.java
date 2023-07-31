package app.views.appview;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.Vector;

/**
 * This class is used to show the saved conversations in a drop-down list.
 *
 * @author A.Mukhamedov
 */
public class DropdownSavedChats extends JComboBox<String> {
    public static final String NEW_CHAT = "Neuer Chat";
    public DropdownSavedChats(ApplicationView app) {
        super();
        var chats = new Vector<>(app.manager.getConversations().orElse(Collections.emptyList()));
        chats.add(0, NEW_CHAT);
        setModel(new DefaultComboBoxModel<>(chats));
        // Set preferred size for the dropdown menu
        Dimension dropdownSize = new Dimension(150, 30);
        setPreferredSize(dropdownSize);
        setMaximumSize(dropdownSize);
        setMinimumSize(dropdownSize);
        ListenerSavedChatsDropdown savedChatsListener = new ListenerSavedChatsDropdown(app);
        addActionListener(savedChatsListener);
    }

}