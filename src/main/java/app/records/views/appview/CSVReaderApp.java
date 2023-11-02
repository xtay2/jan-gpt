package app.records.views.appview;

import app.managers.backend.GPTPort;
import app.records.Role;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Path;
import java.time.Duration;
import java.util.*;
import java.util.Timer;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

public class CSVReaderApp {
    private static final List<String> responses = new ArrayList<>();
    private static final Path DOCS_PATH = Path.of("data" + File.separator + "docs" + File.separator + "id_name.csv");
    private static final Path GRPS_PATH = Path.of("data" + File.separator + "docs" + File.separator + "grps.csv");
    private static final String instruction = """
            erstelle aus den übergebenen dokumentnamen eine liste an gruppen.
            die dokumentnamen sollen semantisch in diese gruppen passen.
            antworte mit einer html liste.
            <head></head>, <title></title> und <h1></h1>.
            antworte nicht mit einer textuellen erklärung.
            antworte nicht mit python code.
            """;
    private final ApplicationView app;
    public JButton GRPButton;

    Timer timer;
    AtomicInteger timeSpent = new AtomicInteger();

    TimerTask task = new TimerTask() {
        public void run() {
            GRPButton.setText("Time: " + timeSpent.incrementAndGet() + "s");
        }
    };


    public CSVReaderApp(ApplicationView app) {
        this.app = app;
        timer = new Timer();

        GRPButton = new JButton("Start GRP");
        GRPButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer.scheduleAtFixedRate(task, 0, 1000); // Schedule the task to run every second

                // Create and execute a SwingWorker to run startCSVReader in the background
                SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        startCSVReader();
                        return null;
                    }
                };
                worker.execute();
            }
        });
    }

    @SuppressWarnings("BusyWait")
    public void startCSVReader() {
        String csvFile = DOCS_PATH.toString();
        String line;
        String cvsSplitBy = ","; // Trennzeichen in der CSV-Datei
        responses.clear(); // Leere die Liste der vorherigen Antworten

        // Create a map to group document names by ID
        Map<String, List<String>> idToDocumentNames = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile));
             FileWriter fw = new FileWriter(GRPS_PATH.toFile());
             BufferedWriter bw = new BufferedWriter(fw)) {

            while ((line = br.readLine()) != null) {
                String[] data = line.split(cvsSplitBy);
                if (data.length >= 2) {
                    String id = data[0].trim();
                    String documentName = data[1].trim();

                    // Add the document name to the corresponding ID group
                    idToDocumentNames.computeIfAbsent(id, k -> new ArrayList<>()).add(documentName);
                }
            }

            // Iterate through the ID groups and send them to GPT
            for (List<String> documentNames : idToDocumentNames.values()) {
                String prompt = buildPrompt(documentNames);

                timer.scheduleAtFixedRate(task, 0, 1000); // Schedule the task to run every second
                String response = String.valueOf(app.manager.callGPT(prompt)); // Aufruf deiner callGPT-Funktion
                timer.cancel(); // Stop the timer once callGPT is done

                app.chatPane.writeMsg(response, Role.ASSISTANT);
                responses.add(response);

                // Schreibe die Response in die GRPS_PATH CSV-Datei
                bw.write(response);
                bw.newLine();

                try {
                    Thread.sleep(Duration.ofSeconds(10));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException | GPTPort.MissingAPIKeyException | GPTPort.MissingModelException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    private String buildPrompt(List<String> documentNames) {
        StringBuilder promptBuilder = new StringBuilder(instruction);
        if (!responses.isEmpty()) {
            promptBuilder
                    .append("Gruppen:\n")
                    .append(responses.get(responses.size() - 1))
                    .append("\n");
        }
        promptBuilder
                .append("Dokumentennamen:\n")
                .append(String.join("\n", documentNames));

        System.out.println("Folgender Prompt wurde erstellt: " + promptBuilder);
        return promptBuilder.toString();
    }

}
