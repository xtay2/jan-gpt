package main.java.app.views;

import main.java.app.managers.backend.GPTPort;
import main.java.app.managers.frontend.ViewManager;
import main.java.app.records.GPTModel;
import main.java.app.records.Role;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import java.util.stream.Collectors;
import javax.swing.JProgressBar;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * @author Dennis Woithe
 */
public class ApplicationView implements View {

    private ViewManager manager;
    private JFrame frame;
    private JTextArea queryTextArea;
    private JTextArea conversationTextArea;
    private JProgressBar progressBar;
    private JButton sendButton;
    private JTextField saveTextField;



    /**
     * @param manager The component that excepts data.
     */
    @Override
    public void start(ViewManager manager) {
        if (manager.hasAPIKey())
            createMainFrame(manager);
        else
            new APIKeyFrame(manager, () -> createMainFrame(manager)); //  manager.setAPIKey("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");
    }

    private void createMainFrame(ViewManager manager) {

        this.manager = manager;
        manager.setGPTModel(GPTModel.getNewest().orElseThrow());

        frame = new JFrame(manager.getGPTModel().map(m -> m.modelName).orElse("Kein Modell geladen"));
        int HEIGHT = 500;
        int WIDTH = 1000;
        frame.setBounds(100, 100, WIDTH, HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout()); // Use BorderLayout for the frame's content pane
        frame.setResizable(true); // Set JFrame to resizable

        // Conversation history
        conversationTextArea = new JTextArea();
        conversationTextArea.setEditable(true);
        conversationTextArea.setLineWrap(true);
        conversationTextArea.setWrapStyleWord(true);
        conversationTextArea.setPreferredSize(new Dimension(WIDTH, HEIGHT)); // Set initial preferred size
        conversationTextArea.setText("Jan-GPT: \nHallo! Was kann ich für dich tun? \n_______ \n");


        conversationTextArea.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Set focus on the query text field
                SwingUtilities.invokeLater(() -> queryTextArea.requestFocusInWindow());

            }

