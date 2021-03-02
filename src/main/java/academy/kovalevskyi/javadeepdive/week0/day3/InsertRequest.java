package academy.kovalevskyi.javadeepdive.week0.day3;

import academy.kovalevskyi.javadeepdive.week0.day2.CSV;

public class InsertRequest extends AbstractRequest<CSV> {

    private String[] line;

    private InsertRequest(CSV target, String[] line) {
        super(target);
        this.line = line;
    }

    @Override
    public CSV execute() throws RequestException {
        if (csv.values().length == 0) {
            String[][] val = new String[][]{{line[0],line[1],line[2],line[3]}};
            return new CSV(csv.header(), val);
        } else {
            String[][] newValues = new String[csv.values().length + 1][csv.header().length];
            copyValues(csv.values(), newValues);
            final int lastValue = csv.values().length;
            System.arraycopy(line, 0, newValues[csv.values().length], 0, lastValue);

            return new CSV(csv.header(), newValues);
        }
    }

    public static class Builder {
        String[] line;
        CSV csv;

        public Builder insert(String[] line) {
            this.line = line;
            return this;
        }

        public Builder to(CSV csv) {
            this.csv = csv;
            return this;
        }

        public InsertRequest build() {
            return new InsertRequest(this.csv, this.line);
        }
    }
}