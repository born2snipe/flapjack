/**
 * Copyright 2008-2009 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License. 
 */
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
