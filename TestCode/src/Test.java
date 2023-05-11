import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Test {

    public static void main(String[] args) {
        new Test();
    }

    public Test() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame();
                frame.add(new TestPane());
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    public class TestPane extends JPanel {

        /**
		 * 
		 */
    	
		private static final long serialVersionUID = 1L;
		private BufferedImage image;
        private JComboBox<String> stickerComboBox;
        private JButton saveButton;

        public TestPane() {
            try {
                image = ImageIO.read(new File("Test_Images/masktry6.png"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            // Create the sticker combo box and add the sticker options
            stickerComboBox = new JComboBox<>();
            stickerComboBox.addItem("Boom.png");
            stickerComboBox.addItem("Flag.png");
            stickerComboBox.addItem("Glove.png");
            stickerComboBox.addItem("Heart.png");
            stickerComboBox.addItem("Maracas.png");
            stickerComboBox.addItem("Sombrero.png");
            stickerComboBox.addItem("Ukulele.png");
            add(stickerComboBox);
            
            
            saveButton = new JButton("Save");
            saveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFileChooser fileChooser = new JFileChooser();
                    FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG Images", "png");
                    fileChooser.setFileFilter(filter);
                    int returnValue = fileChooser.showSaveDialog(TestPane.this);
                    if (returnValue == JFileChooser.APPROVE_OPTION) {
                        File file = fileChooser.getSelectedFile();
                        try {
                            ImageIO.write(image, "png", file);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            });
            add(saveButton);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (image != null) {
                        // Get the selected sticker image from the combo box
                        String selectedSticker = (String) stickerComboBox.getSelectedItem();

                        // Load the sticker image from a file
                        BufferedImage stickerImage = null;
                        try {
                            stickerImage = ImageIO.read(new File("Test_Images/" + selectedSticker));
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        if (stickerImage != null) {
                            // Calculate the position to place the sticker
                            int x = e.getX() - stickerImage.getWidth() / 2;
                            int y = e.getY() - stickerImage.getHeight() / 2;
                            
                            // Draw the sticker onto the main image
                            Graphics2D g2d = image.createGraphics();
                            g2d.drawImage(stickerImage, x, y, null);
                            g2d.dispose();

                            repaint();
                        }
                    }
                }
            });
        }
        
        

        @Override
        public Dimension getPreferredSize() {
            return image == null ? new Dimension(200, 200) : new Dimension(image.getWidth(), image.getHeight());
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.drawImage(image, 0, 0, this);
            g2d.dispose();
        }

    }

}