import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Start extends JFrame {


    JPanel panel = new JPanel();
    JButton startbtn = new RoundedButton("Click", 25); // radius 25인 버튼 생성
    JLabel title = new JLabel("DON'T SAY \"THAT\"",JLabel.CENTER);
    Font font = new Font("WagleWagle",Font.PLAIN, 95);
    Font font1 = new Font("WagleWagle",Font.PLAIN, 130);
    public Start() {

        // 폰트 설정
        title.setFont(font1);
        title.setSize(715, 110);
        title.setLocation(160,220);
        title.setForeground(Color.decode("#3D5A80"));
        // title을 panel에 추가
        panel.add(title);

        // 버튼 설정
        startbtn.setFont(font);
        startbtn.setSize(350, 90); // 버튼 크기 설정
        startbtn.setLocation(308, 430); // 버튼 위치 설정

        startbtn.setBackground(Color.decode("#3D5A80")); // 버튼 배경색 설정
        startbtn.setForeground(Color.WHITE); // 버튼 글자색 설정
        startbtn.setBorderPainted(false); // 버튼 테두리 제거
        startbtn.setFocusPainted(false); // 포커스 테두리 제거
        panel.add(startbtn);

        // 버튼에 ActionListener 추가
        startbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Start 창을 닫고 Login 창을 엽니다.
                setVisible(false); // 현재 창 닫기
                new Login(); // Login 창 열기
            }
        });

        // 패널의 레이아웃 설정 및 프레임에 추가
        panel.setSize(1000, 700); // 패널 크기 설정
        panel.setLayout(null); // 패널 레이아웃을 null로 설정
        add(panel);

        // 프레임 설정
        setVisible(true);
        setSize(1000, 700);
        setResizable(false);
        panel.setBackground(Color.decode("#E0FBFC"));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }


    public static void main(String[] args) {
        new Start(); // 프로그램 시작
    }
}

// 커스터마이즈된 둥근 버튼 클래스
class RoundedButton extends JButton {
    private int radius;

    public RoundedButton(String text, int radius) {
        super(text);
        this.radius = radius;
        setOpaque(false); // 투명하게 설정
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 버튼 배경 그리기
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        // 텍스트 그리기
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 버튼 테두리 그리기
        g2.setColor(getForeground());
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
    }
}
