package app.records.views.appview;

import app.Main;
import app.managers.frontend.ViewManager;
import app.records.GPTModel;
import app.records.views.View;
import app.records.views.appview.listeners.*;
import app.records.views.appview.panels.*;
import app.records.views.appview.textfields.SaveCurrentChatNameField;
import app.records.views.appview.textfields.TextPaneChat;
import app.records.views.appview.textfields.TextPaneQuery;
//import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;

/**
 * @author Dennis Woithe
 */
public class ApplicationView implements View {

    public final Path PREFERRED_MODEL_FILE_PATH = Path.of(Main.BASE_DATA_PATH + "preferred_model.txt");
    public final Path PREFERRED_TIMEOUT_FILE_PATH = Path.of(Main.BASE_DATA_PATH + "preferred_timeout.txt");
    public ViewManager manager;
    public MainFrame mainFrame;
    public TextPaneQuery queryPane;
    public TextPaneChat chatPane;
    public JTextArea unformattedChat;
    public SenderReceiver senderReceiver;
    public JProgressBar progressBar;
    public SaveCurrentChatNameField currentChatNameField;
    public String currentHypenizedChatName;
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
    public PanelTimeout timeoutPanel;
    public Tooltip tooltipCommands;
    public JLabel timeoutLabel;
    public JTextField timeoutTextField;
    public int timeoutValue;
    public int HEIGHT = 650, WIDTH = 1250;
    public ToolTipManager globalToolTipManager;

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

    void buildMainFrame(ViewManager manager) {
        this.manager = manager;
        rememberPreferredModel();

        mainFrame = new MainFrame(this);
        chatPane = new TextPaneChat();
        unformattedChat = new JTextArea();
        chatPane.addMouseWheelListener(new ListenerMouseScrollChat(this));
        queryPane = new TextPaneQuery(this);
        queryPane.initHint();
        savedChatsLabel = new JLabel("Gespeicherte Chats:", SwingConstants.CENTER);
        senderReceiver = new SenderReceiver(this);
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(false);
        timeoutValue = getPreferredTimeout();
        timeoutTextField = new JTextField(String.valueOf(timeoutValue));
        timeoutTextField.addFocusListener(new ListenerFocusTimeoutText(this));
        timeoutTextField.addKeyListener(new ListenerKeyPressedTimeoutText(this));
        timeoutTextField.setEditable(true);
        timeoutLabel = new JLabel("maximale Wartezeit: ");
        timeoutLabel.setToolTipText("<html>" +
                "Defaultmäßig ist die Wartezeit auf 30 Sekunden gesetzt. <br/>" +
                "Sobald du sie änderst, merke ich sie mir. <br/>" +
                "</html>");
        currentChatNameField = new SaveCurrentChatNameField(this);
        currentChatNameField.addKeyListener(new ListenerKeyPressedNameField(this));
        savedChatsList = new SavedChatsList(this);
        dropdownGPTModels = new DropdownGPTModels(this);
        saveButton = new JButton("Chat speichern");
        saveButton.addActionListener(new ListenerButtonSaveChat(this));
        deleteSelectedButton = new JButton("Chat löschen");
        deleteSelectedButton.addActionListener(new ListenerButtonDeleteChat(this));
        deleteAllButton = new JButton("Alle löschen");
        deleteAllButton.addActionListener(new ListenerButtonDeleteAllChats(this));
        // a a tooltip displaying an ⓘ
        String INFO = "<html><b><span style='font-size: 16px;'>ⓘ</span></b></html>";
        tooltipCommands = new Tooltip(INFO);
        currentHypenizedChatName = "";
        globalToolTipManager = ToolTipManager.sharedInstance();
        globalToolTipManager.setInitialDelay(800);

        scrollableChat = new JScrollPane(chatPane);
        scrollableChat.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollableChat.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
        scrollableChat.setWheelScrollingEnabled(true);

        scrollableQuery = new JScrollPane(queryPane);
        scrollableQuery.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollableQuery.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
        scrollableQuery.setWheelScrollingEnabled(true);
        scrollableQuery.setPreferredSize(new Dimension(1000, 160));

        timeoutPanel = new PanelTimeout(timeoutLabel, timeoutTextField);
        tooltipPanel = new PanelLeftSideBottom(tooltipCommands, dropdownGPTModels);
        buttonsPanel = new PanelButtons(timeoutPanel, currentChatNameField, saveButton, deleteSelectedButton, deleteAllButton, tooltipPanel);
        leftSidePanel = new PanelLeftSide(savedChatsLabel, savedChatsList, buttonsPanel);
        chatPanel = new PanelChat(chatPane);
        queryPanel = new PanelQuery(scrollableQuery, progressBar);
        rightSidePanel = new PanelRightSide(chatPanel, queryPanel);
        mainPanel = new PanelMain(leftSidePanel, rightSidePanel);

        mainFrame.getContentPane().add(mainPanel);
        mainFrame.setTitle("Jan-GPT");
        mainFrame.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        mainFrame.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        mainFrame.setLocation(dim.width / 2 - WIDTH / 2, dim.height / 2 - HEIGHT / 2);

        mainFrame.pack();
        mainFrame.setVisible(true);
        mainFrame = new MainFrame(this);
        queryPane.requestFocus();
    }

