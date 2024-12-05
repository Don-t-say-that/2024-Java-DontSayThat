import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class ChatServer {
    private static final int PORT = 8000;
    private static final String[] TOPICS = {
            "음식", "영화","학교", "급식", "TMI", "좋아하는 것", "과거", "미래", "현재", "이상형", "영화",
            "동물", "여행", "과목", "기분", "크리스마스", "연말", "직업", "장래희망", "밈", "첫 데이트"
    };
    public static Map<String, Integer> scores = new HashMap<>();           // 점수 관리
    private static Map<String, String> forbiddenWords = new HashMap<>();    // 금칙어 저장
    private static Set<PrintWriter> clientWriters = new HashSet<>();        // 출력 스트림 저장
    private static int timeRemaining = 120;                                 // 초기 타이머 시간 (초 단위)
    private static int connectedClients = 0;                                // 현재 접속한 클라이언트 수
    private static boolean timerRunning = false;                            // 타이머 실행 여부
    private static String randomTopic;

    // 배경음악 재생을 위한 필드
    private static Clip clip;
    private static boolean isPlaying = false;


    public static void main(String[] args) {
        System.out.println("채팅 서버 시작 중...");
        randomTopic = RandomTopic();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                // 연결 수락한 클라이언트 -> 새 Thread로 처리
                new ClientHandler(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 타이머 구현
    private static synchronized void startTimer(Map<String, Integer> scores) {
        if (!timerRunning && connectedClients >= 1) {
            timerRunning = true;
            startBackgroundMusic();
            System.out.println("타이머 시작!");
            new Timer().scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if (timeRemaining > 0) {
                        timeRemaining--;
                        broadcast("Timer " + timeRemaining);
                    } else {
                        saveScoresToDB();
                        stopBackgroundMusic();
                        broadcast("Timer 종료");
                        timeRemaining = 120;  // 초기화
                        timerRunning = false;  // 타이머 상태 초기화
                        cancel();
                        new GameOver(scores);  // GameOver 호출
                    }
                }
                // 배경음악 정지 메서드
                private void stopBackgroundMusic() {
                    if (clip != null && clip.isRunning()) {
                        clip.stop();
                        isPlaying = false;
                    }
                }

                private void saveScoresToDB() {
                    Connection conn = null;
                    PreparedStatement pstmt = null;

                    try {
                        conn = DBConnection.getConnection();
                        String query = "INSERT INTO rankingtable (member_id, score) VALUES (?, ?) ON DUPLICATE KEY UPDATE score = ?";
                        pstmt = conn.prepareStatement(query);

                        // scores 맵에 있는 모든 사용자 점수 DB에 삽입
                        for (Map.Entry<String, Integer> entry : scores.entrySet()) {
                            String memberId = entry.getKey();
                            int score = entry.getValue();

                            pstmt.setString(1, memberId);
                            pstmt.setInt(2, score);
                            pstmt.setInt(3, score);  // 업데이트하려면 동일한 값을 다시 설정

                            pstmt.executeUpdate();  // 쿼리 실행
                        }

                    } catch (SQLException e) {
                        System.out.println("DB에 점수 저장 중 오류 발생");
                        e.printStackTrace();
                    } finally {
                        try {
                            if (pstmt != null) pstmt.close();
                            if (conn != null) conn.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, 0, 1000);
        }
    }

    private static void startBackgroundMusic() {
        if (!isPlaying) {
            try {
                File musicFile = new File("./music/BGM.wav"); // MP3를 WAV로 변환하여 사용
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicFile);
                clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.loop(Clip.LOOP_CONTINUOUSLY); // 반복 재생
                isPlaying = true;
            } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
                e.printStackTrace();
                System.out.println("음악 재생 중 오류 발생");
            }
        }
    }

    // 게임이 끝나고 남은 시간 초기화
    private static void resetTime() {
        timeRemaining = 120;
    }

    // 랜덤한 주제 뽑기
    private static String RandomTopic() {
        return TOPICS[new Random().nextInt(TOPICS.length)];
    }

    // 뽑은 랜덤 주제 클라이언트에 전송
    private static void sendTopicToClients() {
        for(PrintWriter writer : clientWriters) {
            writer.println("랜덤 주제 : " + randomTopic);
        }
    }

    private static void broadcast(String message) {
        synchronized (clientWriters) {
            for (PrintWriter writer : clientWriters) {
                writer.println(message);
            }
        }
    }

    // 여러 클라이언트 동시 처리 가능하도록
    private static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;        // 출력 스트림
        private String name;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                // 이름 설정 및 초기 점수 등록
                this.name = in.readLine();    // 클라이언트로부터 이름 받기
                scores.put(this.name, 1000);  // 초기 점수 설정

                synchronized (clientWriters) {
                    clientWriters.add(out);
                }

                synchronized(ChatServer.class) {
                    connectedClients++;
                    if(connectedClients == 2) {
                        startTimer(scores);
                    }
                }

                // 새 클라이언트 접속 시 주제 보내기
                out.println("randomTopic " + randomTopic);

                String message;
                while ((message = in.readLine()) != null) {     // 한줄씩 읽어옴
                    if (message.startsWith("/forbidden ")) {
                        String[] parts = message.split(" ", 2);
                        if (parts.length == 2) {
                            forbiddenWords.put(this.name, parts[1]);    // map에 저장
                        }
                    } else {
                        broadcast(message);
                        checkForbiddenWords(message,this.name);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                synchronized (clientWriters) {
                    clientWriters.remove(out);
                    scores.remove(this.name);
                    forbiddenWords.remove(this.name);
                }
                synchronized (ChatServer.class) {
                    connectedClients--;
                }
            }
        }

        private void checkForbiddenWords(String message, String sender) {
            Iterator<Map.Entry<String, String>> iterator = forbiddenWords.entrySet().iterator();

            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                if (message.contains(entry.getValue()) && !entry.getKey().equals(sender)) {
                    // 메시지에 금칙어가 포함되어 있고, 메시지를 보낸 사람(sender)이 금칙어의 주인(entry.getKey)이 아닐 경우
                    int newScore = scores.get(sender) - 80;  // 메시지 보낸 사람의 점수 감점
                    scores.put(sender, newScore);
                    System.out.println(sender + "의 점수가 80점 감점되어 " + newScore + "점이 되었습니다.");
                }
            }
        }

        private void broadcast(String message) {
            for (PrintWriter writer : clientWriters) {
                writer.println(message);
            }
        }

    }
}
