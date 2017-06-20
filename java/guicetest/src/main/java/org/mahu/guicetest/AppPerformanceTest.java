package org.mahu.guicetest;

import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class AppPerformanceTest {

    public static void main(String[] args) {

        Injector injector = Guice.createInjector(new BindingModule1(), new BindingModule2());
        injector.getInstance(InjectorProxy.class).set(injector);

        final int max = 20000;
        measure("BillingService via Guice", max, () -> {
            for (int i = 0; i < max; i++) {
                BillingService1 billingService = injector.getInstance(BillingService1.class);
            }
        });

        measure("Create instance BillingService", max, () -> {
            for (int i = 0; i < max; i++) {
                ICreditCardProcessor processor = new PaypalCreditCardProcessor();
                BillingService2 billingService2 = new BillingService2(processor);
                BillingService1 billingService1 = new BillingService1(processor, billingService2);
            }
        });

        measure("Task2 via Guice", max, () -> {
            for (int i = 0; i < max; i++) {
                Task2 task2 = injector.getInstance(Task2.class);
            }
        });

        measure("ChildInjector", max, () -> {
            for (int i = 0; i < max; i++) {
                IDiagnosticsLogger diagnosticsLogger = new DiagnosticsLogger();
                Injector requestInjector = injector.createChildInjector(new AbstractModule() {
                    @Override
                    protected void configure() {
                        bind(IDiagnosticsLogger.class).toInstance(diagnosticsLogger);
                    }
                });
            }
        });

        IDiagnosticsLogger diagnosticsLogger = new DiagnosticsLogger();
        Injector requestInjector = injector.createChildInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(IDiagnosticsLogger.class).toInstance(diagnosticsLogger);
            }
        });
        measure("ChildInjector - task3", max, () -> {
            for (int i = 0; i < max; i++) {
                requestInjector.getInstance(Task3.class);
            }
        });

    }

    public static void measure(final String text, final long max, Runnable r) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        r.run();
        stopwatch.stop(); // optional
        long nanos = stopwatch.elapsed(TimeUnit.NANOSECONDS);
        System.out.println("##### " + text);
        long avgNano = nanos / max;
        long avgMicro = nanos / (max * 1000);
        long avgMilli = nanos / (max * 1000 * 1000);
        System.out.println("elapsed(nanosec)=" + nanos);
        System.out.println("avg(nanosec)=" + avgNano);
        System.out.println("avg(microsec)=" + avgMicro);
        System.out.println("avg(millisec)=" + avgMilli);
    }
}
