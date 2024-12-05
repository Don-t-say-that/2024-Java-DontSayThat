import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServer {
    private static final int PORT = 8000;
    private static Map<String, Integer> scores = new HashMap<>();
    private static Map<String, String> forbiddenWords = new HashMap<>();

    private static Set<PrintWriter> clientWriters = new HashSet<>();

    public static void main(String[] args) {
        System.out.println("채팅 서버 시작 중...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                new ClientHandler(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
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
                this.name = in.readLine();
                scores.put(this.name, 1000);  // 초기 점수 설정

                synchronized (clientWriters) {
                    clientWriters.add(out);
                }

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

        // DB에 점수 저장 함수
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
    }
}
