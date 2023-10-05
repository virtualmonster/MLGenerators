package customcode;

import com.ibm.rational.test.lt.kernel.engine.impl.Engine;
import com.ibm.rational.test.lt.kernel.services.ITestExecutionServices;

import perf.generate.Delay;
import perf.generate.Delay.Profile;
import perf.generate.Delay.Stage;

public class GetDelay implements com.ibm.rational.test.lt.kernel.custom.ICustomCode2 {

	static Delay delay;
	static {
		delay = new Delay(new Profile() //
				.add(Stage.builder().fromUsers(0).toUsers(100).minMs(200L).minMs(1000L).build()));
	}

	public GetDelay() {
	}

	public String exec(ITestExecutionServices tes, String[] args) {
		return String.valueOf(delay.generateNext(Engine.getInstance().getVirtualUsersActive()));
	}

}
