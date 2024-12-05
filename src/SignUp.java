import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SignUp extends JFrame{

    Font font = new Font("WagleWagle",Font.PLAIN, 110);
    RoundedPanel panel = new RoundedPanel(25, new Color(61, 90, 128));
    JLabel SignupLabel = new JLabel("SignUp");
    JLabel userLabel = new JLabel("ID : ");
    JButton loginCheckButton = new JButton("중복확인");
    JTextField userText = new JTextField();
    JLabel passLabel = new JLabel("Password : ");
    JPasswordField passText = new JPasswordField();
    JButton signUpButton = new JButton("회원가입");

    public SignUp() {

        // 패널
        panel.setLayout(null);  // Layout manager 비활성화하여 위치 지정
        panel.setBounds(300, 300, 400, 250);  // 패널의 위치와 크기 지정 (프레임 크기에 맞게 설정)

        SignupLabel.setFont(font);
        SignupLabel.setBounds(410,120,420,79);
        SignupLabel.setForeground(Color.decode("#3D5A80"));

        // 사용자 라벨과 입력 필드
        userLabel.setForeground(Color.WHITE);
        userLabel.setBounds(35, 55, 200, 30);
        userText.setBounds(125, 55, 140, 30);

        // 중복확인 버튼
        loginCheckButton.setBounds(275, 59, 85, 20);
        loginCheckButton.setBackground(Color.decode("#98C1D9"));
        loginCheckButton.setForeground(Color.BLACK);

        // 비밀번호 라벨과 입력 필드
        passLabel.setForeground(Color.WHITE);
        passLabel.setBounds(35, 125, 200, 30);
        passText.setBounds(125, 125, 240, 30);

        // 회원가입 버튼
        signUpButton.setBounds(150, 190, 100, 30);
        signUpButton.setBackground(Color.decode("#98C1D9"));
        signUpButton.setForeground(Color.BLACK);

        // 패널에 컴포넌트 추가
        panel.add(userLabel);
        panel.add(userText);
        panel.add(passLabel);
        panel.add(passText);
        panel.add(loginCheckButton);
        panel.add(signUpButton);
        add(SignupLabel);

        signUpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String member_id = userText.getText();
                String member_pw = new String(passText.getPassword());

                // DB에 사용자 정보 저장
                Connection conn = DBConnection.getConnection();
                if (conn != null) {
                    try {
                        String query = "INSERT INTO membertable (member_id, member_pw) VALUES (?, ?)";
                        PreparedStatement pstmt = conn.prepareStatement(query);
                        pstmt.setString(1, member_id);
                        pstmt.setString(2, member_pw);

                        int rows = pstmt.executeUpdate(); // 실행 후 영향받은 행 수 반환
                        if (rows > 0) {
                            JOptionPane.showMessageDialog(null, "회원가입 성공!");
                        } else {
                            JOptionPane.showMessageDialog(null, "회원가입 실패!");
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "이미 존재하는 사용자입니다.");
                    }
                }
                setVisible(false); // 현재 창 닫기
                new Login(); // Login 창 열기
            }
        });

        loginCheckButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String id = userText.getText();
                if (id.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "아이디를 입력해주세요.");
                } else if (DBConnection.isIdDuplicate(id)) {
                    JOptionPane.showMessageDialog(null, "이미 존재하는 아이디입니다.");
                } else {
                    JOptionPane.showMessageDialog(null, "사용 가능한 아이디입니다.");
                }
            }
        });

        // JFrame 설정
        getContentPane().setBackground(Color.decode("#E0FBFC"));
        setLayout(null);
        add(panel);
        setSize(1000, 700);  // 프레임 크기 설정
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new SignUp(); // 프로그램 시작
    }
}
