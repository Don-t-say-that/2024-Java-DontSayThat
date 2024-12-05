import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class explainRule extends JFrame {

    Font font = new Font("WagleWagle", Font.PLAIN, 80);
    Font font1 = new Font("WagleWagle", Font.PLAIN, 25);
    RoundedPanel panel = new RoundedPanel(25, new Color(61, 90, 128));
    JLabel title = new JLabel("게임 설명");
    JLabel explainRule = new JLabel("<html><body><center>앞서 뽑은 랜덤 주제에 대해 2분동안 어쩌구 저쩌구 그것입니다.<br>그리고 어쩌구 저쩌구 그럼 좋아요. " +
            "게임을 시작할까요? </center></body></html>"); // 게임 설명
    JButton nextBtn = new RoundedButton("다음으로", 25);
    String member_id;
    public explainRule(String member_id) {
        this.member_id = member_id;

        setLayout(null); // 위치 수동 지정

        // 제목 설정
        title.setFont(font);
        title.setBounds(350, -30, 600, 184); // 프레임 안에서의 위치와 크기 설정
        title.setForeground(Color.decode("#3D5A80"));

        panel.setLayout(new GridBagLayout()); // 패널 중앙 배치
        panel.setBounds(205, 125, 600, 500);

        // 게임 설명 라벨 설정
        explainRule.setFont(font1);
        explainRule.setForeground(Color.black);
        explainRule.setBackground(Color.WHITE); // 배경색 설정
        explainRule.setOpaque(true); // 배경색 적용을 위해 opaque 설정
        explainRule.setPreferredSize(new Dimension(500, 400)); // 라벨의 크기를 고정
        explainRule.setHorizontalAlignment(SwingConstants.CENTER); // 텍스트 가로 중앙 정렬
        explainRule.setVerticalAlignment(SwingConstants.CENTER); // 텍스트 세로 중앙 정렬
        explainRule.setBounds(360, 110, 300, 300);
        explainRule.setBorder(BorderFactory.createEmptyBorder(10 , 20, 30 , 20));       // padding과 같은 역할

        // nextBtn 설정
        nextBtn.setFont(font1);
        nextBtn.setSize(120, 40); // 버튼 크기 설정
        nextBtn.setLocation(845, 600); // 버튼 위치 설정
        nextBtn.setBackground(Color.decode("#BFDBE4")); // 버튼 배경색 설정
        nextBtn.setForeground(Color.decode("#3D5A80")); // 버튼 글자색 설정
        nextBtn.setBorderPainted(false); // 버튼 테두리 제거
        nextBtn.setFocusPainted(false); // 포커스 테두리 제거

        // 버튼에 ActionListener 추가
        nextBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false); // 현재 창 닫기
                new setForbiddenword(member_id);
            }
        });

        panel.add(nextBtn);        // 버튼을 패널에 추가
        panel.add(explainRule); // 게임 설명을 패널에 추가

        add(nextBtn);
        add(title);
        add(panel);

        // 프레임 설정
        getContentPane().setBackground(Color.decode("#E0FBFC"));
        setVisible(true);
        setSize(1000, 700);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

    }
    public static void main(String[] args) {
        new explainRule("hyewon");
    }
}