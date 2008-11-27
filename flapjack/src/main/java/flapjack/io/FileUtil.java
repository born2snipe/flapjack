/**
 * Copyright 2008 Dan Dudley
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

import java.io.File;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.ScatteringByteChannel;


public interface FileUtil {
    /**
     * Creates a FileChannel for the given file
     *
     * @param file - the file to create a channel on
     * @return the FileChannel
     */
    ScatteringByteChannel channel(File file);

    /**
     * Closes the given FileChannel
     *
     * @param channel - the file channel to close
     */
    void close(ReadableByteChannel channel);

    /**
     * Attempts to map a region of a FileChannel
     *
     * @param channel - the FileChannel to map
     * @param offset  - the starting point of the mapped region
     * @param length  - the number of bytes of the mapped region
     * @return the ByteBuffer representing the mapped region
     */
    ByteBuffer map(ScatteringByteChannel channel, long offset, long length);

    /**
     * The number of bytes in the file
     *
     * @param file - the file to tell the length of
     * @return the number of bytes
     */
    long length(File file);
}
