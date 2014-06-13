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
package flapjack.example;

import flapjack.annotation.Field;
import flapjack.annotation.Record;
import flapjack.annotation.model.AnnotatedObjectMappingStore;
import flapjack.io.LineRecordReader;
import flapjack.layout.RecordLayout;
import flapjack.layout.SimpleRecordLayout;
import flapjack.model.RecordFactory;
import flapjack.model.SameRecordFactoryResolver;
import flapjack.parser.ParseResult;
import flapjack.parser.RecordParserImpl;
import flapjack.parser.SameRecordLayoutResolver;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;


public class UseAnnotationTest {
    @Test
    public void test() throws IOException {
        String records = "Coldplay  Clocks    03:45";

        /**
         * Initialize the AnnotatedObjctMappingStore with what packages need to be scanned for the domain classes
         * that contain the annotations.
         */
        AnnotatedObjectMappingStore objectMappingStore = new AnnotatedObjectMappingStore();
        objectMappingStore.setPackages(Arrays.asList("flapjack.example"));

        /**
         * Initialize the RecordParser with our ObjectMappingStore
         */
        RecordParserImpl recordParser = new RecordParserImpl();
        recordParser.setRecordLayoutResolver(new SameRecordLayoutResolver(new SongRecordLayout()));
        recordParser.setRecordFactoryResolver(new SameRecordFactoryResolver(SongFactory.class));
        recordParser.setObjectMappingStore(objectMappingStore);

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
            super("song");
            field("Artist", 10);
            field("Title", 10);
            field("Length of Song", 5);
        }
    }


    /**
     * Our domain class to be used with the annotations telling what fields should be mapped.
     * <p/>
     * The names you give the the @Field annotation are very IMPORTANT they should match the descriptions
     * you have defined in your RecordLayout you have defined in the @Record annotation.
     */
    @Record("song")
    public static class Song {
        @Field
        private String artist;
        @Field
        private String title;
        @Field("Length of Song")
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

    private static class SongFactory implements RecordFactory {
        public Object build(RecordLayout recordLayout) {
            return new Song();
        }
    }
}
