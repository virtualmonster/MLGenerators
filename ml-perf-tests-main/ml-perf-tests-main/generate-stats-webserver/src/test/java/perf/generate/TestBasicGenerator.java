package perf.generate;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.DoubleStream;
import java.util.stream.LongStream;

import org.apache.commons.math3.analysis.function.Sigmoid;
import org.junit.jupiter.api.Test;

import com.univocity.parsers.conversions.IntegerConversion;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import perf.generate.Generator.AbsoluteStage;
import perf.generate.Generator.Experiment;
import perf.generate.Generator.FunctionOfTime;
import perf.generate.Generator.Interpolate;
import perf.generate.Generator.Stage;
import perf.generate.Generator.UserResponseTime;
import perf.generate.Generator.UserStage;
import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.IntColumn;
import tech.tablesaw.api.LongColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.io.csv.CsvWriteOptions;

public class TestBasicGenerator {
	
	private StageProvider stageProvider = new StageProvider();
	private File csvOutFile = new File("c:\\file.csv");
	
	@Getter
	@AllArgsConstructor
	public static class TimeAndUser {
		long time;
		int users;
	}
	
	@Getter
	@AllArgsConstructor
	public static class TimeUserResp {
		double time;
		double users;
		double resp;
	}
	
	public static class StageProvider {
		
		private Profile stepSterioType1_MedRamp() {
			// 3 plateau, ramping increases 
			return new Profile() //
					.add(s3(0, 10, 10L)) //
					.add(s3(10, 10, 30L)) //
					.add(s3(10, 20, 10L)) //
					.add(s3(20, 20, 30L)) //
					.add(s3(20, 40, 10L)) //
					.add(s3(40, 40, 10L));
		}
		
		private Profile stepSterioType1_NoRamp() {
			// 3 step, no ramping 
			return new Profile() //
					.add(s3(0, 10, 1L)) //
					.add(s3(10, 10, 39L)) //
					.add(s3(10, 20, 1L)) //
					.add(s3(20, 20, 39L)) //
					.add(s3(20, 40, 2L)) //
					.add(s3(40, 40, 38L));
		}
		
		private Profile singlePrimaryStage_NoRamp(int targetUsers, long durationMs) {
			return new Profile() //
					.add(s3(targetUsers, targetUsers, durationMs));
		}
		
		private Profile singlePrimaryStage_ShortRamp(int targetUsers, long durationMs) {
			return new Profile() //
					.add(s3(0, targetUsers, 5)) //
					.add(s3(targetUsers, targetUsers, durationMs));
		}
		
		private Profile singlePrimaryStage_LongRamp(int targetUsers, long durationMs) {
			return new Profile() //
					.add(s3(0, targetUsers, 20)) //
					.add(s3(targetUsers, targetUsers, durationMs));
		}
		
	}
	

	/**
	 * Returns a simplistic function which calculates the response time given a time
	 * and a schedule profile.
	 */
	static FunctionOfTime<UserResponseTime> idealLockStep(Profile p, Interpolate respTimeMapper, //
			double outputRespMin, double outputRespMax) {
		// Produce response time given a time across the given profile
		Interpolate usersWithinStage = Generator.linearInterpolate();
		return time -> {
			double minUsers = p.minUsers();
			double maxUsers = p.maxUsers();
			Optional<AbsoluteStage> find = p.findAbsoluteStartEndTimeFromTime(time);
			if (!find.isPresent()) {
				return new UserResponseTime(0, 0);
			}
			AbsoluteStage s = find.get();
			double users = usersWithinStage.evaluate(s.getStartTime(), s.getEndTime(), time, //
					s.getStartUsers(), s.getEndUsers());
			return new UserResponseTime(users,
					respTimeMapper.evaluate(minUsers, maxUsers, users, outputRespMin, outputRespMax));
		};
	}
	
	
	static Stage s3(int fromUsers, int toUsers, long durationMs) {
		return new Stage(fromUsers, toUsers, durationMs);
	}
	
