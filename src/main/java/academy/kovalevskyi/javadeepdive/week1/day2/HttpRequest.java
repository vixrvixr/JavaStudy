package academy.kovalevskyi.javadeepdive.week1.day2;
import java.util.Optional;

public record HttpRequest(String path, HttpMethod httpMethod, Optional<String> body, ContentType contentType, HttpVersion httpVersion) {

    public static class Builder {
        private String path;
        private HttpMethod method;
        private Optional<String> body;
        private ContentType contentType;
        private HttpVersion httpVersion;

        public Builder path(String path) {
            this.path = path;
            return this;
        }
        public Builder method(HttpMethod method) {
            this.method = method;
            return this;
        }
        public Builder body(Optional<String> body) {
            this.body = body;
            return this;
        }
        public Builder contentType(ContentType contentType) {
            this.contentType = contentType;
            return this;
        }
        public Builder httpVersion(HttpVersion httpVersion) {
            this.httpVersion = httpVersion;
            return this;
        }
        public HttpRequest build() {
            return new HttpRequest(this.path, this.method, this.body, this.contentType, this.httpVersion);
        }
    }
}