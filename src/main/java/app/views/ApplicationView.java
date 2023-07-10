package app.views;

import app.managers.backend.GPTPort;
import app.managers.frontend.ViewManager;
import app.records.GPTModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * @author Dennis Woithe
 */
public class ApplicationView implements View {

    private ViewManager manager;
    private JFrame frame;
    private JSlider fontSize;
    private JTextArea queryTextArea;
    private JTextArea answerTextArea;
    private JTextArea conversationTextArea;
    private JProgressBar progressBar;
    private JButton sendButton;

    private final int WIDTH = 1000;
    private final int HEIGHT = 600;

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

        // Dropdown menu
        String[] models = GPTModel.values().toArray(String[]::new);
        JComboBox<String> dropdownMenu = new JComboBox<>(models);
        manager.getGPTModel().ifPresent(model -> dropdownMenu.setSelectedItem(model.modelName));
        dropdownMenu.addActionListener(e -> {
            String modelName = (String) dropdownMenu.getSelectedItem();
            var model = GPTModel.valueOf(modelName);
            if (model.isEmpty()) return;
            manager.setGPTModel(model.get());
            frame.setTitle(modelName);
        });
// Slider for changing font size
        JSlider fontSizeSlider = new JSlider(JSlider.HORIZONTAL, 10, 30, 14);
        fontSizeSlider.setMajorTickSpacing(5);
        fontSizeSlider.setMinorTickSpacing(1);
        fontSizeSlider.setPaintTicks(true);
        fontSizeSlider.setPaintLabels(true);
        fontSizeSlider.addChangeListener(e -> {
            int fontSize = fontSizeSlider.getValue();
            Font newFont = new Font(Font.SANS_SERIF, Font.PLAIN, fontSize);
            conversationTextArea.setFont(newFont);
            queryTextArea.setFont(newFont);
        });


        // Label for the font size slider
        JLabel fontSizeLabel = new JLabel("Font Size");
        fontSizeLabel.setHorizontalAlignment(SwingConstants.CENTER);

// Panel for holding the font size slider
        JPanel sliderPanel = new JPanel();
        sliderPanel.setLayout(new BorderLayout());
        sliderPanel.add(fontSizeLabel, BorderLayout.NORTH);
        sliderPanel.add(fontSizeSlider, BorderLayout.WEST);

// Panel for holding the query panel and dropdown menu
        JPanel queryPanel = new JPanel();
        queryPanel.setLayout(new BorderLayout());
        queryPanel.add(queryTextArea, BorderLayout.CENTER);
        queryPanel.add(sendButton, BorderLayout.EAST);

// Panel for holding the dropdown menu
        JPanel dropdownPanel = new JPanel();
        dropdownPanel.add(dropdownMenu);

// Panel for holding the query panel and dropdown panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.add(queryPanel, BorderLayout.CENTER);
        inputPanel.add(dropdownPanel, BorderLayout.EAST);
        inputPanel.add(progressBar, BorderLayout.SOUTH);
        inputPanel.add(sliderPanel, BorderLayout.NORTH);

        // Create a GridBagConstraints instance
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(10, 10, 10, 10);

        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.getContentPane().add(inputPanel, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);

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


        // DocumentListener to dynamically adjust preferred size
//        conversationTextArea.getDocument().addDocumentListener(new DocumentListener() {
//            @Override
//            public void insertUpdate(DocumentEvent e) {
//                adjustWindowSize();
//            }
//
//            @Override
//            public void removeUpdate(DocumentEvent e) {
//                adjustWindowSize();
//            }
//
//            @Override
//            public void changedUpdate(DocumentEvent e) {
//                adjustWindowSize();
//            }
//
//            private void adjustWindowSize() {
//                conversationTextArea.setPreferredSize(conversationTextArea.getPreferredSize());
//                frame.pack();
//            }
//        });
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
                conversationTextArea.append("You: \n ");
                conversationTextArea.append(query + "\n");
                queryTextArea.setText("");

                try {
                    var response = manager.callGPT(query);
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