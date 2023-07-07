package main.java.app.views.appview;

import main.java.app.managers.frontend.ViewManager;
import main.java.app.records.GPTModel;
import main.java.app.views.View;

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
    public ChatName chatName;
    public JButton saveButton;
    public JScrollPane scrollPane;
    public SavedChats dropdownSavedChats;
    public GPTModels dropdownGPTModels;
    public JLabel enterToSend;

    public

    int HEIGHT = 500;
    int WIDTH = 1000;


    /**
     * @param manager The component that excepts data.
     */
    @Override
    public void start(ViewManager manager) {
        if (manager.hasAPIKey())
            createMainFrame(manager);
        else //  manager.setAPIKey("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");
            new APIKeyFrame(manager, () -> createMainFrame(manager));

    }


    private void createMainFrame(ViewManager manager) {

        this.manager = manager;
        manager.setGPTModel(GPTModel.getNewest().orElseThrow());

        mainFrame = new MainFrame(this);
        queryArea = new QueryArea();
        queryArea.addKeyListener(new EnterKeyPressedToSend(this));
        enterToSend = new JLabel("Schreibe deine Frage hier und drücke Enter zum Absenden!");
        chatArea = new ChatArea();
        chatArea.addMouseListener(new ChatAreaClickedFocusQueryArea(this));
        chatArea.getDocument().addDocumentListener(new ResizeChatAreaToFitText(this));
        sender = new Sender(this);
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(false);
        chatName = new ChatName();
        saveButton = new JButton("Konversation speichern");
        saveButton.addActionListener(e -> {
            String convName = chatName.getText();
            if (convName.isEmpty()) {
                JOptionPane.showMessageDialog(mainFrame, "Bitte gib einen Namen für die Konversation ein.\n");
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
        scrollPane = new JScrollPane(chatArea);
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






//
//        JPanel savePanel = new JPanel(new BorderLayout());
//        savePanel.add(chatName, BorderLayout.CENTER);
//        savePanel.add(saveButton, BorderLayout.EAST);
//
//        // Panel for holding the font size slider
//        JPanel midPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        midPanel.add(dropdownSavedChats);
//        midPanel.add(savePanel, BorderLayout.WEST);
//
//        // Panel for holding the query panel and dropdown menu
//        JPanel bottomPanel = new JPanel(new BorderLayout());
//        bottomPanel.add(queryArea, BorderLayout.CENTER);
//        bottomPanel.add(enterToSend, BorderLayout.NORTH);
//
//        // Panel for holding the query panel and dropdown panel
//        JPanel mainPanel = new JPanel(new BorderLayout());
//        mainPanel.add(bottomPanel, BorderLayout.CENTER);
//        mainPanel.add(dropdownGPTModels, BorderLayout.EAST);
//        mainPanel.add(progressBar, BorderLayout.SOUTH);
//        mainPanel.add(midPanel, BorderLayout.NORTH);


        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel midPanel = new JPanel(new FlowLayout());
        JPanel bottomPanel = new JPanel(new BorderLayout());

        topPanel.add(chatArea, BorderLayout.NORTH);

        midPanel.add(dropdownSavedChats);
        midPanel.add(chatName);
        midPanel.add(saveButton);
        midPanel.add(dropdownGPTModels);

        bottomPanel.add(queryArea, BorderLayout.SOUTH);
        bottomPanel.add(progressBar, BorderLayout.NORTH);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(midPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        mainFrame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        mainFrame.getContentPane().add(mainPanel, BorderLayout.SOUTH);
        mainFrame.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        mainFrame.pack();
        mainFrame.setVisible(true);



        // Set focus on the query text field
        SwingUtilities.invokeLater(() -> queryArea.requestFocusInWindow());



    }








}