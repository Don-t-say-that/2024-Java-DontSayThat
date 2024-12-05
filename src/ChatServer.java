import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServer {
    private static final int PORT = 8000;
    private static final String[] TOPICS = {
            "음식", "영화","학교", "급식", "TMI", "좋아하는 것", "과거", "미래", "현재", "이상형", "영화",
            "동물", "여행", "과목", "기분", "크리스마스", "연말", "직업", "장래희망", "밈", "첫 데이트"
    };
    private static Map<String, Integer> scores = new HashMap<>();           // 점수 관리
    private static Map<String, String> forbiddenWords = new HashMap<>();    // 금칙어 저장
    private static Set<PrintWriter> clientWriters = new HashSet<>();        // 출력 스트림 저장
    private static int timeRemaining = 120;                                 // 초기 타이머 시간 (초 단위)
    private static int connectedClients = 0;                                // 현재 접속한 클라이언트 수
    private static boolean timerRunning = false;                            // 타이머 실행 여부
    private static String randomTopic;

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
    private static synchronized void startTimer() {
        if (!timerRunning && connectedClients >= 1) {
            timerRunning = true;
            System.out.println("타이머 시작!");
            new Timer().scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if (timeRemaining > 0) {
                        timeRemaining--;
                        broadcast("Timer " + timeRemaining);
                    } else {
                        broadcast("Timer 종료");
                        timeRemaining = 120;  // 초기화
                        timerRunning = false;  // 타이머 상태 초기화
                        cancel();
                    }
                }
            }, 0, 1000);
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
                        startTimer();
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
    }
}
