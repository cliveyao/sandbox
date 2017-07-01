package org.mahu.guicetest;

import javax.inject.Inject;

import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

// This test case explores basic binding. See modules for details
public class GuiceBindingTest {

    static class BindingModule extends AbstractModule {
        @Override
        protected void configure() {
            bind(Data1.class);
            // Data 2 (default constructor) is not bound, but can be injected
            // Data 3 (@Inject constructor) is not bound, but is/can be injected
            // Data 4 (@Inject members) is not bound, but is/can be injected
            bind(TestObject.class);
        }
    }

    static class Data1 {
    }

    static class Data2 {
    }

    static class Data3 {
        @Inject
        Data3(Data1 data1) {

        }
    }

    static class Data4 {
        @Inject
        Data1 data1;
    }

    static class TestObject {

        @Inject
        TestObject(Data1 data1, Data1 data2, Data3 data3, Data4 data4) {
        }
    }

    @Test
    public void twestBasicBinding() throws Exception {
        Injector injector = Guice.createInjector(new BindingModule());
        injector.getInstance(TestObject.class);
    }

}
