import java.io.*;
import java.net.*;
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
                while ((message = in.readLine()) != null) {
                    broadcast(message);
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
    }
}
