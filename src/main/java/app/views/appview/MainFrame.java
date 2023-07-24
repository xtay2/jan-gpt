package main.java.app.views.appview;

import javax.swing.*;
import java.awt.*;

/**
 * This class is responsible for the main window of the application.
 *
 * @author A.Mukhamedov
 */
public class MainFrame extends javax.swing.JFrame{
    public int HEIGHT = 500;
    public int WIDTH = 1000;

    public MainFrame(ApplicationView app) {
        super(app.manager.getGPTModel().map(m -> m.modelName).orElse("Kein Modell geladen"));
        setBounds(100, 100, WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        setResizable(true);

//        app.queryArea = new QueryArea(app);
//        app.enterToSend = new JLabel("Enter zum Absenden");
//        app.chatArea = new ChatArea();
//        app.fontChangeListener = new FontChangeListener(app.chatArea);
//        app.chatArea.addMouseWheelListener(app.fontChangeListener);
//        app.sender = new Sender(app);
//        app.progressBar = new JProgressBar();
//        app.progressBar.setIndeterminate(false);
//        app.currentChat = new ChatName();
//
//        app.deleteButton = new JButton("Alle löschen");
//        DeleteChatsButtonListener deleteButtonListener = new DeleteChatsButtonListener(app);
//        app.deleteButton.addActionListener(deleteButtonListener);
//
//        app.saveButton = new JButton("Chat speichern");
//        SaveButtonListener saveButtonListener = new SaveButtonListener(app);
//        app.saveButton.addActionListener(saveButtonListener);
//
//        app.dropdownSavedChats = new SavedChats(app);
//        SavedChatsDropdownListener savedChatsListener = new SavedChatsDropdownListener(app);
//        app.dropdownSavedChats.addActionListener(savedChatsListener);
//
//        app.dropdownGPTModels = new GPTModels(app);
//        GPTModelsDropdownListener gptModelsListener = new GPTModelsDropdownListener(app);
//        app.dropdownGPTModels.addActionListener(gptModelsListener);
//
//        app.scrollableChatArea = new JScrollPane(app.chatArea);
//        app.scrollableChatArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
//        app.scrollableChatArea.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
//
//        app.scrollableQueryArea = new JScrollPane(app.queryArea);
//        app.scrollableQueryArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
//        app.scrollableQueryArea.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
//
//        app.buttons = new ButtonsPanel(app.saveButton, app.deleteButton);
//        app.oldChatsPanel = new OldChatsPanel(app.currentChat, app.buttons, app.dropdownSavedChats);
//        app.currentChatPanel = new ChatPanel(app.chatArea, app.oldChatsPanel);
//        app.queryPanel = new QueryPanel(app.queryArea, app.enterToSend, app.dropdownGPTModels, app.progressBar);
//        app.mainPanel = new MainPanel(app.queryPanel, app.currentChatPanel);
//
//        getContentPane().add(app.mainPanel);
//        setMinimumSize(new Dimension(WIDTH, HEIGHT));
//        pack();
//        setVisible(true);
//        SwingUtilities.invokeLater(() -> app.queryArea.requestFocusInWindow());

    }
}

