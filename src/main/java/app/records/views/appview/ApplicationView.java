package app.records.views.appview;

import app.managers.frontend.ViewManager;
import app.records.GPTModel;
import app.records.views.View;

import javax.swing.*;
import java.awt.*;

/**
 * @author Dennis Woithe
 */
public class ApplicationView implements View {

    public ViewManager manager;
    public MainFrame mainFrame;
    public TextPaneQuery queryPane;
    public TextPaneChat chatPane;
    public SenderAndReceiver senderAndReceiver;
    public JProgressBar progressBar;
    public SaveCurrentChatNameField currentChatNameBox;
    public String currentChatName;
    public JButton saveButton;
    public JButton deleteAllButton;
    public JButton deleteSelectedButton;
    public JScrollPane scrollableChat;
    public JScrollPane scrollableQuery;
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
        chatPane = new TextPaneChat();
        queryPane = new TextPaneQuery(this);
        queryPane.initHint();
        savedChatsLabel = new JLabel("Gespeicherte Chats:", SwingConstants.CENTER);
        senderAndReceiver = new SenderAndReceiver(this);
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(false);
        currentChatNameBox = new SaveCurrentChatNameField();
        savedChatsList = new SavedChatsList(this);
        dropdownGPTModels = new DropdownGPTModels(this);
        saveButton = new JButton("Chat speichern");
        saveButton.addActionListener(new ListenerButtonSaveChat(this));
        deleteSelectedButton = new JButton("Chat löschen");
        deleteSelectedButton.addActionListener(new ListenerButtonDeleteChat(this));
        deleteAllButton = new JButton("Alle löschen");
        deleteAllButton.addActionListener(new ListenerButtonDeleteAllChats(this));
        tooltip = new Tooltip(" ♿");
      //  tooltip = new Tooltip(" ⓘ");
        wrapper = new Wrapper(this);
        currentChatName = "";

        scrollableChat = new JScrollPane(chatPane);
        scrollableChat.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollableChat.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
        scrollableChat.setWheelScrollingEnabled(true);

        scrollableQuery = new JScrollPane(queryPane);
        scrollableQuery.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollableQuery.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
        scrollableQuery.setWheelScrollingEnabled(true);
        scrollableQuery.setPreferredSize(new Dimension(1000, 150));

        tooltipPanel = new PanelLeftSideBottom(tooltip, dropdownGPTModels);
        buttonsPanel = new PanelButtons(currentChatNameBox, saveButton, deleteSelectedButton, deleteAllButton, tooltipPanel);
        leftSidePanel = new PanelLeftSide(savedChatsLabel, savedChatsList, buttonsPanel);
        chatPanel = new PanelChat(chatPane);
        progressPanel = new PanelProgress(progressBar);
        queryPanel = new PanelQuery(scrollableQuery, progressPanel);
        rightSidePanel = new PanelRightSide(chatPanel, queryPanel);
        mainPanel = new PanelMain(leftSidePanel, rightSidePanel);

        mainFrame.getContentPane().add(mainPanel);
        mainFrame.setTitle("Neuer Chat");
        mainFrame.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        mainFrame.setMinimumSize(new Dimension(minWIDTH, minHEIGHT));
        mainFrame.pack();
        mainFrame.setVisible(true);
        mainFrame = new MainFrame(this);
        queryPane.requestFocus();

    }
}