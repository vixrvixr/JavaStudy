package academy.kovalevskyi.javadeepdive.week1.day2;

public enum HttpVersion {
    HTTP_1_1("HTTP/1.1");
    String version;

    HttpVersion(String version) {
        this.version = version;
    }
    @Override
    public String toString() {
        return this.version;
    }
}