package main.java.app.views.appview;

import main.java.app.managers.frontend.ViewManager;
import main.java.app.records.GPTModel;
import main.java.app.views.View;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;

import javax.swing.*;
import java.awt.*;
import java.util.stream.Collectors;
import javax.swing.JProgressBar;

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
    public JScrollPane scrollableChatArea;
    public JScrollPane scrollableQueryArea;
    public SavedChats dropdownSavedChats;
    public GPTModels dropdownGPTModels;
    public JLabel enterToSend;
    public MainPanel mainPanel;
    public QueryPanel queryPanel;
    public ChatPanel chatPanel;
    public OldChatsPanel oldChatsPanel;
    public FontChangeListener fontChangeListener;
    public int HEIGHT = 500;
    public int WIDTH = 1000;


    /**
     * @param manager The component that excepts data.
     */
    @Override
    public void start(ViewManager manager) {
        if (manager.hasAPIKey())
            createMainFrame(manager);
        else  // manager.setAPIKey("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");
            new main.java.app.views.appview.APIKeyFrame(manager, () -> createMainFrame(manager));

    }


    private void createMainFrame(ViewManager manager) {

        this.manager = manager;
        manager.setGPTModel(GPTModel.getNewest().orElseThrow());

        mainFrame = new MainFrame(this);
        queryArea = new QueryArea(this);
        enterToSend = new JLabel("Enter zum Absenden");
        // Attach the FontChangeListener to the chatArea
        chatArea = new ChatArea();
        fontChangeListener = new FontChangeListener(chatArea);
        chatArea.addMouseWheelListener(fontChangeListener);
        //chatArea.addMouseListener(new ChatAreaClickedFocusQueryArea(this));
        //chatArea.getDocument().addDocumentListener(new ResizeChatAreaToFitText(this));
        sender = new Sender(this);
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(false);
        currentChat = new ChatName();
        saveButton = new JButton("Konversation speichern");
        saveButton.addActionListener(e -> {
            String name = currentChat.getText();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(mainFrame, "Bitte gib links vom Button einen Namen ein.\n");
                return;
            }
            if(manager.saveConversationAs(name)) {
                chatArea.append("Konversation wurde gespeichert \n_______ \n");
                dropdownSavedChats.addItem(name);
                dropdownSavedChats.setSelectedItem(name);
            } else {
                JOptionPane.showMessageDialog(mainFrame, "Bitte führe erst eine Konversation.\n");
            }
        });

        dropdownSavedChats = new SavedChats(this);
        dropdownSavedChats.addActionListener(e -> {
            String convName = (String) dropdownSavedChats.getSelectedItem();
            if(SavedChats.NEW_CONV.equals(convName)) {
                manager.newConversation();
                chatArea.setText("Jan-GPT: \nHallo! Was kann ich für dich tun? \n_______ \n");
            }
            else {
                var conv = manager.loadConversation(convName);
                chatArea.setText(conv.map(messages ->
                        messages.stream().map(msg -> msg.role().alias(false) + ": "
                                + msg.content() + "\n").collect(Collectors.joining())
                ).orElse("Konversation konnte nicht geladen werden \n"));
            }
            // Set focus on the query text field
            SwingUtilities.invokeLater(() -> queryArea.requestFocusInWindow());
        });

        dropdownGPTModels = new GPTModels(this);
        dropdownGPTModels.addActionListener(e -> {
            String modelName = (String) dropdownGPTModels.getSelectedItem();
            var model = GPTModel.valueOf(modelName);
            if (model.isEmpty()) return;
            mainFrame.setTitle(modelName);
            manager.setGPTModel(model.get());
            // Set focus on the query text field
            SwingUtilities.invokeLater(() -> queryArea.requestFocusInWindow());
        });

        scrollableChatArea = new JScrollPane(chatArea);
        scrollableChatArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollableChatArea.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);

        scrollableQueryArea = new JScrollPane(queryArea);
        scrollableQueryArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollableQueryArea.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);

        oldChatsPanel = new OldChatsPanel(currentChat, saveButton, dropdownSavedChats);
        chatPanel = new ChatPanel(chatArea, oldChatsPanel);
        queryPanel = new QueryPanel(queryArea, enterToSend, dropdownGPTModels, progressBar);
        mainPanel = new MainPanel(queryPanel, chatPanel);


        mainFrame.getContentPane().add(mainPanel);
        mainFrame.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        mainFrame.pack();
        mainFrame.setVisible(true);

        // Set focus on the query text field
        SwingUtilities.invokeLater(() -> queryArea.requestFocusInWindow());
    }
}