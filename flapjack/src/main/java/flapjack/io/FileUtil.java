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

import java.nio.channels.ScatteringByteChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.FileChannel;
import java.nio.ByteBuffer;
import java.io.File;


public interface FileUtil {
    ScatteringByteChannel channel(File file);
    void close(ReadableByteChannel channel);
    ByteBuffer map(ScatteringByteChannel channel, long offset, long length);
    long length(File file);
}
