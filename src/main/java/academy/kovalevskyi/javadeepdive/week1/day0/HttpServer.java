package academy.kovalevskyi.javadeepdive.week1.day0;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

// run - в вечном цикле ожидает новых HTTP запросов (на localhost:8080)
// stop - прерывает цикл ожидания запросов и останавливает сервер
// isLive - возвращает статус сервера, запущен он или нет
// main - запускает в фоновом потоке сервер и ожидает с stdin
//        слово “stop”, получив которое он останавливает сервер

public class HttpServer implements Runnable {
    private ServerSocket serverSocket;

    public static void main(String[] args) {
        // scanner for read line from System.in
        Scanner scanner = new Scanner(System.in);

        // create HttpServer in another thread
        HttpServer httpServer = new HttpServer();
        Thread httpServerThread = new Thread(httpServer);
        httpServerThread.start();

        // wait stop in stdin
        while (true) {
            String stop = scanner.nextLine();
            if (stop.equals("stop")) {
                httpServer.stop();
                break;
            }
        }
    }

    public HttpServer() {
        try {
            serverSocket = new ServerSocket(8080);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (isLive()) {
            try {
                HttpRequestsHandler httpRequestsHandler = new HttpRequestsHandler(serverSocket.accept());
                httpRequestsHandler.executeRequest();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isLive() {
        return !this.serverSocket.isClosed();
    }
}