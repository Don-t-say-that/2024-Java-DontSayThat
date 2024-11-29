import javax.swing.*;
import java.awt.*;

public class Ranking extends JFrame {
    private JLabel title = new JLabel("랭킹", SwingConstants.CENTER); // 텍스트 중앙 정렬
    private Font font = new Font("WagleWagle", Font.PLAIN, 70);
    private Font font1 = new Font("Pretendard",Font.PLAIN,15);
    private Font font2 = new Font("Pretendard",Font.PLAIN,18);
    public Ranking() {
        // 프레임 전체 레이아웃은 BorderLayout 사용
        setLayout(new BorderLayout());

        // 제목 설정
        title.setFont(font);
        title.setForeground(Color.decode("#98C1D9"));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title, BorderLayout.NORTH); // 제목을 상단에 배치

        // PaintPanel 추가 (절대 위치 패널)
        PaintPanel centerPanel = new PaintPanel();
        add(centerPanel, BorderLayout.CENTER); // PaintPanel을 중앙에 배치

        // 프레임 설정
        setTitle("랭킹");
        getContentPane().setBackground(Color.decode("#E0FBFC"));
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    // 커스텀 패널 클래스
    class PaintPanel extends JPanel {
        public PaintPanel() {
            // PaintPanel 내부는 절대 위치로 설정
            setLayout(null);
            setBackground(Color.decode("#E0FBFC"));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // 막대 그래프 그리기
            drawBar(g2, 270, 150, 150, 200, "#6DAEDB", "hyewon4052","940점"); // Left Bar
            drawBar(g2, 420, 50, 150, 300, "#004E89", "yeoun1","910점"); // Middle Bar (Highest)
            drawBar(g2, 570, 200, 150, 150, "#98C1D9", "jooo","900점"); // Right Bar

            // 점수 리스트 도형 추가
            drawRankingEntry(g2, 200, 400, 600, 60, "4", "hyewon4052", "870점", "#6DAEDB");
            drawRankingEntry(g2, 200, 470, 600, 60, "5", "hayyeong07", "810점", "#6DAEDB");
        }

        private void drawBar(Graphics2D g2, int x, int y, int width, int height, String color, String name, String score) {
            // 막대 그래프 그리기
            g2.setColor(Color.decode(color));
            g2.fillRoundRect(x, y, width, height, 20, 20);

            // 이름 표시 (막대 중앙 위에)
            g2.setColor(Color.WHITE);
            g2.setFont(font1);
            int nameX = x + (width / 2) - g2.getFontMetrics().stringWidth(name) / 2;
            int nameY = y + 40;
            g2.drawString(name, nameX, nameY);

            // 점수 표시 (이름 아래에)
            g2.setColor(Color.WHITE);
            g2.setFont(font1);
            int scoreX = x + (width / 2) - g2.getFontMetrics().stringWidth(score) / 2;
            int scoreY = nameY + 20; // 점수는 이름 바로 아래에 표시
            g2.drawString(score, scoreX, scoreY);
        }


        private void drawRankingEntry(Graphics2D g2, int x, int y, int width, int height, String rank, String name, String score, String color) {
            // 전체 도형 배경
            g2.setColor(Color.decode("#4583D4"));
            g2.fillRoundRect(x, y, width, height, 30, 30);

            // 순위 번호 (왼쪽)
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(x + 10, y + 10, 40, height - 20, 20, 20);

            g2.setColor(new Color(0, 0, 0)); // 순위 번호 글씨 색상
            g2.setFont(new Font("Arial", Font.BOLD, 18));
            g2.drawString(rank, x + 25, y + height / 2 + 6); // 센터 정렬

            // 이름 (중앙)
            g2.setColor(Color.WHITE);
            g2.setFont(font2);
            int nameX = x + (width / 2) - g2.getFontMetrics().stringWidth(name) / 2; // 이름 센터 정렬
            g2.drawString(name, nameX, y + height / 2 + 6);

            // 점수 (오른쪽)
            g2.setFont(font2);
            int scoreX = x + width - 80;
            g2.drawString(score, scoreX, y + height / 2 + 6);
        }

    }

    public static void main(String[] args) {
        new Ranking();
    }
}
