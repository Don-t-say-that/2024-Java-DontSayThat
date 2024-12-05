import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Ranking extends JFrame {
    private JLabel title = new JLabel("랭킹", SwingConstants.CENTER);
    private JButton restartButton = new RoundedButton("처음으로", 25);
    private Font font = new Font("WagleWagle", Font.PLAIN, 70);
    private Font font2 = new Font("Pretendard", Font.PLAIN, 18);
    private Font font3 = new Font("WagleWagle", Font.PLAIN, 30);
    private List<RankingEntry> rankingEntries; // 랭킹 데이터를 저장할 리스트

    public Ranking() {
        rankingEntries = fetchRankingData(); // 데이터베이스에서 랭킹 데이터 가져오기

        // 프레임 전체 레이아웃은 BorderLayout 사용
        setLayout(new BorderLayout());

        // 제목 설정
        title.setFont(font);
        title.setForeground(Color.decode("#98C1D9"));
        add(title, BorderLayout.NORTH);

        restartButton.setBackground(Color.decode("#3D5A80")); // 버튼 배경색 설정
        restartButton.setForeground(Color.WHITE); // 버튼 글자색 설정
        restartButton.setBorderPainted(false); // 버튼 테두리 제거
        restartButton.setFocusPainted(false); // 포커스 테두리 제거

        // restart 버튼을 우측 하단에 배치하기 위한 패널 생성
        JPanel restartPanel = new JPanel();
        restartPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));  // 오른쪽 정렬
        restartPanel.setBackground(Color.decode("#E0FBFC")); // 배경색을 부모 패널과 동일
        restartButton.setFont(font3);
        restartPanel.add(restartButton);  // 버튼을 패널에 추가

        add(restartPanel, BorderLayout.SOUTH);  // 패널을 남쪽에 추가

        // PaintPanel 추가 (절대 위치 패널)
        PaintPanel centerPanel = new PaintPanel();
        add(centerPanel, BorderLayout.CENTER);

        // 프레임 설정
        setTitle("랭킹");
        getContentPane().setBackground(Color.decode("#E0FBFC"));
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new Start();
            }
        });

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

            // 데이터베이스에서 가져온 데이터를 기반으로 랭킹 그리기
            int yPosition = 60;
            for (int i = 0; i < 5; i++) {
                RankingEntry entry = rankingEntries.get(i);
                drawRankingEntry(g2, 200, yPosition, 600, 70, String.valueOf(i + 1), entry.getName(), entry.getScore());
                yPosition += 90;
            }
        }

        private void drawRankingEntry(Graphics2D g2, int x, int y, int width, int height, String rank, String name, String score) {
            g2.setColor(Color.decode("#4583D4"));
            g2.fillRoundRect(x, y, width, height, 30, 30);

            g2.setColor(Color.WHITE);
            g2.fillRoundRect(x + 10, y + 10, 40, height - 20, 20, 20);

            g2.setColor(new Color(0, 0, 0));
            g2.setFont(new Font("Arial", Font.BOLD, 18));
            g2.drawString(rank, x + 25, y + height / 2 + 6);

            g2.setColor(Color.WHITE);
            g2.setFont(font2);
            int nameX = x + (width / 2) - g2.getFontMetrics().stringWidth(name) / 2;
            g2.drawString(name, nameX, y + height / 2 + 6);

            g2.setFont(font2);
            int scoreX = x + width - 80;
            g2.drawString(score, scoreX, y + height / 2 + 6);
        }
    }

    // RankingEntry 클래스 (랭킹 데이터 저장용)
    class RankingEntry {
        String name;
        String score;

        public RankingEntry(String name, String score) {
            this.name = name;
            this.score = score;
        }

        public String getName() {
            return name;
        }

        public String getScore() {
            return score;
        }

    }

    // 데이터베이스에서 랭킹 데이터 가져오기
    private List<RankingEntry> fetchRankingData() {
        List<RankingEntry> entries = new ArrayList<>();
        String query = "SELECT member_id, MAX(score) AS score " +
                "FROM rankingtable " +
                "GROUP BY member_id " +
                "ORDER BY score DESC " +
                "LIMIT 10;";

        try (Connection conn = DBConnection.getConnection();  // DBConnection 클래스 사용
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String name = rs.getString("member_id");
                String score = rs.getString("score") + "점";
                entries.add(new RankingEntry(name, score));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return entries;
    }

    public static void main(String[] args) {
        new Ranking();
    }
}
