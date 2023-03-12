package org.acme.example;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.ImmutableTag;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import jdk.jfr.consumer.EventStream;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {
	public static final String JDK_GCHEAP_SUMMARY = "jdk.GCHeapSummary";
	public static final String JDK_CPULOAD = "jdk.CPULoad";
	public static final String JDK_GARBAGE_COLLECTION = "jdk.GarbageCollection";

	public static final String NAME = "name";
	public static final String CAUSE = "cause";
	public static final String HEAP_USED = "heapUsed";
	public static final String SUM_OF_PAUSES = "sumOfPauses";
	public static final String LONGEST_PAUSE = "longestPause";
	public static final String MACHINE_TOTAL = "machineTotal";
	public static final String JVM_USER = "jvmUser";
	public static final String JVM_SYSTEM = "jvmSystem";


	/**
	 * This event is executed as late as conceivably possible to indicate that
	 * the application is ready to service requests.
	 */
	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		CompositeMeterRegistry metricsRegistry = Metrics.globalRegistry;

		try (var es = EventStream.openRepository()) {
			extractJDKCpuLoadToGauge(metricsRegistry, es);

			extractJDKGCToGauge(metricsRegistry, es);

			es.onEvent(JDK_GCHEAP_SUMMARY, recordedEvent -> {
				Gauge.builder(JDK_GCHEAP_SUMMARY + HEAP_USED, recordedEvent, e -> e.getLong(HEAP_USED))
						.description("Bytes allocated by objects in the heap").register(metricsRegistry);
			});
			es.start();
		} catch (IOException e) {
			throw new RuntimeException("Couldn't process event", e);
		}
	}

	private static void extractJDKGCToGauge(CompositeMeterRegistry metricsRegistry, EventStream es) {
		es.onEvent(JDK_GARBAGE_COLLECTION, recordedEvent -> {
			List<Tag> tags = Arrays.asList(new ImmutableTag(NAME, recordedEvent.getString(NAME)),
					new ImmutableTag(CAUSE, recordedEvent.getString(CAUSE)));
			Gauge.builder(JDK_GARBAGE_COLLECTION + SUM_OF_PAUSES,
							recordedEvent, e -> e.getDuration(SUM_OF_PAUSES).toMillis())
					.tags(tags).description(JDK_GARBAGE_COLLECTION + "Total pause").register(metricsRegistry);
			Gauge.builder(JDK_GARBAGE_COLLECTION + LONGEST_PAUSE,
							recordedEvent, e -> e.getDuration(LONGEST_PAUSE).toMillis())
					.tags(tags).description(JDK_GARBAGE_COLLECTION + " Longest pause").register(metricsRegistry);
		});
	}

	private static void extractJDKCpuLoadToGauge(CompositeMeterRegistry metricsRegistry, EventStream es) {
		es.onEvent(JDK_CPULOAD, recordedEvent -> {
			Gauge.builder(JDK_CPULOAD + MACHINE_TOTAL, recordedEvent, e -> 100 * e.getFloat(MACHINE_TOTAL))
					.description(JDK_CPULOAD + "Machine total %").register(metricsRegistry);
			Gauge.builder(JDK_CPULOAD + JVM_USER, recordedEvent, e -> 100 * e.getFloat(JVM_USER))
					.description(JDK_CPULOAD + "JVM User %").register(metricsRegistry);
			Gauge.builder(JDK_CPULOAD + JVM_SYSTEM, recordedEvent, e -> 100 * e.getFloat(JVM_SYSTEM))
					.description(JDK_CPULOAD + "JVM System %").register(metricsRegistry);
		});
	}

}