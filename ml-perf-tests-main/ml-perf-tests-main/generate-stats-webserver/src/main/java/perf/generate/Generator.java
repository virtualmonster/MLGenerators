package perf.generate;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

public interface Generator {

	public static class Experiment {
		private double instances;
		private List<Tagging> tags;
	}
	
	@Getter
	@AllArgsConstructor
	public static class Tagging {
		private String name;
	}
	
	@Getter
	@AllArgsConstructor
	@Builder
	@ToString
	public static class AbsoluteStage {
		private double startTime;
		private double endTime;
		private double startUsers;
		private double endUsers;
	}
	
	@Getter
	@AllArgsConstructor
	public static class UserResponseTime {
		private double users;
		private double respTime;
	}
	
	@FunctionalInterface
	interface FunctionOfTime<T> {
		T evaluate(double time);
	}
	
	@FunctionalInterface
	interface Interpolate {
		double evaluate(double min, double max, double valueWithinMinMax, double outputLeft, double outputRight);
	}
	
	public static Interpolate linearInterpolate() {
		return (min, max, valueWithinMinMax, outputLeft, outputRight) -> {
		    if (min == max || valueWithinMinMax <= min) {
		        return outputLeft;
		    }
		    if (valueWithinMinMax >= max) {
		        return outputRight;
		    }
		    double ratio = (valueWithinMinMax - min) / (max - min);
		    return outputLeft + ratio * (outputRight - outputLeft);
		};
	}

	@Getter
	@AllArgsConstructor
	public static class Stage {
		private int fromUsers; // Inclusive
		private int toUsers;   // Exclusive
		private long durationMs;
	}

	@Getter
	@AllArgsConstructor
	public static class UserStage<T> {
		private int fromUsers; // Inclusive
		private int toUsers;   // Exclusive
		private T context;
	}
	
	@Getter
	@AllArgsConstructor
	@ToString
	public static class Invocation {
		long totalActiveUsers;
		long uid;
		long timeInTest;
	}
	
	public static double[] generateEqualIntervals(double initialValue, double increment, double maxValue) {
		int size = (int) Math.ceil((maxValue - initialValue) / increment) + 1;
		double[] array = new double[size];
		double currentValue = initialValue;
		for (int i = 0; i < size; i++) {
			array[i] = currentValue;
			currentValue += increment;
		}
		return array;
	}

	public static int[] doubleToIntArray(double[] doubleArray) {
		int[] intArray = new int[doubleArray.length];
		for (int i = 0; i < doubleArray.length; i++) {
			intArray[i] = (int) doubleArray[i];
		}
		return intArray;
	}

	public static String[] intToStringArray(int[] intArray) {
		String[] stringArray = new String[intArray.length];
		for (int i = 0; i < intArray.length; i++) {
			stringArray[i] = String.valueOf(intArray[i]);
		}
		return stringArray;
	}
	

	String nextAction(Invocation call);

}
