package academy.kovalevskyi.javadeepdive.week1.day2;

import academy.kovalevskyi.javadeepdive.week0.day0.StdBufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class HttpRequestHelper {
    private static final String NEW_LINE = "\r\n";
    private static final String INPUT_2_NEW_LINE = "\r\n\r\n";
    private static final int REQUEST_LINE_INDEX = 0;
    private static final int REQUEST_LINE_PARAMS_LIMIT = 3;
    private static final int HEADER_HOST_PARAMS_LIMIT = 2;
    private static final int HEADER_CONTENT_LENGTH_PARAMS_LIMIT = 2;
    private static final int METHOD_INDEX = 0;
    private static final int PATH_INDEX = 1;
    private static final int VERSION_INDEX = 2;

    private final OutputStream outputStream;
    private final StdBufferedReader bufferedReader;
    private int contentLength = 0;

    public HttpRequestHelper(Socket socket) throws IOException {
        this.outputStream = socket.getOutputStream();
        this.bufferedReader = new StdBufferedReader(new InputStreamReader(socket.getInputStream()));
    }

        HttpRequest deserializeFromInputStream() throws IOException {
        int index = 0;
        String[] requestString = {"GET", "/", "HTTP/1.1"};
        String[] stringWithHost;
        String[] stringWithContentLength;
        while (true) {
            String result = new String(bufferedReader.readLine());

            if (result.trim().length() == 0) {
                break;
            }
            if (index == REQUEST_LINE_INDEX) {
                requestString = result.split(" ", REQUEST_LINE_PARAMS_LIMIT);
                requestString[VERSION_INDEX] =
                        requestString[VERSION_INDEX].substring(0,requestString[VERSION_INDEX].length() - 1);
            }
            if (result.contains("Host")) {
                stringWithHost = result.split(" ", HEADER_HOST_PARAMS_LIMIT);
                stringWithHost[1] = stringWithHost[1].trim();
            }

            if (result.contains("Content-Length:")) {
                stringWithContentLength = result.split(" ", HEADER_CONTENT_LENGTH_PARAMS_LIMIT);
                stringWithContentLength[1] = stringWithContentLength[1].trim();
                this.contentLength = Integer.parseInt(stringWithContentLength[1]);
            }
            index++;
        }

        StringBuilder body = new StringBuilder();
        if (this.contentLength > 0) {
            bufferedReader.setRequestedSize(contentLength);
            body.append(bufferedReader.readLine());
        }

        return new HttpRequest.Builder()
                .method(HttpMethod.valueOf(requestString[METHOD_INDEX]))
                .path(requestString[PATH_INDEX])
                .httpVersion(HttpVersion.HTTP_1_1)
                .body(java.util.Optional.of(body.toString()))
                .build();
    }

    public void serializeToOutputStream(HttpResponse httpResponse) throws IOException {
        StringBuilder stringBuilder = new StringBuilder(httpResponse.httpVersion().toString());
        stringBuilder.append(" ");
        stringBuilder.append(httpResponse.status());
        stringBuilder.append(NEW_LINE);
        stringBuilder.append("Content-Type: ");
        stringBuilder.append(httpResponse.contentType());
        stringBuilder.append(NEW_LINE);
        stringBuilder.append("Content-Length: ");
        if (httpResponse.body() != null) {
            StringBuilder bodyBuilder = new StringBuilder(httpResponse.body());
            stringBuilder.append(bodyBuilder.toString().length());
            stringBuilder.append(INPUT_2_NEW_LINE);
            stringBuilder.append(httpResponse.body());
        } else {
            stringBuilder.append("0");
        }
        stringBuilder.append(INPUT_2_NEW_LINE);
        outputStream.write(stringBuilder.toString().getBytes());
        outputStream.flush();

    }
}
