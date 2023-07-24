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
    public ChatName newChatNameField;
    public JButton saveButton;
    public JScrollPane scrollableChatArea;
    public JScrollPane scrollableQueryArea;
    public SavedChats dropdownSavedChats;
    public GPTModels dropdownGPTModels;
    public JLabel enterToSend;
    public JPanel mainPanel;
    public JPanel queryPanel;
    public JPanel chatPanel;
    public JPanel oldChatsPanel;
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
        newChatNameField = new ChatName();
        saveButton = new JButton("Konversation speichern");
        saveButton.addActionListener(e -> {
            String convName = newChatNameField.getText();
            if (convName.isEmpty()) {
                JOptionPane.showMessageDialog(mainFrame, "Bitte gib links vom Button einen Namen ein.\n");
                return;
            }
            if(manager.saveConversationAs(convName)) {
                chatArea.append("Konversation wurde gespeichert \n_______ \n");
                dropdownSavedChats.addItem(convName);
                dropdownSavedChats.setSelectedItem(convName);
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

        // Panel for holding the new chat name text field, the save button and the saved chats dropdown menu
        oldChatsPanel = new JPanel(new BorderLayout());
        oldChatsPanel.add(newChatNameField, BorderLayout.CENTER);
        oldChatsPanel.add(saveButton, BorderLayout.EAST);
        oldChatsPanel.add(dropdownSavedChats, BorderLayout.WEST);

        // Panel for holding the scrollable chat area and the old chats panel
        chatPanel = new JPanel(new BorderLayout());
        chatPanel.add(scrollableChatArea, BorderLayout.CENTER);
        chatPanel.add(oldChatsPanel, BorderLayout.SOUTH);


        // Panel for holding the query area, gpt models dropdown menu, enter to send message and the progress bar
        queryPanel = new JPanel(new BorderLayout());
        queryPanel.add(scrollableQueryArea, BorderLayout.NORTH);
        queryPanel.add(enterToSend, BorderLayout.WEST);
        queryPanel.add(dropdownGPTModels, BorderLayout.EAST);
        queryPanel.add(progressBar, BorderLayout.CENTER);

        // Panel for holding the query panel and the chat panel
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(queryPanel, BorderLayout.SOUTH);
        mainPanel.add(chatPanel, BorderLayout.CENTER);

        mainFrame.getContentPane().add(mainPanel);
        mainFrame.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        mainFrame.pack();
        mainFrame.setVisible(true);

        // Set focus on the query text field
        SwingUtilities.invokeLater(() -> queryArea.requestFocusInWindow());
    }
}