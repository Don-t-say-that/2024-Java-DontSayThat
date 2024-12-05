
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.io.*;
import java.net.Socket;

public class GamePlay extends JFrame {
    private PrintWriter out;
    private Image backgroundImage = new ImageIcon("./img/gameBackground.png").getImage();

    // 메인 배경화면 패널
    BackgroundPanel mainContainer = new BackgroundPanel(backgroundImage);

    // 폰트
    Font font1 = new Font("WagleWagle", Font.PLAIN, 45);
    Font font2 = new Font("맑은 고딕", Font.PLAIN, 15);

    // 상단 타이머 패널
    JPanel topPanel = new JPanel();
    JLabel randomTopic = new JLabel("Random Topic");
    JLabel timerLabel = new JLabel("남은 시간: 02:00");

    // 메인 패널
    Panel panel = new Panel(null);
    JButton completeBtn = new RoundedButton("→", 25);
    JTextField inputField = new JTextField();

    JPanel characterPanel = new JPanel();
    JLabel character_r = new JLabel(new ImageIcon("./img/character1.png"));
    JLabel character_b = new JLabel(new ImageIcon("./img/character2.png"));

    // 채팅창
    JTextArea chatArea = new JTextArea();
    JScrollPane chatScroll = new JScrollPane(chatArea);



    public GamePlay(String username, String forbiddenWord) {

        // 메인 패널 설정(배경 사진)
        mainContainer.setBounds(0, 0, 1000, 700);
        mainContainer.setLayout(null);

        setLayout(null);

        // 상단 패널 설정 (랜덤 주제, 타이머)
        topPanel.setBounds(10, 10, 600, 150);
        topPanel.setLayout(null);
        topPanel.setOpaque(false);

        randomTopic.setFont(font1);
        randomTopic.setBounds(50, 10, 300, 50);
        randomTopic.setOpaque(true);
        randomTopic.setBackground(Color.WHITE);
        randomTopic.setForeground(Color.BLACK);
        randomTopic.setHorizontalAlignment(SwingConstants.CENTER); // 텍스트 중앙 정렬
        randomTopic.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(15),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        timerLabel.setFont(font1);
        timerLabel.setBounds(50, 90, 300, 50);
        timerLabel.setOpaque(true);
        timerLabel.setBackground(Color.WHITE);
        timerLabel.setForeground(Color.BLACK);
        timerLabel.setHorizontalAlignment(SwingConstants.LEFT); // 텍스트 왼쪽 정렬
        timerLabel.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(15),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        topPanel.add(randomTopic);
        topPanel.add(timerLabel);

        mainContainer.add(topPanel);

        // 캐릭터 패널
        characterPanel.setBounds(100, 230, 500, 350);
        characterPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
        characterPanel.setOpaque(false);

        character_r.setPreferredSize(new Dimension(150, 250));
        character_b.setPreferredSize(new Dimension(150, 250));

        characterPanel.add(character_r);
        characterPanel.add(character_b);
        mainContainer.add(characterPanel);

        // 채팅창 추가
        chatArea.setEditable(false);
        chatArea.setFont(font2);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);

        chatScroll.setBounds(660, 120, 250, 350);
        mainContainer.add(chatScroll);

        // 입력창 추가
        inputField.setBounds(250, 560, 400, 30);
        inputField.setFont(font2);
        mainContainer.add(inputField);

        // 완료 버튼 추가
        completeBtn.setBounds(670, 560, 70, 35);
        completeBtn.setFont(font2);
        completeBtn.setBackground(Color.WHITE);
        completeBtn.setBorderPainted(false);
        completeBtn.setFocusPainted(false);
        mainContainer.add(completeBtn);
        add(mainContainer);

        // 리스너 설정
        completeBtn.addActionListener(e -> sendMessage(username));
        inputField.addActionListener(e -> sendMessage(username));

        // 서버 연결
        connectToServer(username, forbiddenWord);

        // 프레임 설정
        setVisible(true);
        setSize(1000, 700);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void connectToServer(String username,String forbiddenWord) {
        try {
            Socket socket = new Socket("localhost", 8000);
            out = new PrintWriter(socket.getOutputStream(), true);

            // 서버로 사용자 이름과 금칙어 전송
            out.println(username);
            out.println("/forbidden " + forbiddenWord);
          
            new Thread(() -> {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                    String message;
                    while ((message = in.readLine()) != null) {
                        if(message.startsWith("randomTopic")) {
                            randomTopic.setText(message.replace("randomTopic ", "주제 : "));
                        }
                        else if(message.startsWith("Timer")) {
                            if(message.equals("Timer 종료"))
                                timerLabel.setText("게임 종료!");
                            else {
                                int time = Integer.parseInt(message.replace("Timer ", ""));
                                int min = time / 60;
                                int sec = time % 60;
                                timerLabel.setText(String.format("남은 시간: %02d:%02d", min, sec));
                            }
                        }
                        else chatArea.append(message + "\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            // 접속 메시지 전송
            out.println(username + "님이 입장하셨습니다.");

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "서버에 연결할 수 없습니다.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    private void sendMessage(String username) {
        String message = inputField.getText().trim();
        if (!message.isEmpty()) {
            out.println(username + ": " + message);
            inputField.setText("");
        }
    }
}

class RoundedBorder extends AbstractBorder {
    private int radius;

    RoundedBorder(int radius) {
        this.radius = radius;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // border 그리기
        g2d.setColor(c.getForeground());
        g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);

        g2d.dispose();
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(radius / 2, radius / 2, radius / 2, radius / 2);
    }
}


// 배경 화면 사진 패널
class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel(Image backgroundImage) {
        this.backgroundImage = backgroundImage;
        setLayout(null);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}