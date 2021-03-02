package academy.kovalevskyi.javadeepdive.week1.day2;

public record HttpResponse(ResponseStatus status, ContentType contentType, String body, HttpVersion httpVersion) {

    public static final  HttpResponse ERROR_404 = new Builder().status(ResponseStatus.ERROR_404).build();
    public static final  HttpResponse OK_200 = new Builder().status(ResponseStatus.OK).build();
    public static final HttpResponse ERROR_500 = new Builder().status(ResponseStatus.ERROR_500).build();

    public static class Builder {
        private ResponseStatus status = ResponseStatus.OK;
        private ContentType contentType = ContentType.TEXT_HTML;
        private String body;
        private HttpVersion httpVersion = HttpVersion.HTTP_1_1;

        public HttpResponse build() {
            return new HttpResponse(this.status, this.contentType, this.body, this.httpVersion);
        }
        public Builder status(ResponseStatus status) {
            this.status = status;
            return this;
        }
        public Builder contentType(ContentType contentType) {
            this.contentType = contentType;
            return this;
        }
        public Builder body(String body) {
            this.body = body;
            return this;
        }
        public Builder httpVersion(HttpVersion version) {
            this.httpVersion = version;
            return this;
        }
    }

    public enum ResponseStatus {
        OK(200, "OK"), ERROR_404(404, "not found"), ERROR_500(500, "server error");
        private int code;
        private String message;

        ResponseStatus(int code, String message) {
            this.code = code;
            this.message = message;
        }

        @Override
        public String toString() {
            return code + " " + message;
        }
    }
}