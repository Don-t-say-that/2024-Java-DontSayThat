// Standby.java
import javax.swing.*;
import java.awt.*;

public class Standby extends JFrame {
    private JPanel panel = new JPanel();
    private JLabel title = new JLabel("상대방을 기다리는 중 . . .");
    private JLabel doneImg = new JLabel(new ImageIcon("./img/done.png"));
    private Font font = new Font("WagleWagle", Font.PLAIN, 80);

    public Standby() {

        setTitle("상대방 대기");
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        title.setFont(font);
        title.setForeground(Color.decode("#3D5A80"));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 0, 10, 0);
        add(title, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(20, 0, 20, 0);
        add(doneImg, gbc);

        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void closeStandby() {
        dispose(); // Standby 창 닫기
    }
    public static void main(String[] args) {
        new Standby();
    }
}
