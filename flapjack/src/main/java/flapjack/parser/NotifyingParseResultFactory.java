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
import java.util.List;
import java.util.Iterator;


public class NotifyingParseResultFactory implements ParseResultFactory {
    private static final NotifyingParseResult results = new NotifyingParseResult();

    public ParseResult build() {
        return results;
    }

    public void addNotifier(Notifier notifier) {
        results.addNotifier(notifier);
    }

    public static interface Notifier {
        void addRecord(Object obj);
        void addPartialRecord(BadRecord badRecord);
        void addUnparseableRecord(BadRecord record);
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
