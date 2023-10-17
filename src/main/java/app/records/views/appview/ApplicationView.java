package app.records.views.appview;

import app.Main;
import app.managers.frontend.ViewManager;
import app.records.GPTModel;
import app.records.views.View;
import org.jetbrains.annotations.NotNull;

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

    public ViewManager manager;
    public MainFrame mainFrame;
    public TextPaneQuery queryPane;
    public TextPaneChat chatPane;
    public JTextArea unformattedChat;
    public SenderAndReceiver senderAndReceiver;
    public JProgressBar progressBar;
    public SaveCurrentChatNameField currentChatNameField;
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
    public PanelTimeout timeoutPanel;
    public Tooltip tooltipCommands;
    public JLabel timeoutLabel;
    public Wrapper wrapper;
    public JTextField timeoutTextField;
    public int HEIGHT = 700, WIDTH = 1400;
    public int minHEIGHT = 500, minWIDTH = 1000;
    public Path PREFERRED_MODEL_FILE_PATH = Path.of(Main.BASE_DATA_PATH + "preferred_model.txt");


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

    void buildMainFrame(@NotNull ViewManager manager) {
        this.manager = manager;
        setPreferredModel();

        mainFrame = new MainFrame(this);
        chatPane = new TextPaneChat();
        unformattedChat = new JTextArea();
        chatPane.addMouseWheelListener(new ListenerMouseScrollChat(this));
        queryPane = new TextPaneQuery(this);
        queryPane.initHint();
        savedChatsLabel = new JLabel("Gespeicherte Chats:", SwingConstants.CENTER);
        senderAndReceiver = new SenderAndReceiver(this);
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(false);
        timeoutTextField = new JTextField(String.valueOf(60));
        timeoutTextField.addActionListener(new ListenerTimeoutText(this));
        timeoutTextField.setEditable(true);
        timeoutLabel = new JLabel("maximale Wartezeit: ");
        timeoutLabel.setToolTipText("Timeout nach " + 60 + " Sekunden");
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
        tooltipCommands = new Tooltip(" ♿");
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

        timeoutPanel = new PanelTimeout(timeoutLabel, timeoutTextField);
        tooltipPanel = new PanelLeftSideBottom(tooltipCommands, dropdownGPTModels);
        buttonsPanel = new PanelButtons(timeoutPanel, currentChatNameField, saveButton, deleteSelectedButton, deleteAllButton, tooltipPanel);
        leftSidePanel = new PanelLeftSide(savedChatsLabel, savedChatsList, buttonsPanel);
        chatPanel = new PanelChat(chatPane);
        queryPanel = new PanelQuery(scrollableQuery, progressBar);
        rightSidePanel = new PanelRightSide(chatPanel, queryPanel);
        mainPanel = new PanelMain(leftSidePanel, rightSidePanel);

        mainFrame.getContentPane().add(mainPanel);
        mainFrame.setTitle("Neuer Chat");
        mainFrame.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        mainFrame.setMinimumSize(new Dimension(minWIDTH, minHEIGHT));
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        mainFrame.setLocation(dim.width / 2 - WIDTH / 2, dim.height / 2 - HEIGHT / 2);

        mainFrame.pack();
        mainFrame.setVisible(true);
        mainFrame = new MainFrame(this);
        queryPane.requestFocus();
    }

    public void setPreferredModel() {
        try {
            getPreferredModel().ifPresentOrElse(
                    manager::setGPTModel, () -> manager.setGPTModel(GPTModel.getNewest().orElseThrow())
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Optional<GPTModel> getPreferredModel() {
        try {
            return GPTModel.valueOf(Files.readString(PREFERRED_MODEL_FILE_PATH));
        } catch (Exception e) {
            return GPTModel.getNewest();
        }
    }

    public void setPreferredModel(GPTModel model) {
        try {
            Files.createDirectories(PREFERRED_MODEL_FILE_PATH.getParent());
            Files.deleteIfExists(PREFERRED_MODEL_FILE_PATH);
            Files.createFile(PREFERRED_MODEL_FILE_PATH);
            Files.writeString(PREFERRED_MODEL_FILE_PATH, model.modelName, StandardOpenOption.WRITE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTimeoutSec(int sec) {
        manager.setTimeoutSec(sec);
    }
}