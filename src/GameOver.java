import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameOver extends JFrame {
    private Font font = new Font("WagleWagle", Font.PLAIN, 110);
    private Font font1 = new Font("Pretendard", Font.BOLD, 20);
    private JLabel gameOver = new JLabel("-Game Over-", SwingConstants.CENTER);
    private ImageIcon returnIcon = new ImageIcon("./img/Return.png");
    private ImageIcon seeRankingIcon = new ImageIcon("./img/SeeRanking.png");
    JButton returnButton = new JButton(returnIcon);
    JButton seeRankingButton = new JButton(seeRankingIcon);
    private String player1Name = "dauyul";
    private String player2Name = "hyewon4052";
    private int player1Score = 860;
    private int player2Score = 700;

    public GameOver() {
        // Frame 레이아웃 설정
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.decode("#E0FBFC"));

        // Title Label
        gameOver.setFont(font);
        gameOver.setHorizontalAlignment(SwingConstants.CENTER);
        add(gameOver, BorderLayout.NORTH); // 화면 상단에 배치

        // Custom Panel for Ranks
        PaintPanel WinLosePanel = new PaintPanel();
        add(WinLosePanel, BorderLayout.CENTER); // 화면 중앙에 배치

        // Custom Button Panel
        CustomButtonPanel buttonPanel = new CustomButtonPanel(returnButton, seeRankingButton);
        add(buttonPanel, BorderLayout.SOUTH); // 화면 하단에 배치

        // Frame settings
        setTitle("게임오버");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new Start();
            }
        });

        seeRankingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new Ranking();
            }
        });
    }

    class PaintPanel extends JPanel {
        public PaintPanel() {
            setLayout(null);
            setBackground(Color.decode("#E0FBFC"));

            // Win Section
            JLabel winLabel = createLabel("Win", Color.GREEN, 108, 75, 100, 50);
            JLabel winName = createLabel(player1Name, Color.WHITE, 410, 75, 200, 50);
            JLabel winScore = createLabel(player1Score + "점", Color.WHITE, 800, 75, 100, 50);

            // Lose Section
            JLabel loseLabel = createLabel("Lose", Color.RED, 103, 215, 100, 50);
            JLabel loseName = createLabel(player2Name, Color.WHITE, 410, 215, 200, 50);
            JLabel loseScore = createLabel(player2Score + "점", Color.WHITE, 800, 215, 100, 50);

            // Add labels
            add(winLabel);
            add(winName);
            add(winScore);
            add(loseLabel);
            add(loseName);
            add(loseScore);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.decode("#3D5A80"));
            // Draw rectangles
            g.fillRoundRect(60, 60, 880, 80, 30, 30);
            g.fillRoundRect(60, 200, 880, 80, 30, 30);

            g.setColor(Color.WHITE);
            g.fillRoundRect(70, 70, 115, 60, 30, 30);
            g.fillRoundRect(70, 210, 115, 60, 30, 30);
        }

        private JLabel createLabel(String text, Color color, int x, int y, int width, int height) {
            JLabel label = new JLabel(text, SwingConstants.LEFT);
            label.setBounds(x, y, width, height);
            label.setForeground(color);
            label.setFont(font1);
            return label;
        }
    }

    class CustomButtonPanel extends JPanel {
        public CustomButtonPanel(JButton returnButton, JButton seeRankingButton) {
            setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
            setBackground(Color.decode("#E0FBFC"));

            // 버튼 설정
            returnButton.setContentAreaFilled(false);
            seeRankingButton.setContentAreaFilled(false);
            returnButton.setBorderPainted(false);
            seeRankingButton.setBorderPainted(false);
            returnButton.setFocusPainted(false);
            seeRankingButton.setFocusPainted(false);

            // 패널에 버튼 추가
            add(returnButton);
            add(seeRankingButton);
        }
    }

    public static void main(String[] args) {
        new GameOver();
    }
}
