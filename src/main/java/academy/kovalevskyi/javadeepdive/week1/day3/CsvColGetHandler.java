package academy.kovalevskyi.javadeepdive.week1.day3;

import academy.kovalevskyi.javadeepdive.week0.day2.CSV;
import academy.kovalevskyi.javadeepdive.week0.day3.RequestException;
import academy.kovalevskyi.javadeepdive.week0.day3.SelectRequest;
import academy.kovalevskyi.javadeepdive.week0.day3.Selector;
import academy.kovalevskyi.javadeepdive.week1.day2.*;


//public class GreetingsHandler implements HttpRequestsHandler {
//    public String path() {
//        return "/hello";
//    }
//
//    public HttpMethod method() {
//        return HttpMethod.GET;
//    }
//
//    public HttpResponse process(HttpRequest request) {
//        return (new HttpResponse.Builder()).body("<h1>hi</h1>").build();
//    }
//}

//serverThread.addHandler(new CsvColGetHandler(csv, "name", "/names"));
//serverThread.start();

public class CsvColGetHandler implements HttpRequestsHandler {
    private String path;
    private String colName;
    private String[][] answer;
    private CSV csv;

    public CsvColGetHandler(CSV csv, String colName, String path) {
        this.csv = csv;
        this.colName = colName;
        this.path = path;
    }
    @Override
    public String path() {
        return this.path;
    }
    @Override
    public HttpMethod method() {
        return HttpMethod.GET;
    }
    @Override
    public HttpResponse process(HttpRequest request) {
        try {
            answer = (new SelectRequest.Builder())
                     .select(new String[] {colName})
                     .from(this.csv)
                     .build()
                     .execute();
        } catch (RequestException e) {
            e.printStackTrace();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        for (int i = 0; i < answer.length; i++) {
            for (int j = 0; j < answer[i].length; j++) {
                stringBuilder.append("\"");
                stringBuilder.append(answer[i][j]);
                stringBuilder.append("\"");
                stringBuilder.append(",");
            }
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append("]");

        return new HttpResponse.Builder().body(stringBuilder.toString()).build();
//        var json_string = [
//        {"name":"Will","age":"12"},
//        {"name":"John","age":"29"}
//        ];
    }
}