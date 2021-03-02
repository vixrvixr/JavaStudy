package academy.kovalevskyi.javadeepdive.week0.day0;

import java.io.*;

/**
 * An StdBufferedReader reads lines from a character-input stream.
 * The buffer size may be specified, or the default size may be used.
 * The default is large enough for most purposes.
 *
 */
public class StdBufferedReader implements Closeable {
    private static final char LINE_FEED = '\n';
    private static final int END_OF_FILE =  -1;
    private static final int NOT_FIND = -1;
    private static final int EMPTY_STRING = 0;

    private final Reader reader;

    private static final int DEFAULT_CHAR_BUFFER_SIZE = 8192;
    private final char[] charBuffer;
    private int returnCharsByReader = 0; // amount data return by reader
    private int realBufferLength = 0; // for saving new size of buffer during after shifting it
    private int requestedSize = -1;

    /**
     * Base initialization of StdBufferedReader.
     *
     * @param reader        any reader
     * @param bufferSize    size of buffer
     */
    public StdBufferedReader(Reader reader, int bufferSize) {
        if (reader == null) {
            throw  new NullPointerException();
        }
        if (bufferSize <= 0) {
            throw new IllegalArgumentException("bufferSize <= 0");
        }

        this.reader = reader;
        charBuffer = new char[bufferSize];
    }

    public StdBufferedReader(Reader reader) {
        this(reader, DEFAULT_CHAR_BUFFER_SIZE);
    }

    public void setRequestedSize (final int requestedSize) {this.requestedSize = requestedSize; }

    /**
     * Checks if buffer have any lines.
     *
     * @return true if there is something to read from the reader
     * @throws IOException
     */
    public boolean hasNext() throws IOException {
        return updateBufferFromReader() != END_OF_FILE;
    }

    /**
     * Reads one line. A line must be terminated by any one of a line feed or by
     * reaching the end-of-file (EOF).
     *
     * @return array of chars until the separator appears
     * @throws IOException if any I/O error occurs
     */
    public char[] readLine() throws IOException {
        char[] charLine = new char[0]; // array for accumulated result
        while (hasNext()) {
            int offsetLineFeed = findLineInBuffer(charBuffer, this.returnCharsByReader);
            charLine = buildLine(charLine, offsetLineFeed);
            if (offsetLineFeed >= EMPTY_STRING || requestedSize == charLine.length) {
                break;
            }
        }
        return charLine;
    }

    public void close() throws IOException {
        this.reader.close();
    }

    private int findLineInBuffer(final char[] buffer, final int to) {
        for (int i = 0; i < to; i++) {
            if (buffer[i] == LINE_FEED) {
                return i;
            }
        }

        return NOT_FIND;
    }

    private char[] expandCharLine(char[] line, final int additionalSize) {
        /**
         * somethings in buffer soy append new chars from charBuffer
         * copy old part of string with System.arraycopy
         * copy new part of string System.arraycopy
         */
        char[] tBuffer = new char[line.length + additionalSize];
        System.arraycopy(line, 0,
                tBuffer, 0,
                line.length);
        System.arraycopy(charBuffer, 0,
                tBuffer, tBuffer.length - additionalSize,
                additionalSize);
        return tBuffer;
    }

    private void shiftBuffer(final int shift) {
        this.returnCharsByReader --;
        System.arraycopy(charBuffer, shift + 1,
                charBuffer, 0,
                returnCharsByReader);
        realBufferLength = charBuffer.length - this.returnCharsByReader;
    }

    private char[] buildLine(char[] inCharLine, final int offsetLF) {
        if (offsetLF == EMPTY_STRING) {
            shiftBuffer(offsetLF);
        } else if (offsetLF == NOT_FIND) {
            inCharLine = expandCharLine(inCharLine, this.returnCharsByReader);
            realBufferLength = 0;
        } else {
            inCharLine = expandCharLine(inCharLine, offsetLF);
            this.returnCharsByReader -= offsetLF;
            if (this.returnCharsByReader == 1) { // last '\n'
                realBufferLength = 0;
            } else {
                shiftBuffer(offsetLF);
            }
        }
        return inCharLine;
    }

    private int updateBufferFromReader() throws IOException {
        if (realBufferLength == 0) {
            returnCharsByReader = reader.read(charBuffer,
                    0,
                    charBuffer.length);
            realBufferLength = returnCharsByReader;
        }
        return returnCharsByReader;
    }

}