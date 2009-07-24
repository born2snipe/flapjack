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
import java.io.OutputStream;


public class StreamRecordWriter implements RecordWriter {
    private FileUtil fileUtil = new FileUtilImpl();
    private OutputStream output;

    public StreamRecordWriter(OutputStream output) {
        if (output == null) {
            throw new IllegalArgumentException("Null stream given");
        }
        this.output = output;
    }

    public void write(byte[] data) throws IOException {
        output.write(data);
        output.flush();
    }

    public void close() {
        fileUtil.close(output);
    }
}
