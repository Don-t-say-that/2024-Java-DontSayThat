import javax.swing.*;
import java.awt.*;

public class GamePlay extends JFrame {
    private static int timeRemaining = 120; // 초기 타이머 시간 (초 단위)
    private Image backgroundImage = new ImageIcon("./img/gameBackground.png").getImage();

    Font font1 = new Font("WagleWagle", Font.PLAIN, 45);
    Font font2 = new Font("맑은 고딕", Font.PLAIN, 25);

    // 상단 타이머 패널
    JPanel topPanel = new JPanel();
    JLabel randomTopic = new JLabel("Random Topic");
    JLabel timerLabel = new JLabel("남은 시간: 02:00");

    // 메인 패널
    Panel panel = new Panel(null);
    JButton completeBtn = new RoundedButton("→", 25);
    JTextField wordField = new JTextField();

    JPanel characterPanel = new JPanel();
    JLabel character_r = new JLabel(new ImageIcon("./img/character1.png"));
    JLabel character_b = new JLabel(new ImageIcon("./img/character2.png"));

    // 채팅창
    JTextArea chatArea = new JTextArea();
    JScrollPane chatScroll = new JScrollPane(chatArea);

    public GamePlay() {
        setLayout(null);

        // 상단 패널 설정 (랜덤 주제, 타이머)
        topPanel.setBounds(10, 10, 300, 150);
        topPanel.setLayout(null);
        topPanel.setOpaque(false);      // 배경 투명하게 하기

        randomTopic.setFont(font1);
        randomTopic.setBounds(50, 10, 280, 40);
        randomTopic.setOpaque(true);        // 배경색을 적용하기 전 해주기
        randomTopic.setBackground(Color.WHITE);
        randomTopic.setHorizontalAlignment(SwingConstants.CENTER); // 텍스트 중앙 정렬
        randomTopic.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        timerLabel.setFont(font1);
        timerLabel.setBounds(50, 90, 280, 40);
        timerLabel.setOpaque(true);
        timerLabel.setBackground(Color.WHITE);
        timerLabel.setHorizontalAlignment(SwingConstants.LEFT); // 텍스트 왼쪽 정렬
        timerLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        topPanel.add(timerLabel);
        topPanel.add(randomTopic);

        add(topPanel);

        // 메인 패널 설정(캐릭터 패널이 들어감)
        panel.setBounds(50, 140, 900, 500);
        panel.setLayout(null);
        // 캐릭터 패널
        characterPanel.setBounds(20, 35, 500, 350);
        characterPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
        characterPanel.setOpaque(false);

        character_r.setPreferredSize(new Dimension(100, 200));
        character_b.setPreferredSize(new Dimension(100, 200));

        characterPanel.add(character_r);
        characterPanel.add(character_b);
        panel.add(characterPanel);

        // 채팅창 추가
        chatArea.setEditable(false);
        chatArea.setFont(font2);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);

        chatScroll.setBounds(600, 40, 250, 350);
        panel.add(chatScroll);

        // 입력창 추가
        wordField.setBounds(200, 460, 400, 30);
        wordField.setFont(font1);
        panel.add(wordField);

        // 완료 버튼 추가
        completeBtn.setBounds(620, 460, 70, 35);
        completeBtn.setFont(font2);
        completeBtn.setBackground(Color.WHITE);
        completeBtn.setBorderPainted(false);
        completeBtn.setFocusPainted(false);
        panel.add(completeBtn);

        add(panel);

        // 타이머 구현
        Timer timer = new Timer(1000, e -> {
            if (timeRemaining > 0) {
                timeRemaining--;
                int minutes = timeRemaining / 60;
                int seconds = timeRemaining % 60;
                timerLabel.setText(String.format("남은 시간: %02d:%02d", minutes, seconds));
            } else {
                ((Timer) e.getSource()).stop();
                timerLabel.setText("시간 종료! 게임을 종료합니다.");
            }
        });
        timer.start();

        // 프레임 설정
        setVisible(true);
        setSize(1000, 700);
        setResizable(false);
        setLocationRelativeTo(null); // 화면 중앙 배치
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // 배경 이미지 그리기
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }

    public static void main(String[] args) {
        new GamePlay();
    }
}