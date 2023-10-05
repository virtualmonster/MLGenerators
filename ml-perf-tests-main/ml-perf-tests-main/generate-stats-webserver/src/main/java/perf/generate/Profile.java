package perf.generate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import lombok.Getter;
import perf.generate.Generator.AbsoluteStage;
import perf.generate.Generator.Interpolate;
import perf.generate.Generator.Stage;

@Getter
public class Profile {

	private List<Stage> stages = new ArrayList<>();
	private static final Interpolate linear = Generator.linearInterpolate();

	public Profile add(Stage stage) {
		stages.add(stage);
		return this;
	}

	public int maxUsers() {
		return stages.stream().map(s -> Math.max(s.getFromUsers(), s.getToUsers())).max(Integer::compareTo)
				.orElseThrow(() -> new IllegalStateException("No maximum user value found"));
	}

	public int minUsers() {
		return stages.stream().map(s -> Math.min(s.getFromUsers(), s.getToUsers())).min(Integer::compareTo)
				.orElseThrow(() -> new IllegalStateException("No minimum user value found"));
	}

	public Optional<AbsoluteStage> findAbsoluteStartEndTimeFromTime(double time) {
		double stageStartTime = 0;
		for (Stage s : stages) {
			double stageEndTime = stageStartTime + s.getDurationMs();
			if (time < stageEndTime) {
				return Optional.of(AbsoluteStage.builder().startTime(stageStartTime).endTime(stageEndTime)
						.startUsers(s.getFromUsers()).endUsers(s.getToUsers()).build());
			}
			stageStartTime = stageEndTime;
		}
		return Optional.empty();
	}
	
	/**
	 * Estimate the number of users at a given point in time.
	 * 
	 * @return Amount of users
	 */
	public Function<Double, Double> fTimeToUsers() {
		return time -> {
			Optional<AbsoluteStage> find = findAbsoluteStartEndTimeFromTime(time);
			double usersNow = 0;
			if (find.isPresent()) {
				AbsoluteStage s = find.get();
				usersNow = linear.evaluate(s.getStartTime(), s.getEndTime(), time, s.getStartUsers(), s.getEndUsers());
			}
			return usersNow;
		};
	}
	
	public long getTotalDuration() {
		return stages.stream().mapToLong(s -> s.getDurationMs()).sum();
	}

}