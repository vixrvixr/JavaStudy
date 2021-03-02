package academy.kovalevskyi.javadeepdive.week0.day2;
import java.util.Arrays;
import java.util.Objects;

public record CSV(String[] header, String[][] values) {

    public static class Builder {
        private String[] header = new String[0];
        private String[][] values = new String[0][0];

        public Builder header(String[] header) {
            this.header = header;
            return this;
        }

        public Builder values(String[][] values) {
            this.values = values;
            return this;
        }

        public CSV build() {
            return new CSV(this.header, this.values);
        }
    }

    public boolean withHeader() {
        return (header != null && header.length != 0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (getClass() != o.getClass()) { // remember about parent
            return false;
        }

        CSV other = (CSV) o;
        if (withHeader() && !Arrays.equals(this.header, other.header)) {
                return false;
        }

        return Arrays.equals(this.values, other.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(header, values);
    }
}
