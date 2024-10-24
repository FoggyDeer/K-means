import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        GraphingData data = new GraphingData("./data/2d_data.csv");

        SwingUtilities.invokeLater(()->{
            JFrame f = new JFrame();
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            JPanel mainPanel = new JPanel();
            JPanel interactionPanel = new JPanel();
            interactionPanel.setLayout(new GridLayout(2, 1));
            TextField field = new TextField();
            JButton button = new JButton("Regroup");
            button.addActionListener(e -> {
                try {
                    data.stop();
                    data.init(Integer.parseInt(field.getText()));
                    data.start();
                } catch (InterruptedException exception) {
                    throw new RuntimeException(exception);
                }catch (NumberFormatException ignored){
                }
            });
            interactionPanel.setPreferredSize(new Dimension(120, 70));
            interactionPanel.add(field);
            interactionPanel.add(button);
            mainPanel.add(interactionPanel , BorderLayout.WEST);
            data.setPreferredSize(new Dimension(980, 600));
            data.setOpaque(false);
            mainPanel.setBackground(Color.BLACK);
            mainPanel.add(data, BorderLayout.CENTER);
            f.add(mainPanel);
            f.setSize(1160,650);
            f.setLocationRelativeTo(null);
            f.setVisible(true);
            f.setResizable(false);

        });

    }
}
