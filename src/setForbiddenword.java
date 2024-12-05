import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class setForbiddenword extends JFrame {

    Font font = new Font("WagleWagle", Font.PLAIN, 80);
    Font font1 = new Font("WagleWagle", Font.PLAIN, 25);
    RoundedPanel panel = new RoundedPanel(25, new Color(61, 90, 128));
    JLabel title = new JLabel("상대방 금칙어 정하기");
    JButton completeWordBtn = new RoundedButton("완료", 25);
    JTextField wordField = new JTextField();

    setForbiddenword() {
        // 패널 설정
        panel.setLayout(null);
        panel.setBounds(300, 300, 400, 230);

        // title label 설정
        title.setFont(font);
        title.setBounds(255,120,800,79);
        title.setForeground(Color.decode("#3D5A80"));

        // 금칙어 입력 필드
        wordField.setBounds(75, 60, 260, 30);

        // 완료 버튼
        completeWordBtn.setBounds(145, 120, 100, 35);
        completeWordBtn.setFont(font1);
        completeWordBtn.setBackground(Color.decode("#BFDBE4")); // 버튼 배경색 설정
        completeWordBtn.setForeground(Color.decode("#3D5A80")); // 버튼 글자색 설정
        completeWordBtn.setBorderPainted(false); // 버튼 테두리 제거
        completeWordBtn.setFocusPainted(false); // 포커스 테두리 제거


        panel.add(wordField);
        panel.add(completeWordBtn);

        add(title);
        add(panel);

        // JFrame 설정
        getContentPane().setBackground(Color.decode("#E0FBFC"));
        setLayout(null);
        add(panel);
        setSize(1000, 700);  // 프레임 크기 설정
        setLocationRelativeTo(null);  // 화면 중앙에 배치
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        completeWordBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                String username = JOptionPane.showInputDialog("사용자 이름을 입력하세요:");
                if (username != null && !username.trim().isEmpty()) {
                    new ChatRoom(username); // 채팅방 열기
                }
            }
        });
    }

    public static void main(String[] args) {
        new setForbiddenword();
    }
}