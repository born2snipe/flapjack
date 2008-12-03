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
package flapjack.example;

import flapjack.annotation.Field;
import flapjack.annotation.Record;
import flapjack.annotation.model.MappedRecordFactoryResolver;
import flapjack.annotation.parser.StringMapRecordFieldParser;
import flapjack.io.LineRecordReader;
import flapjack.layout.SimpleRecordLayout;
import flapjack.parser.ParseResult;
import flapjack.parser.RecordParserImpl;
import flapjack.parser.SameRecordLayoutResolver;
import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;


public class UseAnnotationTest extends TestCase {
    public void test() throws IOException {
        String records = "Coldplay  Clocks    03:45";

        /**
         * Initialize the MappedRecordFactoryResolver with what packages need to be scanned for the domain classes
         * that contain the annotations.
         */
        MappedRecordFactoryResolver recordFactoryResolver = new MappedRecordFactoryResolver();
        recordFactoryResolver.setPackages(Arrays.asList("flapjack.example"));

        /**
         * Initialize the RecordParser with our RecordLayoutResolver and RecordFactoryResolver
         */
        RecordParserImpl recordParser = new RecordParserImpl();
        recordParser.setRecordLayoutResolver(new SameRecordLayoutResolver(SongRecordLayout.class));
        recordParser.setRecordFactoryResolver(recordFactoryResolver);
        recordParser.setRecordFieldParser(new StringMapRecordFieldParser());

        /**
         * Actually call the parser with our RecordReader
         */
        LineRecordReader recordReader = new LineRecordReader(new ByteArrayInputStream(records.getBytes()));
        ParseResult result = recordParser.parse(recordReader);

        assertEquals(0, result.getUnparseableRecords().size());
        assertEquals(0, result.getUnresolvedRecords().size());
        assertEquals(0, result.getPartialRecords().size());
        assertEquals(1, result.getRecords().size());

        Song song = (Song) result.getRecords().get(0);
        assertEquals("Coldplay  ", song.getArtist());
        assertEquals("Clocks    ", song.getTitle());
        assertEquals("03:45", song.getLength());
    }

    /**
     * Our RecordLayout definition for our record type
     */
    private static class SongRecordLayout extends SimpleRecordLayout {
        private SongRecordLayout() {
            field("Artist", 10);
            field("Title", 10);
            field("Length", 5);
        }
    }


    /**
     * Our domain class to be used with the annotations telling what fields should be mapped.
     * <p/>
     * The names you give the the @Field annotation are very IMPORTANT they should match the descriptions
     * you have defined in your RecordLayout you have defined in the @Record annotation.
     */
    @Record(SongRecordLayout.class)
    public static class Song {
        @Field
        private String artist;
        @Field
        private String title;
        @Field
        private String length;

        public String getArtist() {
            return artist;
        }

        public void setArtist(String artist) {
            this.artist = artist;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getLength() {
            return length;
        }

        public void setLength(String length) {
            this.length = length;
        }
    }
}
