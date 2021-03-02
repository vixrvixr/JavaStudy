package academy.kovalevskyi.javadeepdive.week0.day3;

import academy.kovalevskyi.javadeepdive.week0.day2.CSV;

import java.util.ArrayList;
import java.util.List;

public class JoinRequest extends AbstractRequest<CSV> {

    private CSV from;
    private String by;

    private int indexOfFieldFrom = 0;
    private int indexOfFieldOn = 0;

    private JoinRequest(CSV from, CSV on, String by) {
        super(on);
        this.from = from;
        this.by = by;
    }

    @Override
    protected CSV execute() throws RequestException {

        int newLength = from.header().length + cvs.header().length - 1;

        indexOfFieldFrom = findIndexField(from.header());
        indexOfFieldOn =findIndexField(cvs.header());

        String[][] copyValue = from.values();

        String[][] newValue = new String[copyValue.length][newLength];

        for (int i = 0; i < cvs.values().length; i++) {
            String[] copyLine = cvs.values()[i];
            String valueFind = copyLine[indexOfFieldOn];
            String[] findedLine = new String[0];
            for (String[] line : copyValue) {
                if (line[indexOfFieldFrom].equals(valueFind)) {
                    findedLine = line;
                    break;
                }
            }
            newValue[i] = copyLine(copyLine, findedLine, valueFind);
        }
        String[] newHeader = copyLine(cvs.header(), from.header(), by);

        return new CSV(newHeader, newValue);
    }

    private int findIndexField(String[] array) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(by)) {
                return i;
            }
        }

        // не должно сюда приходить
        return 0;
    }

    public static class Builder {

        private CSV from;
        private CSV on;
        private String by;

        public Builder from(CSV from) {
            this.from = from;
            return this;
        }

        public Builder on(CSV on) {
            this.on = on;
            return this;
        }

        public Builder by(String by) {
            this.by = by;
            return this;
        }

        public JoinRequest build() {
            return new JoinRequest(from, on, by);
        }
    }

    private String[] copyLine(String[] on, String[] addedLine, String value) {
        List<String> buffer = new ArrayList<>();
        buffer.add(value);

        for (int j = 0; j < from.header().length; j++) {
            if (j != indexOfFieldFrom) {
                buffer.add(addedLine[j]);
            }
        }

        for (int j = 0; j < cvs.header().length; j++) {
            if (j != indexOfFieldOn) {
                buffer.add(on[j]);
            }
        }

        return buffer.toArray(new String[0]);

    }
}