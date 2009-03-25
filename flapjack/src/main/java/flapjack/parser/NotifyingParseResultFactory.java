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
package flapjack.parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Should be used when you want to be notified when a record has been parsed or a bad record has been encountered while parsing.
 * This is a more ideal solution if you know you are going to be processing large amounts of data and you do not want
 * to wait for the entire file/data to be parsed before processing the records.
 */
public class NotifyingParseResultFactory implements ParseResultFactory {
    private final NotifyingParseResult results = new NotifyingParseResult();

    public ParseResult build() {
        return results;
    }

    /**
     * Register a notifier to be notified of parsing events
     *
     * @param notifier - the notifier to be notified
     */
    public void addNotifier(Notifier notifier) {
        results.addNotifier(notifier);
    }

    public static interface Notifier {
        /**
         * This method is called when a record was successfully parsed.
         *
         * @param obj - the domain object created from the record of data
         */
        void addRecord(Object obj);

        /**
         * This method is called when a record read from a file does not match lengths with the RecordLayout
         *
         * @param record - the record that was read that caused the problem
         */
        void addPartialRecord(BadRecord record);

        /**
         * This method is called when a record read from a file encoutered a ParseException
         *
         * @param record - the record that was read that caused the problem
         */
        void addUnparseableRecord(BadRecord record);

        /**
         * This method is called when a record read from a file could not resolve a RecordLayout
         *
         * @param record - the record that was read that caused the problem
         */
        void addUnresolvedRecord(BadRecord record);

    }

    private static class NotifyingParseResult extends ParseResult {
        private List notifiers = new ArrayList();

        public NotifyingParseResult() {
        }

        private void addNotifier(Notifier notifier) {
            notifiers.add(notifier);
        }

        public void addUnresolvedRecord(BadRecord record) {
            Iterator it = notifiers.iterator();
            while (it.hasNext()) {
                Notifier notifier = (Notifier) it.next();
                notifier.addUnresolvedRecord(record);
            }
        }

        public void addRecord(Object obj) {
            Iterator it = notifiers.iterator();
            while (it.hasNext()) {
                Notifier notifier = (Notifier) it.next();
                notifier.addRecord(obj);
            }
        }

        public void addPartialRecord(BadRecord record) {
            Iterator it = notifiers.iterator();
            while (it.hasNext()) {
                Notifier notifier = (Notifier) it.next();
                notifier.addPartialRecord(record);
            }
        }

        public void addUnparseableRecord(BadRecord record) {
            Iterator it = notifiers.iterator();
            while (it.hasNext()) {
                Notifier notifier = (Notifier) it.next();
                notifier.addUnparseableRecord(record);
            }
        }
    }
}
