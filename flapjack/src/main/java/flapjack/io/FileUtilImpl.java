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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.ScatteringByteChannel;


public class FileUtilImpl implements FileUtil {
    public ScatteringByteChannel channel(File file) {
        try {
            return new FileInputStream(file).getChannel();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void close(ReadableByteChannel channel) {
        try {
            channel.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ByteBuffer map(ScatteringByteChannel channel, long offset, long length) {
        if (channel instanceof FileChannel) {
            FileChannel fileChannel = (FileChannel) channel;
            try {
                return fileChannel.map(FileChannel.MapMode.READ_ONLY, offset, length);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        throw new IllegalArgumentException("Channel is NOT a FileChannel it is a " + channel.getClass().getName());
    }

    public long length(File file) {
        return file.length();
    }
}
