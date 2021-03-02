package academy.kovalevskyi.javadeepdive.week1.day2;

public interface HttpRequestsHandler {
    String path();
    HttpMethod method();
    HttpResponse process(HttpRequest request);
}