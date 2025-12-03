import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {

        // Create logic
        BacteriaLifeLogic logic = new BacteriaLifeLogic();

        // Create UI
        SwingUtilities.invokeLater(() -> {
            new BacteriaLifeUI(logic);
        });
    }
}