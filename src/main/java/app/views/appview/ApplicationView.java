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
    public Sender sender;
    public JProgressBar progressBar;
    public TextFieldChatName saveNameField;
    public JButton saveButton;
    public JButton deleteAllButton;
    public JButton deleteSelectedButton;
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
    public JLabel tooltip;
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
        sender = new Sender(this);
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(false);
        saveNameField = new TextFieldChatName();
        savedChatsList = new SavedChatsList(this);
        dropdownGPTModels = new DropdownGPTModels(this);
        saveButton = new JButton("Chat speichern");
        ListenerButtonSaveChat saveButtonListener = new ListenerButtonSaveChat(this);
        saveButton.addActionListener(saveButtonListener);
        deleteSelectedButton = new JButton("Chat löschen");
        ListenerButtonDeleteChat deleteSelectedButtonListener = new ListenerButtonDeleteChat(this);
        deleteSelectedButton.addActionListener(deleteSelectedButtonListener);
        deleteAllButton = new JButton("Alle löschen");
        ListenerButtonDeleteAllChats deleteAllButtonListener = new ListenerButtonDeleteAllChats(this);
        deleteAllButton.addActionListener(deleteAllButtonListener);

//      tooltip = new JLabel("ⓘ");
        tooltip = new JLabel("♿");
        Font customFont = new Font("Segoe UI Symbol", Font.PLAIN, 18); // a font that supports the "ⓘ" symbol
        tooltip.setFont(customFont);
        tooltip.setVisible(true);
        tooltip.setToolTipText("<html>" +
                "↑ : letzte Frage <br/>" +
                "↓ : nächste Frage <br/>" +
                "↵ : Frage absenden <br/>" +
                "␛ : Frage löschen <br/>" +
                "⇧ + ↵ : neue Zeile <br/>" +
                "Strg + s : Frage speichern <br/>" +
                "Strg + ⇕ : Schriftgröße ändern <br/>" +
                "</html>");
        // Set the initial delay for the tooltip in milliseconds (default is 500 ms)
        ToolTipManager.sharedInstance().setInitialDelay(100); // 1000 ms = 1 second
        // Set the dismiss delay for the tooltip in milliseconds (default is 4,000 ms)
        ToolTipManager.sharedInstance().setDismissDelay(10000); // 3000 ms = 3 seconds

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
        buttonsPanel = new PanelButtons(saveNameField, saveButton, deleteSelectedButton, deleteAllButton, tooltipPanel);
        leftSidePanel = new PanelLeftSide(savedChatsLabel, savedChatsList, buttonsPanel);

        chatPanel = new PanelChat(chatArea);
        queryPanel = new PanelQuery(scrollableQueryArea, progressBar);
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