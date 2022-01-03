import java.awt.*;
import javax.swing.*;

public class Grid extends JFrame {

    public Grid(Labyrinth lab) {
        setSize(960, 640);
        setVisible(true);
    }

    public void paint(Graphics g) {
        for (int x = 50; x <= 300; x += 30) {
            for (int y = 50; y <= 300; y += 30) {
                g.setColor(Color.RED);
                g.fillRect(x, y, 29, 29);
            }
        }
    }
}