            @Override
            public void mousePressed(MouseEvent e) {
                // Set focus on the query text field
                SwingUtilities.invokeLater(() -> queryTextArea.requestFocusInWindow());

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // Set focus on the query text field
                SwingUtilities.invokeLater(() -> queryTextArea.requestFocusInWindow());

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // Set focus on the query text field
                SwingUtilities.invokeLater(() -> queryTextArea.requestFocusInWindow());

            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Set focus on the query text field
                SwingUtilities.invokeLater(() -> queryTextArea.requestFocusInWindow());

            }
        });


        // DocumentListener to dynamically adjust preferred size
        conversationTextArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                adjustWindowSize();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                adjustWindowSize();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                adjustWindowSize();
            }

            private void adjustWindowSize() {
                conversationTextArea.setPreferredSize(conversationTextArea.getPreferredSize());
                frame.pack();
            }
        });
        JScrollPane scrollPane = new JScrollPane(conversationTextArea);


        // Query text area
        queryTextArea = new JTextArea();
        queryTextArea.setLineWrap(true);
        queryTextArea.setWrapStyleWord(true);

        // progress bar
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(false);

        // Send button
        sendButton = new JButton("Send");

        // Save button
        JButton saveButton = new JButton("Save conversation");

        // Save text field
        saveTextField = new JTextField();


        // Dropdown menu for model selection
        String[] models = GPTModel.values().toArray(String[]::new);
        JComboBox<String> modelsCombo = new JComboBox<>(models);
        manager.getGPTModel().ifPresent(model -> modelsCombo.setSelectedItem(model.modelName));
        modelsCombo.addActionListener(e -> {
            String modelName = (String) modelsCombo.getSelectedItem();
            var model = GPTModel.valueOf(modelName);
            if (model.isEmpty()) return;
            frame.setTitle(modelName);
            manager.setGPTModel(model.get());
            // Set focus on the query text field
            SwingUtilities.invokeLater(() -> queryTextArea.requestFocusInWindow());
        });

        // Dropdown menu for conversation selection
        var convs = new Vector<>(manager.getConversations().orElse(Collections.emptyList()));
        final var NEW_CONV = "Neue Konversation";
        convs.add(0, NEW_CONV);
        JComboBox<String> convsCombo = new JComboBox<>(convs);
        convsCombo.addActionListener(e -> {
            String convName = (String) convsCombo.getSelectedItem();
            if(NEW_CONV.equals(convName)) {
                manager.newConversation();
                conversationTextArea.setText("Jan-GPT: \nHallo! Was kann ich für dich tun? \n_______ \n");
            }
            else {

                var conv = manager.loadConversation(convName);
                conversationTextArea.setText(conv.map(messages ->
                        messages.stream().map(msg -> msg.role().alias(false) + ": "
                                + msg.content() + "\n").collect(Collectors.joining())
                ).orElse("Konversation konnte nicht geladen werden \n"));
            }
            // Set focus on the query text field
            SwingUtilities.invokeLater(() -> queryTextArea.requestFocusInWindow());
        });

        // Set preferred size for the dropdown menu
        Dimension dropdownSize = new Dimension(150, 30);
        convsCombo.setPreferredSize(dropdownSize);
        convsCombo.setMaximumSize(dropdownSize);
        convsCombo.setMinimumSize(dropdownSize);

        // Panel for holding the dropdown menu
        JPanel convsPanel = new JPanel();
        convsPanel.add(convsCombo);


        // Slider for changing font size
        JSlider fontSizeSlider = new JSlider(JSlider.HORIZONTAL, 10, 30, 14);
        fontSizeSlider.setMajorTickSpacing(5);
        fontSizeSlider.setMinorTickSpacing(1);
        fontSizeSlider.setPaintTicks(true);
        fontSizeSlider.setPaintLabels(true);

        // Set preferred size for the slider
        Dimension sliderSize = new Dimension(130, 50);
        fontSizeSlider.setPreferredSize(sliderSize);
        fontSizeSlider.setMaximumSize(sliderSize);
        fontSizeSlider.setMinimumSize(sliderSize);


        fontSizeSlider.addChangeListener(e -> {
            int fontSize = fontSizeSlider.getValue();
            Font newFont = new Font(Font.SANS_SERIF, Font.PLAIN, fontSize);
            conversationTextArea.setFont(newFont);
            queryTextArea.setFont(newFont);
        });

        // Label for the font size slider
        JLabel fontSizeLabel = new JLabel("Font Size");
        fontSizeLabel.setHorizontalAlignment(SwingConstants.CENTER);


        saveTextField.setPreferredSize(new Dimension(80, 30));

        JPanel savePanel = new JPanel();
        savePanel.setLayout(new BorderLayout());
        savePanel.add(saveTextField, BorderLayout.CENTER);
        savePanel.add(saveButton, BorderLayout.EAST);

        // Panel for holding the font size slider
        JPanel sliderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sliderPanel.add(fontSizeLabel);
        sliderPanel.add(fontSizeSlider);
        sliderPanel.add(convsPanel);
        sliderPanel.add(savePanel, BorderLayout.WEST);



        // Panel for holding the query panel and dropdown menu
        JPanel queryPanel = new JPanel();
        queryPanel.setLayout(new BorderLayout());
        queryPanel.add(queryTextArea, BorderLayout.CENTER);
        queryPanel.add(sendButton, BorderLayout.EAST);




        // Panel for holding the dropdown menu
        JPanel dropdownPanel = new JPanel();
        dropdownPanel.add(modelsCombo);

        // Panel for holding the query panel and dropdown panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.add(queryPanel, BorderLayout.CENTER);
        inputPanel.add(dropdownPanel, BorderLayout.EAST);
        inputPanel.add(progressBar, BorderLayout.SOUTH);
        inputPanel.add(sliderPanel, BorderLayout.NORTH);

        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.getContentPane().add(inputPanel, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);

        saveButton.addActionListener(e -> {
            String convName = saveTextField.getText();
            if (convName.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Bitte gib einen Namen für die Konversation ein.\n");
                return;
            }
            if(manager.saveConversationAs(convName)) {
                conversationTextArea.append("Konversation wurde gespeichert \n_______ \n");
                convsCombo.addItem(convName);
                convsCombo.setSelectedItem(convName);
            } else {
                JOptionPane.showMessageDialog(frame, "Bitte führe erst eine Konversation.\n");
            }
        });

        // Set focus on the query text field
        SwingUtilities.invokeLater(() -> queryTextArea.requestFocusInWindow());

        // Send message on button click
        sendButton.addActionListener(e -> {
            // Execute the sending of the message in a background thread
            SwingUtilities.invokeLater(this::sendMessage);
        });

        // Send message on Enter key press
        queryTextArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    e.consume(); // Prevents a new line from being added

                    // Execute the sending of the message in a background thread
                    SwingUtilities.invokeLater(ApplicationView.this::sendMessage);
                }
            }
        });
        frame.setMinimumSize(new Dimension(WIDTH, HEIGHT)); // Set the minimum size of the frame
        frame.pack(); // Automatically sizes the frame based on the components' preferred sizes
        frame.setVisible(true);
    }



    private void sendMessage() {
        // Disable UI components to prevent multiple requests
        queryTextArea.setEnabled(false);
        sendButton.setEnabled(false);
        progressBar.setIndeterminate(true);

        String query = queryTextArea.getText();

        // Create a background thread for sending the request
        Thread thread = new Thread(() -> {
            if (!query.isEmpty()) {
                conversationTextArea.append(Role.USER.alias(false) + ": \n ");
                conversationTextArea.append(query + "\n");
                queryTextArea.setText("");

                try {
                    var response = manager.callGPT(query);
                    if (response.isEmpty()) {
                        conversationTextArea.append("ERROR: model could not be reached\n");
                        return;
                    }
                    response.ifPresent(s -> {
                        // Add line breaks to the response if it exceeds the conversation text area width
                        int textAreaWidth = conversationTextArea.getWidth();
                        FontMetrics fontMetrics = conversationTextArea.getFontMetrics(conversationTextArea.getFont());
                        String wrappedResponse = wrapText(s, textAreaWidth, fontMetrics);

                        // Update the UI on the EDT
                        SwingUtilities.invokeLater(() -> {
                            progressBar.setIndeterminate(false);
                            sendButton.setEnabled(true);
                            conversationTextArea.append("_______ \nJan-GPT: \n ");
                            conversationTextArea.append(wrappedResponse + "\n_______ \n");
                            conversationTextArea.setCaretPosition(conversationTextArea.getDocument().getLength());
                            // Set focus on the query text field
                            SwingUtilities.invokeLater(() -> queryTextArea.requestFocusInWindow());
                        });
                    });
                    System.out.println(response);

                } catch (GPTPort.MissingAPIKeyException ex) {
                    System.err.println("API key is missing.");
                    throw new RuntimeException(ex);
                } catch (GPTPort.MissingModelException ex) {
                    System.err.println("Model is missing.");
                    throw new RuntimeException(ex);
                }
            }

            // Enable UI components after the request is completed
            SwingUtilities.invokeLater(() -> {
                queryTextArea.setEnabled(true);
                sendButton.setEnabled(true);
            });
        });

        // Start the background thread
        thread.start();
    }

    private String wrapText(String text, int width, FontMetrics fontMetrics) {
        StringBuilder wrappedText = new StringBuilder();
        int lineWidth = 0;

        for (String word : text.split(" ")) {
            int wordWidth = fontMetrics.stringWidth(word + " ");
            if (lineWidth + wordWidth > width) {
                wrappedText.append("\n");
                lineWidth = 0;
            }
            wrappedText.append(word).append(" ");
            lineWidth += wordWidth;
        }

        return wrappedText.toString();
    }


}