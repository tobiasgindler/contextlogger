package io.tracee.cltest.agenttest;

import io.tracee.contextlogger.agent.api.Measured;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class AgentTestApp {


	public String measuredByConfig() {

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
