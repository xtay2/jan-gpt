package app.records.views.appview;

import app.managers.backend.GPTPort;
import app.managers.frontend.ViewManager;
import app.records.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.swing.*;
import javax.swing.text.Document;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class SenderAndReceiverTest {

    private ApplicationView app;
    private SenderAndReceiver senderAndReceiver;

    @BeforeEach
    public void setUp() {
        app = mock(ApplicationView.class);
        app.queryPane = mock(TextPaneQuery.class);
        app.timeoutLabel = mock(JLabel.class);
        app.progressBar = mock(JProgressBar.class);
        app.chatPane = mock(TextPaneChat.class);
        app.chatPane = mock(TextPaneChat.class);
        Document document = mock(Document.class);
        when(app.chatPane.getDocument()).thenReturn(document);
        app.manager = mock(ViewManager.class);
        app.wrapper = mock(Wrapper.class);
        app.savedChatsList = mock(SavedChatsList.class);
        senderAndReceiver = new SenderAndReceiver(app);
    }


    @Test
    public void testSendMessage() throws GPTPort.MissingAPIKeyException, GPTPort.MissingModelException {
        // Arrange
        when(app.queryPane.getText()).thenReturn("query");
        when(app.manager.callGPT(anyString())).thenReturn(Optional.of("response"));

        // Act
        senderAndReceiver.sendMessage();

        // Assert
        verify(app.queryPane, times(1)).disableListener();
        verify(app.timeoutLabel, times(2)).setText(anyString());
        verify(app.progressBar, times(1)).setIndeterminate(true);
        verify(app.queryPane, times(1)).getText();
        verify(app.chatPane, times(1)).writeMsg(Role.USER, "query");
        verify(app.queryPane, times(1)).setText("");
        verify(app.manager, times(1)).callGPT("query");
        verify(app.progressBar, times(2)).setIndeterminate(false);
        verify(app.queryPane, times(1)).enableListener();
    }
}
