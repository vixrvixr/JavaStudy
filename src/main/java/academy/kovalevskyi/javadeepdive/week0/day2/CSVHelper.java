package academy.kovalevskyi.javadeepdive.week0.day2;

import academy.kovalevskyi.javadeepdive.week0.day0.StdBufferedReader;

import java.io.*;
import java.util.*;

public class CSVHelper {

    private CSVHelper() {
        throw new IllegalStateException("Utility class");
    }

    private static final char DEFAULT_DELIMITER = ',';

    public static CSV parseFile(Reader reader) throws IOException {
        return parseFile(reader, false, DEFAULT_DELIMITER);
    }

    public static CSV parseFile(
            Reader reader, boolean withHeader, char delimiter
    ) throws IOException {

        if (!reader.ready()) {
            reader.close();
            return null;
        }

        try (StdBufferedReader stdBufferedReader = new StdBufferedReader(reader)) {
            List<String> headerList;
            String[] header = null;
            if (withHeader) {
                String tmp = new String(stdBufferedReader.readLine());
                headerList = List.of(tmp.split(String.valueOf(delimiter)));
                header = headerList.toArray(new String[0]);
            }

            ArrayList<String[]> valuesList = new ArrayList<>();
            while (stdBufferedReader.hasNext()) {
                String tmp = new String(stdBufferedReader.readLine());
                String[] value = tmp.split(String.valueOf(delimiter));
                valuesList.add(value);
            }

            String[][] values = valuesList.toArray(new String[0][0]);
            return (new CSV.Builder())
                    .header(header)
                    .values(values).build();
        }
    }

    public static void writeCsv(Writer writer, CSV csv, char delimiter) throws IOException {
        if (csv.header().length != 0) {
            var headerList = List.of(csv.header());
            writer.write(String.join(String.valueOf(delimiter), headerList));
            writer.write('\n');
        }
        if (csv.values().length != 0) {
            for (String[] s : csv.values()) {
                writer.write(String.join(String.valueOf(delimiter), Arrays.asList(s)));
                writer.write('\n');
            }
        }
        writer.close();
    }
}