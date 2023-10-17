package app.records.views.appview;

import java.awt.event.KeyEvent;

/**
 * @author A.Mukhamedov
 */
public class ListenerKeyPressedChats extends java.awt.event.KeyAdapter {
	private final ApplicationView app;
	
	public ListenerKeyPressedChats (ApplicationView app) {
		this.app = app;
	}
	
	
	@Override
	public void keyPressed (KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_DELETE) {
			app.savedChatsList.flagChatsAsDeleted();
		}
		// if key pressed is z and ctrl held then undo delete
		if (e.getKeyCode() == KeyEvent.VK_Z && e.isControlDown()) {
			app.savedChatsList.undoDelete();
		}
	}
	
}

