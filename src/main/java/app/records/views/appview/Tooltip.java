package app.records.views.appview;

import javax.swing.*;
import java.awt.*;

/**
 * @author A.Mukhamedov
 */
public class Tooltip extends JLabel {

    public Tooltip(String str) {
        super(str);
        Font customFont = new Font("Segoe UI Symbol", Font.PLAIN, 18); // a font that supports the "ⓘ" symbol
        setFont(customFont);
        setVisible(true);
        setToolTipText("<html>" +
                "↵ : Frage absenden <br/>" +
                "⇧ + ↵ : Zeilenumbruch <br/>" +
                "Strg + ⇕ : Schriftgröße ändern <br/>" +
                "Strg + t : neuen Chat öffnen <br/>" +
                "Strg + s : Frage im Eingabefeld merken <br/>" +
                "Strg + d : Frage im Eingabefeld vergessen <br/>" +
                "␛ : Eingabefeld leeren <br/>" +
                "↑ : letzte gemerkte Frage einfügen <br/>" +
                "↓ : nächste gemerkte Frage einfügen <br/>" +
                "Entf : ausgewählte gespeicherte Chats löschen <br/>" +
                "Strg + z : gelöschte Chats wiederherstellen <br/>" +
                "</html>");
        // Set the initial delay for the tooltip in milliseconds (default is 500 ms)
        ToolTipManager.sharedInstance().setInitialDelay(100);
        // Set the dismiss delay for the tooltip in milliseconds (default is 4,000 ms)
        ToolTipManager.sharedInstance().setDismissDelay(10000);
    }

}
