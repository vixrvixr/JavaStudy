package academy.kovalevskyi.javadeepdive.week1.day0;

import academy.kovalevskyi.javadeepdive.week0.day0.StdBufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class HttpRequestsHandler {
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private StdBufferedReader bufferedReader;
    private Request request;

    private static final String RESPONSE_BODY = "<b>It works!</b>\r\n\r\n";
    private static final int RESPONSE_BODY_LENGTH = RESPONSE_BODY.length();
    private static final int REQUEST_LINE_INDEX = 0;
    private static final int REQUEST_LINE_PARAMS_COUNT = 3;
    private static final int HEADER_HOST_PARAMS_COUNT = 2;
    private static final int METHOD_INDEX = 0;
    private static final int PATH_INDEX = 1;
    private static final int VERSION_INDEX = 2;

    public HttpRequestsHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.outputStream = socket.getOutputStream();
        this.inputStream = socket.getInputStream();
        this.bufferedReader = new StdBufferedReader(new InputStreamReader(this.inputStream));
    }

    public void executeRequest() {
        try {
            readInputHeaders();
            writeResponse(RESPONSE_BODY);
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                this.socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /*
    Данный класс должен парсить HTTP запрос и выводить на
    stdout (в любой форме):
    HTTP метод , путь , версию HTTP , хост
    */
    private void readInputHeaders() throws IOException {
        int index = 0;
        String[] requestString = new String[REQUEST_LINE_PARAMS_COUNT];
        String[] stringWithHost = new String[HEADER_HOST_PARAMS_COUNT];
        while (true) {
            String result = new String(bufferedReader.readLine());
            if (result.trim().length() == 0) {
                break;
            }
            if (index == REQUEST_LINE_INDEX) {
                requestString = result.split(" ", 3);
                for (String s: requestString) {
                    s = s.trim();
                }
            }
            if (result.contains("Host")) {
                stringWithHost = result.split(" ",2);
                stringWithHost[1] = stringWithHost[1].trim();
            }
            index++;
        }
        this.request = (new Request.Builder())
                .method(requestString[METHOD_INDEX])
                .path(requestString[PATH_INDEX])
                .version(requestString[VERSION_INDEX])
                .host(stringWithHost[1]).build();

        System.out.println("HTTP Method - " + this.request.method);
        System.out.println("Path        - " + this.request.path);
        System.out.println("Version     - " + this.request.version);
        System.out.println("Host        - " + this.request.host);
    }
    /*
    А также сформировать HTTP ответ (как минимум):
    с хедерами:
    Content-Length , Content-Type , и телом: “<b>It works!</b>”
    */
    private void writeResponse(String s) throws IOException {
        String response = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/html\r\n" +
                "Content-Length: " + RESPONSE_BODY_LENGTH + "\r\n\r\n";

        String result = response + RESPONSE_BODY;
        outputStream.write(result.getBytes());
        outputStream.flush();
    }

    public record Request(String method, String path, String version, String host) {

        public static class Builder {
            private String method;
            private String path;
            private String version;
            private String host;

            public Builder method(String method) {
                this.method = method;
                return this;
            }

            public Builder path(String path) {
                this.path = path;
                return this;
            }

            public Builder version(String version) {
                this.version = version;
                return this;
            }

            public Builder host(String host) {
                this.host = host;
                return this;
            }

            public Request build() {
                return new Request(this.method, this.path,this.version, this.host);
            }
        }
    }
}