    public void rememberPreferredModel() {
        try {
            getPreferredModel().ifPresentOrElse(
                    manager::setGPTModel,
                    () -> manager.setGPTModel(GPTModel.getNewest().orElseThrow())
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getPreferredTimeout() {
        try {
            return Integer.parseInt(Files.readString(PREFERRED_TIMEOUT_FILE_PATH));
        } catch (Exception e) {
            return 30;
        }
    }

    public Optional<GPTModel> getPreferredModel() {
        try {
            return GPTModel.valueOf(Files.readString(PREFERRED_MODEL_FILE_PATH));
        } catch (Exception e) {
            return GPTModel.getNewest();
        }
    }

    public void rememberPreferredModel(GPTModel model) {
        try {
            Files.createDirectories(PREFERRED_MODEL_FILE_PATH.getParent());
            Files.deleteIfExists(PREFERRED_MODEL_FILE_PATH);
            Files.createFile(PREFERRED_MODEL_FILE_PATH);
            Files.writeString(PREFERRED_MODEL_FILE_PATH, model.modelName, StandardOpenOption.WRITE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void rememberPreferredTimeout(int sec) {
        try {
            Files.createDirectories(PREFERRED_TIMEOUT_FILE_PATH.getParent());
            Files.deleteIfExists(PREFERRED_TIMEOUT_FILE_PATH);
            Files.createFile(PREFERRED_TIMEOUT_FILE_PATH);
            Files.writeString(PREFERRED_TIMEOUT_FILE_PATH, String.valueOf(sec), StandardOpenOption.WRITE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTimeoutSec(int sec) {
        manager.setTimeoutSec(sec);
    }

    public void disableElements() {
        queryPane.disableListener();
        savedChatsList.disableListener();
        dropdownGPTModels.setEnabled(false);
        currentChatNameField.setEnabled(false);
        timeoutTextField.setEnabled(false);
        progressBar.setIndeterminate(true);
        saveButton.setEnabled(false);
        deleteSelectedButton.setEnabled(false);
        deleteAllButton.setEnabled(false);
    }

    public void enableElements() {
        queryPane.enableListener();
        savedChatsList.enableListener();
        dropdownGPTModels.setEnabled(true);
        currentChatNameField.setEnabled(true);
        timeoutTextField.setEnabled(true);
        progressBar.setIndeterminate(false);
        saveButton.setEnabled(true);
        deleteSelectedButton.setEnabled(true);
        deleteAllButton.setEnabled(true);
    }
}