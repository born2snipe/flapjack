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

import flapjack.example.model.User;
import flapjack.io.LineRecordReaderFactory;
import flapjack.layout.SimpleRecordLayout;
import flapjack.model.ObjectMapping;
import flapjack.model.ObjectMappingStore;
import flapjack.model.RecordFactory;
import flapjack.model.SameRecordFactoryResolver;
import flapjack.parser.FlatFileParser;
import flapjack.parser.ParseResult;
import flapjack.parser.RecordParserImpl;
import flapjack.parser.SameRecordLayoutResolver;
import junit.framework.TestCase;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class FileTest extends TestCase {
	public void test() throws URISyntaxException, IOException {
		// The file to be parsed
		File file = new File(Thread.currentThread().getContextClassLoader().getResource("test.txt").toURI());

		/**
		 * Configure the ObjectMapping from the record data to the domain objects
		 */
		ObjectMapping userMapping = new ObjectMapping(User.class);
		userMapping.add("First Name", "firstName");
		userMapping.add("Last Name", "lastName");
		userMapping.add("Username", "userName");

		ObjectMappingStore objectMappingStore = new ObjectMappingStore();
		objectMappingStore.add(userMapping);

		/**
		 * Initialize the RecordParser with our RecordLayoutResolver and RecordFactoryResolver
		 */
		RecordParserImpl recordParser = new RecordParserImpl();
		recordParser.setRecordLayoutResolver(new SameRecordLayoutResolver(new UserRecordLayout()));
		recordParser.setRecordFactoryResolver(new SameRecordFactoryResolver(UserRecordFactory.class));
		recordParser.setObjectMappingStore(objectMappingStore);
		recordParser.setIgnoreUnmappedFields(true);

		FlatFileParser parser = new FlatFileParser();
		parser.setRecordReaderFactory(new LineRecordReaderFactory());
		parser.setRecordParser(recordParser);

		ParseResult result = parser.parse(file);

		/**
		 * Verify the contents read from the records have not been altered
		 */
		assertNotNull(result);
		assertEquals(0, result.getPartialRecords().size());
		assertEquals(0, result.getUnparseableRecords().size());
		assertEquals(0, result.getUnresolvedRecords().size());
		assertEquals(2, result.getRecords().size());

		User user1 = (User) result.getRecords().get(0);
		assertEquals("Joe        ", user1.getFirstName());
		assertEquals("Schmoe     ", user1.getLastName());
		assertEquals("jschmoe111 ", user1.getUserName());

		User user2 = (User) result.getRecords().get(1);
		assertEquals("Jimmy      ", user2.getFirstName());
		assertEquals("Smith      ", user2.getLastName());
		assertEquals("jsmith     ", user2.getUserName());
	}


	/**
	 * These RecordLayouts represent the different possible record types that should be encounted in out data
	 */
	private static class UserRecordLayout extends SimpleRecordLayout {
		private UserRecordLayout() {
            super("user");
			field("First Name", 11);
			field("Last Name", 11);
			field("Username", 11);
			field("Terminator", 1);
		}
	}


	/**
	 * This class is responsible for creating the POJO that represents the given record.
	 */
	private static class UserRecordFactory implements RecordFactory {
		public Object build() {
			return new User();
		}
	}
}
