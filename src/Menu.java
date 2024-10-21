import java.awt.*;
import javax.swing.*;

public class Menu extends JFrame {

    ImageIcon gameImg = new ImageIcon("./img/game.png");
    ImageIcon rankingImg = new ImageIcon("./img/ranking.png");
    JPanel panel = new JPanel();
    JButton gameButton = new JButton(gameImg);
    JButton rankingButton = new JButton(rankingImg);

    public Menu() {

        // 패널 레이아웃 설정
        panel.setLayout(new GridBagLayout());   // 레이아웃 생성자
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 70, 0, 70);  // 버튼 사이 간격 설정 (좌우 간격 70px)
        gbc.anchor = GridBagConstraints.CENTER;

        // 버튼 설정
        gameButton.setBorderPainted(false); rankingButton.setBorderPainted(false);  // 버튼 테두리 설정해제
        gameButton.setPreferredSize(new Dimension(219, 217)); rankingButton.setPreferredSize(new Dimension(219, 217));
        gameButton.setContentAreaFilled(false); rankingButton.setContentAreaFilled(false); // 내용영역 채우기 안함
        gameButton.setFocusPainted(false); rankingButton.setFocusPainted(false); // 선택되었을 때 생기는 테두리 사용 안함

        // 첫 번째 버튼 패널에 추가
        panel.add(gameButton, gbc);

        // 두 번째 버튼 패널에 추가
        gbc.gridx = 1;  // 두 번째 버튼
        panel.add(rankingButton, gbc);

        // 패널을 프레임에 추가
        add(panel);

        panel.setSize(1000,700);
        panel.setBackground(Color.decode("#E0FBFC"));
        // 프레임 설정
        setVisible(true);
        setSize(1000, 700); // 프레임 크기
        setResizable(false);
        setLocationRelativeTo(null); // 화면 중앙 배치
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        new Menu(); // 프로그램 시작
    }
}
