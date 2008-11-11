package flapjack.io;

import java.io.IOException;

/**
 * Handles all IO with the file that is being read
 */
public interface RecordReader {

    /**
     * Attempts to read a record
     * <p/>
     * Will return null when EOF has been reached
     * <p/>
     * If a partial record is read it will return the bytes read and trim any empty slots in the array
     *
     * @return the bytes representing a record
     * @throws IOException will be thrown when ecountering any IO problems
     */
    byte[] readRecord() throws IOException;

    /**
     * Cleans up resources
     */
    void close();
}
