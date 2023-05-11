import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ColoringApp extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    private BufferedImage image;
    private JPanel colorPanel;
    private JButton[] colorButtons;
    private int colorCode = Color.BLACK.getRGB();

    public void actionPerformed(ActionEvent e) {
        // check which button was clicked
        for (int i = 0; i < colorButtons.length; i++) {
            if (e.getSource() == colorButtons[i]) {
                colorCode = colorButtons[i].getBackground().getRGB();
                break;
            }
        }
    }
    public ColoringApp() {
        super("Coloring App");
        setSize(600, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        loadImage();

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel imagePanel = new JPanel() {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (image != null) {
                    g.drawImage(image, 0, 0, null);
                }
            }
        };
        imagePanel.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
        mainPanel.add(imagePanel, BorderLayout.CENTER);

        colorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        mainPanel.add(colorPanel, BorderLayout.SOUTH);

        addColorButton(Color.RED);
        addColorButton(Color.YELLOW);
        addColorButton(Color.BLUE);
        addColorButton(Color.GREEN);
        addColorButton(Color.PINK);


        setContentPane(mainPanel);
        setVisible(true);

        // Add mouse listener to image panel
        imagePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	if (e.getButton() == MouseEvent.BUTTON3) {
                    int x = e.getX();
                    int y = e.getY();
                    fill(x, y);
            	}
            }
        });
    }

    private void addColorButton(Color color) {
        if (colorButtons == null) {
            colorButtons = new JButton[5];
        }
        if (colorPanel.getComponentCount() == 5) {
            return;
        }
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(50, 50));
        button.setBackground(color);
        button.addActionListener(this);
        colorButtons[colorPanel.getComponentCount()] = button;
        colorPanel.add(button);
    }

    private void loadImage() {
        try {
            image = ImageIO.read(new File("Test_Images/masktry6.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void floodFill(int x, int y, int targetColor, int replacementColor) {
        if (x < 0 || y < 0 || x >= image.getWidth() || y >= image.getHeight()) {
            return;
        }
        if (image.getRGB(x, y) == targetColor) {
            image.setRGB(x, y, replacementColor);
            floodFill(x - 1, y, targetColor, replacementColor);
            floodFill(x + 1, y, targetColor, replacementColor);
            floodFill(x, y - 1, targetColor, replacementColor);
            floodFill(x, y + 1, targetColor, replacementColor);
        }
    }

    public void fill(int x, int y) {
        if (image == null) {
            return;
        }
        int targetColor = image.getRGB(x, y);
        if (targetColor == colorCode) {
            return;
        }
        floodFill(x, y, targetColor, colorCode);
        repaint();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ColoringApp();
            }
        });
    }
}