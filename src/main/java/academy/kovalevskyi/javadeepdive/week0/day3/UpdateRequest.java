package academy.kovalevskyi.javadeepdive.week0.day3;

import academy.kovalevskyi.javadeepdive.week0.day2.CSV;

import java.util.ArrayList;
import java.util.List;

public class UpdateRequest extends AbstractRequest<CSV> {

    private Selector whereSelector;
    private Selector updateToSelector;

    private UpdateRequest(CSV target, Selector whereSelector, Selector updateToSelector) {
        super(target);
        this.whereSelector = whereSelector;
        this.updateToSelector = updateToSelector;
    }

    @Override
    protected CSV execute() throws RequestException {

        // find index of whereSelector.fieldName()
        int indexOfFieldWhere = 0;

        for (int i = 0; i < csv.header().length; i++) {
            if (csv.header()[i].equalsIgnoreCase(whereSelector.fieldName())) {
                indexOfFieldWhere = i;
                break;
            }
        }

        int indexOfFieldUpdate = 0;
        for (int i = 0; i < csv.header().length; i++) {
            if (csv.header()[i].equalsIgnoreCase(updateToSelector.fieldName())) {
                indexOfFieldUpdate = i;
                break;
            }
        }

        List<String[]> newValue = new ArrayList<>();
        for (String[] line : csv.values()) {
            if (line[indexOfFieldWhere].equalsIgnoreCase(whereSelector.value())) {
                line[indexOfFieldUpdate] = updateToSelector.value();
            }
            newValue.add(line);
        }

        return new CSV(csv.header(), newValue.toArray(new String[0][0]));
    }

    public static class Builder {

        private CSV csv;
        private Selector whereSelector;
        private Selector updateToSelector;

        public Builder where(Selector selector) {
            whereSelector = selector;
            return this;
        }

        public Builder update(Selector updateSelector) {
            this.updateToSelector = updateSelector;
            return this;
        }

        public Builder from(CSV csv) {
            this.csv = csv;
            return this;
        }

        public UpdateRequest build() {
            return new UpdateRequest(csv, whereSelector, updateToSelector);
        }
    }
}