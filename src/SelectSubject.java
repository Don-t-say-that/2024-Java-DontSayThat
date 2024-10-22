import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class SelectSubject extends JFrame {

    Font font = new Font("WagleWagle", Font.PLAIN, 80);
    Font font1 = new Font("WagleWagle", Font.PLAIN, 60);
    RoundedPanel panel = new RoundedPanel(25, new Color(61, 90, 128));
    JLabel subjectLabel = new JLabel("<html><body><center>랜덤 주제는...<br>두구두구두..구..두..ㄱ..</center></body></html>"); // 줄바꿈 위해서 html문법 사용
    JLabel randomSubjectLabel = new JLabel(); // 랜덤 주제를 표시할 라벨

    public SelectSubject() {

        setLayout(null); // 위치 수동 지정

        subjectLabel.setFont(font);
        subjectLabel.setBounds(250, 50, 600, 184); // 프레임 안에서의 위치와 크기 설정
        subjectLabel.setForeground(Color.decode("#3D5A80"));

        panel.setLayout(new GridBagLayout()); // 패널 중앙 배치
        panel.setBounds(270, 300, 460, 250);

        randomSubjectLabel.setFont(font1);
        randomSubjectLabel.setForeground(Color.black);
        randomSubjectLabel.setBackground(Color.WHITE); // 배경색 설정
        randomSubjectLabel.setOpaque(true); // 배경색 적용을 위해 opaque 설정
        randomSubjectLabel.setPreferredSize(new Dimension(400, 150)); // 라벨의 크기를 고정
        randomSubjectLabel.setHorizontalAlignment(SwingConstants.CENTER); // 텍스트 가로 중앙 정렬
        randomSubjectLabel.setVerticalAlignment(SwingConstants.CENTER); // 텍스트 세로 중앙 정렬

        panel.add(randomSubjectLabel); // 랜덤 주제를 표시할 라벨을 패널에 추가

        add(subjectLabel);
        add(panel);

        // 프레임 설정
        getContentPane().setBackground(Color.decode("#E0FBFC"));
        setVisible(true);
        setSize(1000, 700);
        setResizable(false);
        setLocationRelativeTo(null); // 화면 중앙 배치
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        randomSubject(); // 창을 열 때 랜덤 주제 설정
    }

    void randomSubject() {
        String sj[] = {"오늘의 tmi", "학교 생활", "일상", "옷", "지구온난화", "화장", "결벽증", "사랑", "친구", "동물", "아이폰", "과일",
                "소리", "천체", "악기", "편견", "음식", "종교", "죽음", "데일리"};
        Random rand = new Random();
        String selectedSubject = sj[rand.nextInt(sj.length)]; // 랜덤으로 주제 선택
        randomSubjectLabel.setText(selectedSubject); // 선택된 주제를 라벨에 설정
    }

    public static void main(String[] args) {
        new SelectSubject();
    }
}
