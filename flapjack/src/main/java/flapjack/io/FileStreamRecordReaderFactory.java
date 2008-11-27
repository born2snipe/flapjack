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
import java.io.IOException;


public class FileStreamRecordReaderFactory implements RecordReaderFactory {
    private int recordLength;

    public FileStreamRecordReaderFactory(int recordLength) {
        this.recordLength = recordLength;
    }

    public RecordReader build(File file) {
        StreamRecordReader recordReader = null;
        try {
            recordReader = new StreamRecordReader(new FileInputStream(file));

            recordReader.setRecordLength(recordLength);
            return recordReader;
        } catch (IOException err) {
            throw new RuntimeException(err);
        }

    }
}
