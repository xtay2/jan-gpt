package app.views.appview;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.Vector;

/**
 * This class is used to show the saved conversations in a list.
 * Author: A.Mukhamedov
 */
public class SavedChatsList extends JList<String> {
    public static final String NEW_CHAT = "Neuer Chat";
    public SavedChatsList(ApplicationView app) {
        super();
        var chats = new Vector<>(app.manager.getConversations().orElse(Collections.emptyList()));
        chats.add(0, NEW_CHAT);
        setListData(chats);

        // Set preferred size for the list
        Dimension listSize = new Dimension(150, 100);
        setPreferredSize(listSize);
//        setMaximumSize(listSize);
//        setMinimumSize(listSize);
        ListenerSavedChatsList savedChatsListListener = new ListenerSavedChatsList(app);
        addListSelectionListener(savedChatsListListener);


    }
}
