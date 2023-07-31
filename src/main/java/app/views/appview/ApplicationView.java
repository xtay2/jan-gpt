package app.views.appview;

import app.managers.frontend.ViewManager;
import app.records.GPTModel;
import app.views.View;

import javax.swing.*;
import java.awt.*;

/**
 * @author Dennis Woithe
 */
public class ApplicationView implements View {

    public ViewManager manager;
    public MainFrame mainFrame;
    public TextQueryArea queryArea;
    public TextChatArea chatArea;
    public Sender sender;
    public JProgressBar progressBar;
    public TextChatName saveNameField;
    public JButton saveButton;
    public JButton deleteButton;
    public JScrollPane scrollableChatArea;
    public JScrollPane scrollableQueryArea;
//    public DropdownSavedChats dropdownSavedChats;
    public SavedChatsList savedChatsList; // Replace DropdownSavedChats with SavedChatsList
    public DropdownGPTModels dropdownGPTModels;
    public JLabel enterToSend;
    public PanelMain mainPanel;
    public PanelQuery queryPanel;
    public PanelChat chatPanel;
    public PanelNames panelNames;
    public PanelLeftSide leftSidePanel;
    public PanelRightSide rightSidePanel;
    public PanelCenter centerPanel;
    public ListenerMouseScroll mouseScrollListener;
    public int HEIGHT = 700, WIDTH = 1400;
    public int minHEIGHT = 500, minWIDTH = 1000;


    /**
     * @param manager The component that excepts data.
     */
    @Override
    public void start(ViewManager manager) {
        if (manager.hasAPIKey())
            buildMainFrame(manager);
        else
            new APIKeyFrame(manager, () -> buildMainFrame(manager));
    }

    private void buildMainFrame(ViewManager manager) {
        this.manager = manager;
        manager.setGPTModel(GPTModel.getNewest().orElseThrow());

        mainFrame = new MainFrame(this);
        chatArea = new TextChatArea();
        queryArea = new TextQueryArea(this);
        mouseScrollListener = new ListenerMouseScroll(chatArea, queryArea);
        queryArea.addMouseWheelListener(mouseScrollListener);
        chatArea.addMouseWheelListener(mouseScrollListener);
        enterToSend = new JLabel("Enter zum Absenden");
        sender = new Sender(this);
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(false);
        saveNameField = new TextChatName();

        deleteButton = new JButton("Alle löschen");
        ListenerDeleteChatsButton deleteButtonListener = new ListenerDeleteChatsButton(this);
        deleteButton.addActionListener(deleteButtonListener);

        saveButton = new JButton("Chat speichern");
        ListenerSaveButton saveButtonListener = new ListenerSaveButton(this);
        saveButton.addActionListener(saveButtonListener);


        savedChatsList = new SavedChatsList(this);


//        dropdownSavedChats = new DropdownSavedChats(this);


        dropdownGPTModels = new DropdownGPTModels(this);
        ListenerGPTModelsDropdown gptModelsListener = new ListenerGPTModelsDropdown(this);
        dropdownGPTModels.addActionListener(gptModelsListener);

        scrollableChatArea = new JScrollPane(chatArea);
        scrollableChatArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollableChatArea.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
        scrollableChatArea.setWheelScrollingEnabled(true);

        scrollableQueryArea = new JScrollPane(queryArea);
        scrollableQueryArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollableQueryArea.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
        scrollableQueryArea.setWheelScrollingEnabled(true);

        leftSidePanel = new PanelLeftSide(deleteButton);
        rightSidePanel = new PanelRightSide(dropdownGPTModels);
        centerPanel = new PanelCenter(saveNameField, saveButton);
        panelNames = new PanelNames(leftSidePanel, centerPanel, rightSidePanel);
        chatPanel = new PanelChat(chatArea, panelNames);
        queryPanel = new PanelQuery(queryArea, enterToSend, progressBar);
        mainPanel = new PanelMain(savedChatsList, queryPanel, chatPanel);

        mainFrame.getContentPane().add(mainPanel);
        mainFrame.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        mainFrame.setMinimumSize(new Dimension(minWIDTH, minHEIGHT));
        mainFrame.pack();
        mainFrame.setVisible(true);
        mainFrame = new MainFrame(this);
    }
}