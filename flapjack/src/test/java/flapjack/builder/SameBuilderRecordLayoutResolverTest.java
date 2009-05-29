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
package flapjack.builder;

import flapjack.layout.SimpleRecordLayout;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;


public class SameBuilderRecordLayoutResolverTest extends TestCase {
    public void test_resolve_SingleRecordLayout() {
        SimpleRecordLayout recordLayout = new SimpleRecordLayout("id");

        SameBuilderRecordLayoutResolver resolver = new SameBuilderRecordLayoutResolver(recordLayout);
        assertEquals(Arrays.asList(new Object[]{recordLayout}), resolver.resolve("blah"));
    }

    public void test_resolve_MultipleRecordLayouts() {
        SimpleRecordLayout recordLayout = new SimpleRecordLayout("1");
        SimpleRecordLayout recordLayout2 = new SimpleRecordLayout("2");
        List recordLayouts = Arrays.asList(new Object[]{recordLayout, recordLayout2});

        SameBuilderRecordLayoutResolver resolver = new SameBuilderRecordLayoutResolver(recordLayouts);
        assertSame(recordLayouts, resolver.resolve("blah"));
    }
}