	@Getter
	static class Tuple<A,B> {
		private A a;
		private B b;
		public Tuple(A a, B b) {
			this.a = a;
			this.b = b;
		}
		@Override
		public String toString() {
			return "[" + a + ", " + b + "]";
		}
	}
	
	@Getter
	static class Tuple3<A,B,C> {
		private A a;
		private B b;
		private C c;
		public Tuple3(A a, B b, C c) {
			this.a = a;
			this.b = b;
			this.c = c;
		}
		@Override
		public String toString() {
			return "[" + a + ", " + b + ", " + c + "]";
		}

	}
	
	/**
	 * Testing basic concept.
	 */
	@Test
	void generateTest() {
		DoubleColumn timeCol = DoubleColumn.create("TIME");
		DoubleColumn usersCol = DoubleColumn.create("USERS");
		DoubleColumn respCol = DoubleColumn.create("RESP_TIME");

		Profile p = stageProvider.stepSterioType1_MedRamp();
		
		idealLockStep(p, timeCol, usersCol, respCol);

		Table table = Table.create(timeCol.asLongColumn(), usersCol.asLongColumn(), respCol.asLongColumn());
		dumpTableToConsole(table);
	}
	
	@Test
	void sendWave() {
		DoubleColumn timeCol = DoubleColumn.create("TIME");
		DoubleColumn usersCol = DoubleColumn.create("USERS");
		DoubleColumn respCol = DoubleColumn.create("RESP_TIME");

		Profile p = stageProvider.stepSterioType1_MedRamp();
		
		idealLockStep(p, timeCol, usersCol, respCol);
		
		double[] transform = respCol.asDoubleArray();
		transform = addTransform(transform, //
				waveTransform(timeCol.asDoubleArray(), 0.1, 1000.0));
		transform = addTransform(transform, //
				waveTransform(timeCol.asDoubleArray(), 0.05, 500));

		respCol = DoubleColumn.create("RESP_TIME", DoubleStream.of(transform));

		Table table = Table.create(timeCol.asLongColumn(), usersCol.asLongColumn(), respCol.asLongColumn());
		dumpTableToConsole(table);
		writeTableToFile(table, csvOutFile);
	}
	
	@Test
	void testWave() {
		DoubleColumn timeCol = DoubleColumn.create("TIME");
		DoubleColumn usersCol = DoubleColumn.create("USERS");
		DoubleColumn respCol = DoubleColumn.create("RESP_TIME");

		Profile p = stageProvider.singlePrimaryStage_NoRamp(30, 100);
		
		idealLockStep(p, timeCol, usersCol, respCol);
		
		//Math.PI
		double[] transform = respCol.asDoubleArray();
		
		
		double timeUnitsPerCycle = 99;
		double frequency = (2 * Math.PI) / timeUnitsPerCycle ;
		double height = 2;
		double offset = -1;
		transform = addTransform(transform, //
				waveTransform2(timeCol.asDoubleArray(), frequency, height, offset));
		transform = chopBottom(transform, 100);
		transform = chopTop(transform, 101.5);
		//transform = addTransform(transform, //
		//		waveTransform(timeCol.asDoubleArray(), 0.05, 500));

		respCol = DoubleColumn.create("RESP_TIME", DoubleStream.of(transform));

		Table table = Table.create(timeCol.asLongColumn(), usersCol.asLongColumn(), respCol);
		dumpTableToConsole(table);
		writeTableToFile(table, csvOutFile);
	}
	
