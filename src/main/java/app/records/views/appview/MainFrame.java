package app.records.views.appview;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * This class is responsible for the main window of the application.
 *
 * @author A.Mukhamedov
 */
public class MainFrame extends javax.swing.JFrame {
	private final ApplicationView app;
	public int HEIGHT = 500;
	public int WIDTH = 1000;
	
	public MainFrame (ApplicationView app) {
		super(app.manager.getGPTModel().map(m -> m.modelName).orElse("Kein Modell geladen"));
		this.app = app;
		setBounds(100, 100, WIDTH, HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		addWindowListener(new ListenerWindowClosing(app));
		setResizable(true);
	}
	
	
	private class ListenerWindowClosing implements WindowListener {
		public ListenerWindowClosing (ApplicationView app) {}
		
		@Override
		public void windowOpened (WindowEvent e) {
		
		}
		
		@Override
		public void windowClosing (WindowEvent e) {
			app.savedChatsList.permaDeleteFlaggedChats();
			
		}
		
		@Override
		public void windowClosed (WindowEvent e) {
		}
		
		@Override
		public void windowIconified (WindowEvent e) {
		
		}
		
		@Override
		public void windowDeiconified (WindowEvent e) {
		
		}
		
		@Override
		public void windowActivated (WindowEvent e) {
		
		}
		
		@Override
		public void windowDeactivated (WindowEvent e) {
		
		}
	}
}