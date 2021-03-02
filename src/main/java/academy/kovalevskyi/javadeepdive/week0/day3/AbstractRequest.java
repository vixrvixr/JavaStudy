package academy.kovalevskyi.javadeepdive.week0.day3;
import academy.kovalevskyi.javadeepdive.week0.day2.CSV;

public abstract class AbstractRequest<T> {
    protected CSV cvs;

    protected AbstractRequest(CSV target) {
        this.cvs = target;
    }

    protected abstract T execute() throws RequestException;

    protected static void copyValues(final String[][] sourceValues, String[][] destValues)
    {
        for ( int i = 0 ; i < sourceValues.length; i++)
        {
            System.arraycopy(sourceValues[i], 0, destValues[i], 0, sourceValues[i].length);
        }
    }
}
