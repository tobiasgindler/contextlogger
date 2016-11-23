package io.tracee.cltest.agenttest;

import io.tracee.contextlogger.agent.api.Measured;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class AgentTestApp {

	private NoTracking noTracking = new NoTracking();

	public void throwsRuntimeException(String x, String y) {
		noTracking.wtf();
		throw new NullPointerException("TATA");
	}


	public String measuredByConfig() {

		try {
			throwsRuntimeException("a", "b");
		} catch (Exception e) {
			// do nothing
		}

		noTracking.notAgain("X");

		return "OK";
	}

	@Measured
	public String doStuff(int i) {
		System.out.println("CALL " + i);
		log.info("Invoked doStuff with {}", i);
		if (true == true) {
			//throw new NullPointerException();
		}
		return "T" + 1;
	}


	public static void main(String[] args) throws Exception {

		AgentTestApp instance = new AgentTestApp();

		for (int i = 0; i < 100; i++) {

			instance.doStuff(i);
			instance.measuredByConfig();

			Thread.sleep(1000);

		}


	}
}
