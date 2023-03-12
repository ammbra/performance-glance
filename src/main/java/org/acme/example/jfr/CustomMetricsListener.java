package org.acme.example.jfr;

import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebFilter;
import jdk.jfr.consumer.RecordingStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static jdk.jfr.FlightRecorder.register;

@Component
public class CustomMetricsListener implements ServletContextListener {

    private RecordingStream recordingStream;
    Logger logger = LoggerFactory.getLogger(CustomMetricsListener.class);

    @PostConstruct
    public void init() {
        register(RestEndpointInvocationEvent.class);
        logger.info("#### Registered");
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        CompositeMeterRegistry metricsRegistry = Metrics.globalRegistry;
        ServletContextListener.super.contextInitialized(sce);
        recordingStream = new RecordingStream();
        recordingStream.enable(RestEndpointInvocationEvent.NAME);
        recordingStream.onEvent(RestEndpointInvocationEvent.NAME, event -> {

            String path = event.getString("path").substring(1);
            String method = event.getString("method");
            String name = ((path.length()>1) ? (path.replaceAll("/",".") + ".") : "") + method.toLowerCase();
            Timer timer = metricsRegistry.find(name).timer();
            Objects.requireNonNullElseGet(timer, () -> {
                return Timer.builder(name).description("Metrics for " + path + " (" + method + ")").register(metricsRegistry);
            }).record(event.getDuration());
        });

        recordingStream.startAsync();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
        recordingStream.close();
        try {
            recordingStream.awaitTermination();
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
