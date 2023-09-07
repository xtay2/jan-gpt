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
                "↑ : letzte Frage <br/>" +
                "↓ : nächste Frage <br/>" +
                "↵ : Frage absenden <br/>" +
                "⇧ + ↵ : neue Zeile <br/>" +
                "Strg + s : Frage speichern <br/>" +
                "Strg + d : Frage löschen <br/>" +
                "Strg + ⇕ : Schriftgröße ändern <br/>" +
                "␛ : Feld leeren <br/>" +
                "</html>");
        // Set the initial delay for the tooltip in milliseconds (default is 500 ms)
        ToolTipManager.sharedInstance().setInitialDelay(100);
        // Set the dismiss delay for the tooltip in milliseconds (default is 4,000 ms)
        ToolTipManager.sharedInstance().setDismissDelay(10000);
    }

}
