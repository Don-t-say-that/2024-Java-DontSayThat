import javax.swing.*;
import java.awt.*;

public class Standby extends JFrame {
    JPanel panel = new JPanel();
    JButton nextbtn = new RoundedButton("게임 시작하기", 25); // radius 25인 버튼 생성
    JLabel title = new JLabel("상대방을 기다리는 중 . . .");
    JLabel doneImg = new JLabel(new ImageIcon("./img/done.png"));
    Font font = new Font("WagleWagle", Font.PLAIN, 80);

    public Standby() {
        setTitle("상대방 대기");

        // panel GridBagLayout 설정
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // title 설정
        title.setFont(font);
        title.setForeground(Color.decode("#3D5A80"));
        gbc.gridx = 0; // x 좌표
        gbc.gridy = 0; // y 좌표 (위쪽에 배치)
        gbc.anchor = GridBagConstraints.PAGE_START; // 위쪽에 고정
        panel.add(title, gbc);

        // doneImg 설정 (title과 next 버튼 사이에 배치)
        gbc.gridx = 0;
        gbc.gridy = 1;      // title 아래에 배치
        gbc.anchor = GridBagConstraints.CENTER; // 중앙 배치
        gbc.insets = new Insets(30, 0, 0, 0);
        panel.add(doneImg, gbc);

        // next 버튼 설정
        nextbtn.setFont(new Font("WagleWagle", Font.PLAIN, 40));
        nextbtn.setBorderPainted(false);        // 테두리 없애기
        nextbtn.setFocusPainted(false);
        nextbtn.setPreferredSize(new Dimension(220, 80));      // 레이아웃 적용 시 size변경
        nextbtn.setContentAreaFilled(false);
        nextbtn.setBackground(Color.decode("#BFDBE4"));
        gbc.gridx = 0;
        gbc.gridy = 2;      // 이미지 아래에 배치
        gbc.anchor = GridBagConstraints.PAGE_END;   // 아래쪽에 고정
        gbc.insets = new Insets(80, 0, 0, 0);
        panel.add(nextbtn, gbc);

        // 프레임에 추가
        add(panel);

        // 패널 배경색 설정
        panel.setBackground(Color.decode("#E0FBFC"));

        // 프레임 설정
        setSize(1000, 700);
        setLocationRelativeTo(null); // 화면 중앙에 창 배치
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Standby();
    }
}