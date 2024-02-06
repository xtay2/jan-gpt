package app.managers.frontend.appview;

import app.records.GPTModel;

import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Optional;

/**
 * @author A.Mukhamedov
 */
public class AppPreferenceManager {
    private final ApplicationView app;

    public AppPreferenceManager(ApplicationView app) {
        this.app = app;
    }

    public void rememberPreferredModel() {
        try {
            getPreferredModel().ifPresentOrElse(
                    app.manager::setGPTModel,
                    () -> app.manager.setGPTModel(GPTModel.getNewest().orElseThrow())
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Optional<GPTModel> getPreferredModel() {
        try {
            return GPTModel.valueOf(Files.readString(app.PREFERRED_MODEL_FILE_PATH));
        } catch (Exception e) {
            return GPTModel.getNewest();
        }
    }

    /**
     * Returns default timeout of 120 sec if none set.
     */
    public int getPreferredTimeout() {
        try {
            return Integer.parseInt(Files.readString(app.PREFERRED_TIMEOUT_FILE_PATH));
        } catch (Exception e) {
            return 120;
        }
    }

    public void rememberPreferredModel(GPTModel model) {
        try {
            Files.createDirectories(app.PREFERRED_MODEL_FILE_PATH.getParent());
            Files.deleteIfExists(app.PREFERRED_MODEL_FILE_PATH);
            Files.createFile(app.PREFERRED_MODEL_FILE_PATH);
            Files.writeString(app.PREFERRED_MODEL_FILE_PATH, model.modelName, StandardOpenOption.WRITE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void rememberPreferredTimeout(int sec) {
        try {
            Files.createDirectories(app.PREFERRED_TIMEOUT_FILE_PATH.getParent());
            Files.deleteIfExists(app.PREFERRED_TIMEOUT_FILE_PATH);
            Files.createFile(app.PREFERRED_TIMEOUT_FILE_PATH);
            Files.writeString(app.PREFERRED_TIMEOUT_FILE_PATH, String.valueOf(sec), StandardOpenOption.WRITE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTimeoutSec(int sec) {
        app.manager.setTimeoutSec(sec);
    }

    public void disableElements() {
        app.queryPane.disableListener();
        app.savedChatsList.disableListener();
        app.dropdownGPTModels.setEnabled(false);
        app.currentChatNameField.setEnabled(false);
        app.timeoutTextField.setEnabled(false);
        app.progressBar.setIndeterminate(true);
        app.saveButton.setEnabled(false);
        app.deleteSelectedButton.setEnabled(false);
        app.deleteAllButton.setEnabled(false);
    }

    public void enableElements() {
        app.queryPane.enableListener();
        app.savedChatsList.enableListener();
        app.dropdownGPTModels.setEnabled(true);
        app.currentChatNameField.setEnabled(true);
        app.timeoutTextField.setEnabled(true);
        app.progressBar.setIndeterminate(false);
        app.saveButton.setEnabled(true);
        app.deleteSelectedButton.setEnabled(true);
        app.deleteAllButton.setEnabled(true);

    }
}