	private double[] chopBottom(double[] data, double bottom) {
		double[] transform = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			if (data[i] < bottom) {
				transform[i] = bottom;
			} else {
				transform[i] = data[i];
			}
		}
		return transform;
	}
	
	private double[] chopTop(double[] data, double top) {
		double[] transform = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			if (data[i] > top) {
				transform[i] = top;
			} else {
				transform[i] = data[i];
			}
		}
		return transform;
	}
	
	private double[] addTransform(double[] data, double[] wave) {
		if (data.length != wave.length) {
			throw new IllegalArgumentException("data and transform must have the same length");
		}
		double[] offset = new double[wave.length];
		for (int i = 0; i < offset.length; i++) {
			offset[i] = data[i] + wave[i];
        }
		return offset;
	}
	
	private double[] waveTransform(double[] time, double frequency, double height) {
		double[] waveTransform = new double[time.length];
		for (int i = 0; i < time.length; i++) {
			waveTransform[i] = height * Math.sin(2 * Math.PI * frequency * time[i]);
		}
		return waveTransform;
	}
	
	private double[] waveTransform2(double[] time, double frequency, double height, double offset) {
		double[] waveTransform = new double[time.length];
		for (int i = 0; i < time.length; i++) {
			waveTransform[i] = height * Math.sin( frequency *  ((time[i] + offset))  );
		}
		return waveTransform;
	}

	
	private void idealLockStep(Profile p, DoubleColumn timeCol, DoubleColumn usersCol, DoubleColumn respCol) {
		double respTimeMin = 100.0, respTimeMax = 10000.0;
		FunctionOfTime<UserResponseTime> respTimeGenerator = idealLockStep(p, Generator.linearInterpolate(),
				respTimeMin, respTimeMax);
		for (double time = 1.0; time < 100.0; time += 1.0) {
			UserResponseTime dataPoint = respTimeGenerator.evaluate(time);
			timeCol.append(time);
			usersCol.append(dataPoint.getUsers());
			respCol.append(dataPoint.getRespTime());
		}
	}
	
	
	private void dumpTableToConsole(Table table) {
		StringWriter stringWriter = new StringWriter();
		table.write().csv(CsvWriteOptions.builder(stringWriter).separator('\t').build());
		stringWriter.flush();
		System.out.println(stringWriter.getBuffer().toString());
	}
	
	private void writeTableToFile(Table table, File file) {
		table.write().csv(CsvWriteOptions.builder(file).separator('\t').build());
	}

//	@Test
//	void simpleLockStep() {
//		Profile profile = new Profile();
//		profile.add(new Stage(0, 10, 10));
//		profile.add(new Stage(10, 10, 30));
//		profile.add(new Stage(10, 20, 10));
//		profile.add(new Stage(20, 20, 40));
//		profile.add(new Stage(20, 0, 10));
//		profile.add(new Stage(0, 0, 1));
//
//		List<TimeAndUser> userShape = LongStream.range(0, 100).boxed() //
//				.map(time -> {
//					List<Stage> stages = profile.getStages();
//					long startTime = 0L;
//					for (Stage s : stages) {
//						long endTime = startTime + s.getDurationMs();
//						if (time < endTime) {
//							return new TimeAndUser(time, (int) mapToRange(Math.max(startTime, time), startTime, endTime,
//									s.getFromUsers(), s.getToUsers()));
//						}
//						startTime = endTime;
//					}
//					// Use last stage starting users until forever
//					return new TimeAndUser(time, stages.get(stages.size() - 1).getFromUsers());
//				}).map(p -> {
//					System.out.println("time=" + p.getTime() + " users=" + p.getUsers() + 
//							"sig=" + mapSigmoid(p.getUsers(), 0, 40, 0, 200));
//					return p;
//				}).toList();
//		
//		
//		
//		//new UserStage<Range<Double>>(0, 10, new Range<>(500.0, 800.0));
//		//new UserStage<Range<Double>>(10, 20, new Range<>(800.0, 30000.0));
//		
//		
//	} // 
	
	
	static long mapToRange(long number, long fromInclusive, long toExclusive, long newFromInclusive, long newToExclusive) {
		long originalRange = toExclusive - fromInclusive;
		long newRange = newToExclusive - newFromInclusive;
		double normalizedValue = (double) (number - fromInclusive) / originalRange;
		long mappedNumber = (long) (normalizedValue * newRange + newFromInclusive);
		return mappedNumber;
	}
	
    public static double mapSigmoid(double number, double x, double y, double a, double b) {
        double normalizedValue = (number - x) / (y - x);
        normalizedValue -= 0.5;
        Sigmoid sigmoid = new Sigmoid(a, b);
        double mappedValue = sigmoid.value(normalizedValue);
        return mappedValue;
    }

}
