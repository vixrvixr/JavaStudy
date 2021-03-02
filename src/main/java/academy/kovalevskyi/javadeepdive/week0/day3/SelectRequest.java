package academy.kovalevskyi.javadeepdive.week0.day3;

import academy.kovalevskyi.javadeepdive.week0.day2.CSV;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelectRequest extends AbstractRequest<String[][]> {

    private Selector selector;
    private String[] columns;

    private SelectRequest(CSV csv, Selector selector, String[] columns) {
        super(csv);
        this.selector = selector;
        this.columns = columns;
    }

    @Override
    public String[][] execute() throws RequestException {

        Integer indexSelector = null;
        List<String> columnsAsList = Arrays.asList(columns);
        if (selector != null) {
            for (int i = 0; i < cvs.header().length; i++) {
                if(cvs.header()[i].equals(selector.fieldName())){
                    indexSelector = i;
                    break;
                }
            }
        }

        List<Integer> ordinal = new ArrayList<>();

        for (int i = 0; i < cvs.header().length; i++) {
            if (columnsAsList.contains(cvs.header()[i])) {
                ordinal.add(i);
            }
        }

        List<String[]> newValue = new ArrayList<>();
        String[][] values = cvs.values();
        for (String[] line : values) {
            if (indexSelector == null || line[indexSelector].equals(selector.value())) {
                List<String> newLine = new ArrayList<>();
                for (Integer j : ordinal) {
                    newLine.add(line[j]);
                }
                newValue.add(newLine.toArray(new String[0]));
            }
        }


        return newValue.toArray(new String[0][0]);
    }

    public static class Builder {

        private Selector selector;
        private CSV csv;
        private String[] columns;

        public Builder where(Selector selector) {
            this.selector = selector;
            return this;
        }

        public Builder select(String[] columns) {
            this.columns = columns;
            return this;
        }

        public Builder from(CSV csv) {
            this.csv = csv;
            return this;
        }

        public SelectRequest build() {
            return new SelectRequest(csv, selector, columns);
        }
    }
}