package academy.kovalevskyi.javadeepdive.week1.day1;

import academy.kovalevskyi.javadeepdive.week1.day0.HttpRequestsHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// run - в вечном цикле ожидает новых HTTP запросов (на localhost:8080)
// stop - прерывает цикл ожидания запросов и останавливает сервер
// isLive - возвращает статус сервера, запущен он или нет
// main - запускает в фоновом потоке сервер и ожидает с stdin
//        слово “stop”, получив которое он останавливает сервер

public class ConcurrentHttpServer implements Runnable {
    private ServerSocket serverSocket;
    private ExecutorService executorService;

    public static void main(String[] args) {
        // scanner for read line from System.in
        Scanner scanner = new Scanner(System.in);

        // create HttpServer in another thread
        ConcurrentHttpServer httpServer = new ConcurrentHttpServer();
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

    public ConcurrentHttpServer() {
        this.executorService = Executors.newCachedThreadPool();
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
                this.executorService.submit(new ExecuteRequest(httpRequestsHandler));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        this.executorService.shutdown();
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isLive() {
        return !this.serverSocket.isClosed();
    }

    private class ExecuteRequest implements Runnable {
        private HttpRequestsHandler httpRequestsHandler;

        ExecuteRequest(HttpRequestsHandler httpRequestsHandler) {
            this.httpRequestsHandler = httpRequestsHandler;
        }

        @Override
        public void run() {
            this.httpRequestsHandler.executeRequest();
        }
    }
}