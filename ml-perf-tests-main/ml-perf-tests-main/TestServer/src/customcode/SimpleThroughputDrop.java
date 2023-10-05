package customcode;

import com.ibm.rational.test.lt.kernel.services.ITestExecutionServices;

import perf.generate.Generator.Invocation;
import perf.generate.Generator.Stage;
import perf.generate.Profile;
import perf.generate.ThroughputGenerator;
import static perf.generate.ThroughputGenerator.constant;
import static perf.generate.ThroughputGenerator.userIdSplit;
import static perf.generate.Generator.AbsoluteStage;

import java.util.Optional;
import java.util.function.Function;

import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.concurrent.TimeUnit.DAYS;

public class SimpleThroughputDrop implements com.ibm.rational.test.lt.kernel.custom.ICustomCode2 {

	static int BYTES_PER_PAGE = 10000;
	static int UNLIMITED_USERS = 1000000;
	static ThroughputGenerator gen = null;

	static synchronized void ensureInitialized(int durationSecs, int usersNotAffected) {
		if (gen == null) {
			Profile splitCurve = new Profile();
			splitCurve.add(new Stage(UNLIMITED_USERS, UNLIMITED_USERS, SECONDS.toMillis(80))); // Effect off
			// Users > usersNotAffected have drop
			splitCurve.add(new Stage(-1, usersNotAffected, durationSecs*1000));
			splitCurve.add(new Stage(UNLIMITED_USERS, UNLIMITED_USERS, DAYS.toMillis(1))); // Effect off

			int delayOff = 0;
			Function<Invocation, Double> delayUntilStageEnd = i -> {
				Optional<AbsoluteStage> stage = splitCurve.findAbsoluteStartEndTimeFromTime(i.getTimeInTest());
				if (stage.isPresent()) {
					return Math.max(0, stage.get().getEndTime() - i.getTimeInTest());
				} else {
					return 0.0;
				}
			};
			gen = new ThroughputGenerator(constant(BYTES_PER_PAGE), //
					userIdSplit(splitCurve, constant(delayOff), delayUntilStageEnd));			
		}
	}

	public SimpleThroughputDrop() {
	}

	public String exec(ITestExecutionServices tes, String[] args) {
		if (gen == null) {
			ensureInitialized(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
		}
		return gen.nextAction(Util.userInfo(tes));
	}

}
