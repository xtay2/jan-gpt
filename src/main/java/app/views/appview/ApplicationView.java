package main.java.app.views.appview;

import main.java.app.managers.frontend.ViewManager;
import main.java.app.records.GPTModel;
import main.java.app.views.View;

import javax.swing.*;
import javax.swing.JProgressBar;
import java.awt.*;

/**
 * @author Dennis Woithe
 */
public class ApplicationView implements View {

    public ViewManager manager;
    public MainFrame mainFrame;
    public QueryArea queryArea;
    public ChatArea chatArea;
    public Sender sender;
    public JProgressBar progressBar;
    public ChatName currentChat;
    public JButton saveButton;
    public JButton deleteButton;
    public JScrollPane scrollableChatArea;
    public JScrollPane scrollableQueryArea;
    public SavedChats dropdownSavedChats;
    public GPTModels dropdownGPTModels;
    public JLabel enterToSend;
    public MainPanel mainPanel;
    public QueryPanel queryPanel;
    public ChatPanel currentChatPanel;
    public OldChatsPanel oldChatsPanel;
    public ButtonsPanel buttons;
    public MouseScrollListener mouseScrollListener;
    public int HEIGHT = 500;
    public int WIDTH = 1000;


    /**
     * @param manager The component that excepts data.
     */
    @Override
    public void start(ViewManager manager) {
        if (manager.hasAPIKey())
            buildMainFrame(manager);
        else  // manager.setAPIKey("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");
            new main.java.app.views.appview.APIKeyFrame(manager, () -> buildMainFrame(manager));

    }

    private void buildMainFrame(ViewManager manager) {
        this.manager = manager;
        manager.setGPTModel(GPTModel.getNewest().orElseThrow());

        mainFrame = new MainFrame(this);
        chatArea = new ChatArea();
        queryArea = new QueryArea(this);
        mouseScrollListener = new MouseScrollListener(chatArea, queryArea);
        queryArea.addMouseWheelListener(mouseScrollListener);
        chatArea.addMouseWheelListener(mouseScrollListener);
        enterToSend = new JLabel("Enter zum Absenden");
        sender = new Sender(this);
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(false);
        currentChat = new ChatName();

        deleteButton = new JButton("Alle löschen");
        DeleteChatsButtonListener deleteButtonListener = new DeleteChatsButtonListener(this);
        deleteButton.addActionListener(deleteButtonListener);

        saveButton = new JButton("Chat speichern");
        SaveButtonListener saveButtonListener = new SaveButtonListener(this);
        saveButton.addActionListener(saveButtonListener);

        dropdownSavedChats = new SavedChats(this);
        SavedChatsDropdownListener savedChatsListener = new SavedChatsDropdownListener(this);
        dropdownSavedChats.addActionListener(savedChatsListener);

        dropdownGPTModels = new GPTModels(this);
        GPTModelsDropdownListener gptModelsListener = new GPTModelsDropdownListener(this);
        dropdownGPTModels.addActionListener(gptModelsListener);

        scrollableChatArea = new JScrollPane(chatArea);
        scrollableChatArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollableChatArea.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
        scrollableChatArea.setWheelScrollingEnabled(true);

        scrollableQueryArea = new JScrollPane(queryArea);
        scrollableQueryArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollableQueryArea.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
        scrollableQueryArea.setWheelScrollingEnabled(true);

        buttons = new ButtonsPanel(saveButton, deleteButton);
        oldChatsPanel = new OldChatsPanel(currentChat, buttons, dropdownSavedChats);
        currentChatPanel = new ChatPanel(chatArea, oldChatsPanel);
        queryPanel = new QueryPanel(queryArea, enterToSend, dropdownGPTModels, progressBar);
        mainPanel = new MainPanel(queryPanel, currentChatPanel);

        mainFrame.getContentPane().add(mainPanel);
        mainFrame.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        mainFrame.pack();
        mainFrame.setVisible(true);
        mainFrame = new MainFrame(this);
    }
}


