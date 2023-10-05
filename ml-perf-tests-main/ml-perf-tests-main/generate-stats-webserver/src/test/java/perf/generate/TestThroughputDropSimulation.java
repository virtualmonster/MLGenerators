package perf.generate;

import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.*;
import static perf.generate.CsvTemplate.STARTTIME_PARAM;
import static perf.generate.CsvTemplate.TESTNAME_PARAM;
import static perf.generate.CsvTemplate.DefaultTemplate.*;
import static perf.generate.Generator.*;
import static perf.generate.ThroughputGenerator.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static perf.generate.ThroughputGenerator.constant;
import static perf.generate.ThroughputGenerator.userIdSplit;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.junit.jupiter.api.Test;

import perf.generate.CsvTemplate.ColumnParam;
import perf.generate.Generator.Invocation;
import perf.generate.Generator.Stage;

public class TestThroughputDropSimulation {
	
	private CsvTemplate template = CsvTemplate.defaultTemplate();

	@Test
	void testConstant() {
		ThroughputGenerator gen = new ThroughputGenerator(constant(50), constant(5000));
		assertEquals("/throughput/?size=50&delay=5000", gen.nextAction(new Invocation(5, 0, 0L)));
		assertEquals("/throughput/?size=50&delay=5000", gen.nextAction(new Invocation(5, 1, 50L)));
		
		gen = new ThroughputGenerator(constant(100), constant(0));
		assertEquals("/throughput/?size=100&delay=0", gen.nextAction(new Invocation(5, 0, 0L)));
		assertEquals("/throughput/?size=100&delay=0", gen.nextAction(new Invocation(5, 1, 50L)));
	}
	
	@Test
	void dropSuddendlyInverse() {
		Profile curve = new Profile();
		curve.add(new Stage(100, 100, 5)); // Effect off
		curve.add(new Stage(1, 1, 5)); // Half user affect
		curve.add(new Stage(100, 100, 5)); // Effect off
		curve.add(new Stage(-1, -1, 5)); // Full user affect
		
		ThroughputGenerator gen = new ThroughputGenerator(constant(50), //
				userIdSplit(curve, constant(0), constant(100)));
		for (int i = 0; i < 30; i++) {
			for (int user = 0; user < 4; user++) {
				System.out.println("user=" + user + " t=" + i + " delay=" + gen.nextAction(new Invocation(20, user, i)));
				if (i >= 0 && i < 5) {
					assertEquals("/throughput/?size=50&delay=0", gen.nextAction(new Invocation(20, user, i)));
				}
				if (i >= 5 && i < 10) {
					if (user == 0 || user == 1) {
						assertEquals("/throughput/?size=50&delay=0", gen.nextAction(new Invocation(20, user, i)));	
					} else {
						assertEquals("/throughput/?size=50&delay=100", gen.nextAction(new Invocation(20, user, i)));
					}
				}
				if (i >= 10 && i < 15) {
					assertEquals("/throughput/?size=50&delay=0", gen.nextAction(new Invocation(20, user, i)));
				}
				if (i >= 15 && i < 20) {
					assertEquals("/throughput/?size=50&delay=100", gen.nextAction(new Invocation(20, user, i)));
				}
				if(i >= 20) {
					assertEquals("/throughput/?size=50&delay=0", gen.nextAction(new Invocation(20, user, i)));
				}
			}
			System.out.println("");
		}
	}

	@Test
	void generateResult() throws IOException {
		Profile profile = new Profile();
		profile.add(new Stage(5, 5, MINUTES.toMillis(5)));
		profile.add(new Stage(5, 10, SECONDS.toMillis(20)));
		profile.add(new Stage(10, 10, MINUTES.toMillis(15)));
		profile.add(new Stage(10, 0, SECONDS.toMillis(30)));

		Function<Double, Double> fTotalUsersToThroughtput = usersNow -> usersNow * 100000.0;
		double[] time = generateEqualIntervals(5000, 5000, 5000 + profile.getTotalDuration());
		double[] bytesRecv = new double[time.length];
		double[] bytesSent = new double[time.length];
		double[] users = new double[time.length];

		Function<Double, Double> fTimeToUsers = profile.fTimeToUsers();
		for (int i = 0; i < time.length; i++) {
			users[i] = fTimeToUsers.apply(time[i]);
			bytesRecv[i] = fTotalUsersToThroughtput.apply(users[i]);
			bytesSent[i] = bytesRecv[i] * 0.1;
		}
		
		Map<String, String> params = new HashMap<>();
		params.put(STARTTIME_PARAM, "May 17, 2023 at 11:21:18 AM EDT");
		params.put(TESTNAME_PARAM, "TestName");
		List<ColumnParam> columnParams = Arrays.asList( //
				new ColumnParam(TIME_INDEX, intToStringArray(doubleToIntArray(time))), //
				new ColumnParam(USERS_INDEX, intToStringArray(doubleToIntArray(users))), //
				new ColumnParam(BYTES_SENT_INDEX, intToStringArray(doubleToIntArray(bytesSent))), //
				new ColumnParam(BYTES_RECV_INDEX, intToStringArray(doubleToIntArray(bytesRecv))));

		System.out.println(template.generate(params, columnParams));
	}

}
