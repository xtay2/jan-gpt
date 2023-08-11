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
    public TextAreaQuery queryArea;
    public TextAreaChat chatArea;
    public SenderAndReceiver senderAndReceiver;
    public JProgressBar progressBar;
    public TextFieldChatName saveNameField;
    public JButton saveButton;
    public JButton deleteAllButton;
    public JButton deleteSelectedButton;
    public JButton copyButton;
    public JScrollPane scrollableChatArea;
    public JScrollPane scrollableQueryArea;
    public SavedChatsList savedChatsList;
    public DropdownGPTModels dropdownGPTModels;
    public JLabel savedChatsLabel;
    public PanelMain mainPanel;
    public PanelQuery queryPanel;
    public PanelChat chatPanel;
    public PanelLeftSide leftSidePanel;
    public PanelRightSide rightSidePanel;
    public PanelButtons buttonsPanel;
    public PanelLeftSideBottom tooltipPanel;
    public PanelProgress progressPanel;
    public Tooltip tooltip;
    public Wrapper wrapper;
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
        chatArea = new TextAreaChat();
        queryArea = new TextAreaQuery(this);
        savedChatsLabel = new JLabel("Gespeicherte Chats:");
        senderAndReceiver = new SenderAndReceiver(this);
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(false);
        saveNameField = new TextFieldChatName();
        savedChatsList = new SavedChatsList(this);
        dropdownGPTModels = new DropdownGPTModels(this);
        saveButton = new JButton("Chat speichern");
        saveButton.addActionListener(new ListenerButtonSaveChat(this));
        deleteSelectedButton = new JButton("Chat löschen");
        deleteSelectedButton.addActionListener(new ListenerButtonDeleteChat(this));
        deleteAllButton = new JButton("Alle löschen");
        deleteAllButton.addActionListener(new ListenerButtonDeleteAllChats(this));
        tooltip = new Tooltip(" ♿");
//      tooltip = new JLabel("ⓘ");
        copyButton = new JButton("Code kopieren");
        copyButton.setVisible(false);
        wrapper = new Wrapper(this);

        scrollableChatArea = new JScrollPane(chatArea);
        scrollableChatArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollableChatArea.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
        scrollableChatArea.setWheelScrollingEnabled(true);

        scrollableQueryArea = new JScrollPane(queryArea);
        scrollableQueryArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollableQueryArea.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
        scrollableQueryArea.setWheelScrollingEnabled(true);
        scrollableQueryArea.setPreferredSize(new Dimension(1000, 150));

        tooltipPanel = new PanelLeftSideBottom(tooltip, dropdownGPTModels);
        buttonsPanel = new PanelButtons(copyButton, saveNameField, saveButton, deleteSelectedButton, deleteAllButton, tooltipPanel);
        leftSidePanel = new PanelLeftSide(savedChatsLabel, savedChatsList, buttonsPanel);
        chatPanel = new PanelChat(chatArea);
        progressPanel = new PanelProgress(progressBar);
        queryPanel = new PanelQuery(scrollableQueryArea, progressPanel);
        rightSidePanel = new PanelRightSide(chatPanel, queryPanel);
        mainPanel = new PanelMain(leftSidePanel, rightSidePanel);

        mainFrame.getContentPane().add(mainPanel);
        mainFrame.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        mainFrame.setMinimumSize(new Dimension(minWIDTH, minHEIGHT));
        mainFrame.pack();
        mainFrame.setVisible(true);
        mainFrame = new MainFrame(this);
        queryArea.requestFocus();
    }
}