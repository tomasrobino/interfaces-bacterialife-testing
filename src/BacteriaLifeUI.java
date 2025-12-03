import javax.swing.*;
import java.awt.*;

public class BacteriaLifeUI {
    // Constants
    private BacteriaLifeLogic LOGIC = null;
    private static final int BACTERIA_SIZE = 10;
    private static final Color BG_COLOR = new Color(141, 69, 220);
    private static final Color BASE_COLOR = new Color(255, 255, 255);
    private static final int DIMENSION = 30;

    private static int[][] bacteriaGen = BacteriaLifeLogic.generateInitialGen();

    // Circle class for rounded objects (bacteria)
    private static class Circle extends JButton {
        private Color color;
        private final int diameter;

        public Circle(Color color) {
            this.color = color;
            this.diameter = BACTERIA_SIZE;
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setOpaque(false);
            setEnabled(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            g.setColor(color);
            g.fillOval(0, 0, diameter, diameter); // use diameter instead of getWidth()/getHeight() if you want consistent circles
            super.paintComponent(g);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(diameter, diameter);
        }

        public void setCircleColor(Color c) {
            this.color = c;
            repaint();
        }

        public Color getColor() {
            return color;
        }
    }

    // Generate a generation
    private static JPanel generateGen() {
        JPanel gen = new JPanel();
        gen.setLayout(new GridLayout(DIMENSION, DIMENSION,3, 3));
        gen.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        gen.setBackground(BG_COLOR);
        for (int i = 0; i < DIMENSION; i++)  {
            for (int j = 0; j < DIMENSION; j++) {
                Color color = Color.WHITE;
                if (bacteriaGen[i][j] == 1) {
                    color = Color.BLACK;
                }
                Circle bacteria = new Circle(color);
                gen.add(bacteria);
            }
        }
        return gen;
    }

    private static JPanel bottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(BG_COLOR);

        JLabel roundLabel = new JLabel("Round: " + 4);

        JButton startButton = new JButton("Start");
        startButton.setPreferredSize(new Dimension(70, 50));
        startButton.setBackground(Color.WHITE);
        startButton.setContentAreaFilled(true);
        startButton.setBorderPainted(false);
        startButton.setFocusPainted(false);

        bottomPanel.add(roundLabel, BorderLayout.WEST);   // Left side
        bottomPanel.add(startButton, BorderLayout.EAST);  // Right side

        return bottomPanel;
    }

    // Main
    public BacteriaLifeUI(BacteriaLifeLogic logic) {
        this.LOGIC = logic;

        // Main frame
        JFrame mainFrame = new JFrame("BacteriaLife");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());

        // Add the gen
        mainFrame.add(generateGen(), BorderLayout.CENTER);

        // Add the bottom label
        mainFrame.add(bottomPanel(), BorderLayout.SOUTH);

        mainFrame.pack();
        mainFrame.setVisible(true);
    }
}
