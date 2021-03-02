package academy.kovalevskyi.javadeepdive.week1.day2;

public enum ContentType {
    TEXT_HTML("text/html"),
    APPLICATION_JSON("application/json");

    String contentTypeValue;

    ContentType(String contentTypeValue) {
        this.contentTypeValue = contentTypeValue;
    }

    @Override
    public String toString() {
        return this.contentTypeValue;
    }
}