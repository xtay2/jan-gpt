package app.records.views.appview;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Vector;

/**
 * This class is used to show the saved conversations in a list.
 * Author: A.Mukhamedov
 */
public class SavedChatsList extends JList<String> {
    public static final String NEW_CHAT = "<Neuer Chat>";
    public Vector<String> chats;

    private final ApplicationView app;
    public SavedChatsList(ApplicationView app) {
        super();
        this.app = app;
        chats = new Vector<>(app.manager.getConversations().orElse(Collections.emptyList()));
        if(!chats.contains(NEW_CHAT))
            chats.add(0, NEW_CHAT);
        setListData(chats);
        Dimension listSize = new Dimension(150, 100);
        setPreferredSize(listSize);
        ListenerClickedSavedChatsList savedChatsListListener = new ListenerClickedSavedChatsList(app);
        addListSelectionListener(savedChatsListListener);
    }

    public void concurrentlyUpdateList() {
        if (chats.contains(app.currentChatName)) {
            app.manager.deleteConversation(app.currentChatName);
            app.manager.saveConversationAs(app.currentChatName);
        } else if( app.currentChatNameBox.getText().isBlank() && app.currentChatName.isEmpty()) {
            var namelessChat = "Chat vom " + new SimpleDateFormat("yy-MM-dd").format(new Date()) + " um " + new SimpleDateFormat("HH-mm-ss").format(new Date());
            app.manager.saveConversationAs(namelessChat);
            updateList();
            app.currentChatName = namelessChat;
        }
        System.out.println("SavedChatsList concurrentlyUpdateList() called");
    }

    public void updateList(){
        chats = new Vector<>(app.manager.getConversations().orElse(Collections.emptyList()));
        if(!chats.contains(NEW_CHAT))
            chats.add(0, NEW_CHAT);
        setListData(chats);
    }
}
