package academy.kovalevskyi.javadeepdive.week1.day2;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConcurrentHttpServerWithPath extends Thread {
    private ServerSocket serverSocket;
    private final ExecutorService executorService;
    List<HttpRequestsHandler> httpRequestsHandlers;

    public static void main(String[] args) {
        // create HttpServer in another thread
        ConcurrentHttpServerWithPath httpServer = new ConcurrentHttpServerWithPath();
        Thread httpServerThread = new Thread(httpServer);
        httpServerThread.start();

        Scanner scanner = new Scanner(System.in);
        while (scanner.nextLine().equals("stop")) {
                httpServer.stopServer();
        }
    }

    public ConcurrentHttpServerWithPath () {
        this.executorService = Executors.newCachedThreadPool();
        httpRequestsHandlers = new CopyOnWriteArrayList<>();
        try {
            serverSocket = new ServerSocket(8080);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addHandler(HttpRequestsHandler handler) {
         httpRequestsHandlers.add(handler);
    }
    @Override
    public void run() {
        while (isLive()) {
            try {
                HttpRequestHelper httpRequestHelper = new HttpRequestHelper(serverSocket.accept());
                Runnable task = () -> {
                    try {
                        HttpRequest httpRequest = httpRequestHelper.deserializeFromInputStream();
                        HttpResponse httpResponse;
                        for (HttpRequestsHandler item: this.httpRequestsHandlers) {
                            if (httpRequest.path().equals(item.path()) && httpRequest.httpMethod().equals(item.method())) {
                                httpResponse = item.process(httpRequest);
                                httpRequestHelper.serializeToOutputStream(httpResponse);
                                return;
                            }
                        }
                        httpRequestHelper.serializeToOutputStream(HttpResponse.ERROR_404);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                };
                this.executorService.submit(task);
            } catch (IOException e) {
                e.printStackTrace();
                this.executorService.shutdown();
            }
        }
    }
    public void stopServer() {
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
}