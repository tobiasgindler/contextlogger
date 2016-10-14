package io.tracee.contextlogger.agent.metrics;

import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class MeasuredMetricReporter {

	private static MetricRegistry metricRegistry = new MetricRegistry();

	public static void startJmxReporter() {
		log.info("Init JMX reporter");

		JmxReporter jmxReporter = JmxReporter
				.forRegistry(metricRegistry)
				.convertDurationsTo(TimeUnit.MILLISECONDS)
				.convertRatesTo(TimeUnit.MINUTES)
				.build();
		jmxReporter.start();

	}

	public static void reportTime(String name, long timeInMs) {
		Timer timer = metricRegistry.timer(name);
		timer.update(timeInMs, TimeUnit.MILLISECONDS);
	}

}
