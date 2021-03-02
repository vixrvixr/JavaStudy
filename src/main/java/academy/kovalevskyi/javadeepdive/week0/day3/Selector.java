package academy.kovalevskyi.javadeepdive.week0.day3;
public record Selector(String fieldName, String value) {

    public static class Builder {
        private String fieldName;
        private String value;

        public Builder fieldName(String fieldName) {
            this.fieldName = fieldName;
            return this;
        }

        public Builder value(String value) {
            this.value = value;
            return this;
        }

        public Selector build() {
            return new Selector(this.fieldName,this.value);
        }
    }
}
