package academy.kovalevskyi.javadeepdive.week0.day3;

import academy.kovalevskyi.javadeepdive.week0.day2.CSV;

import java.util.ArrayList;

public class DeleteRequest extends AbstractRequest<CSV> {
    private Selector whereSelector;

    private DeleteRequest(CSV target, Selector whereSelector) {
        super(target);
        this.whereSelector = whereSelector;
    }

    @Override
    //protected
    public CSV execute() throws RequestException {
        // find index of whereSelector.fieldName()
        int indexOfField = 0;
        for (int i = 0; i < cvs.header().length; i++) {
            if (cvs.header()[i].equals(whereSelector.fieldName())) {
                indexOfField = i;
                break;
            }
        }
        ArrayList<Integer> listIndexesForDelete = new ArrayList<>();
        for (int i = 0; i < cvs.values().length; i++) {
            if (cvs.values()[i][indexOfField].equals(whereSelector.value())) {
                listIndexesForDelete.add(i);
            }
        }
        int index = 0;
        int newLength = cvs.values().length - listIndexesForDelete.size();
        String[][] valuesResult = new String[newLength][cvs.values()[0].length];
        for (int i = 0; i < cvs.values().length; i++) {
            if (!listIndexesForDelete.contains(i)) {
                System.arraycopy(cvs.values()[i], 0, valuesResult[index], 0, cvs.values()[i].length);
                index++;
            }
        }
        return new CSV(cvs.header(), valuesResult);
    }

    public static class Builder {
        private Selector selector;
        private CSV csv;

        public Builder where(Selector selector) {
            this.selector = selector;
            return this;
        }

        public Builder from(CSV csv) {
            this.csv = csv;
            return this;
        }

        public DeleteRequest build() {
            return new DeleteRequest(this.csv, this.selector);
        }
    }
}