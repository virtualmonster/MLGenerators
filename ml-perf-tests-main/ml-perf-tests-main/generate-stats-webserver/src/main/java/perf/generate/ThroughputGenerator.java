package perf.generate;

import java.util.Optional;
import java.util.function.Function;

public class ThroughputGenerator implements Generator {

	private final Function<Invocation, Double> responseSize;
	private final Function<Invocation, Double> delay;
	private static final Interpolate linear = Generator.linearInterpolate();

	public ThroughputGenerator(Function<Invocation, Double> responseSize, Function<Invocation, Double> delay) {
		this.responseSize = responseSize;
		this.delay = delay;

	}

	@Override
	public String nextAction(Invocation call) {
		return "/throughput/?size=" + responseSize.apply(call).longValue() + "&delay=" + delay.apply(call).longValue();
	}

	public static Function<Invocation, Double> constant(int amount) {
		return call -> {
			return Double.valueOf(amount);
		};
	}

	public static Function<Invocation, Double> userIdSplit(Profile curve, Function<Invocation, Double> below,
			Function<Invocation, Double> above) {
		return call -> {
			Optional<AbsoluteStage> find = curve.findAbsoluteStartEndTimeFromTime(call.getTimeInTest());
			if (!find.isPresent()) {
				return below.apply(call);
			} else {
				AbsoluteStage s = find.get();
				double splitAt = linear.evaluate(s.getStartTime(), s.getEndTime(), call.getTimeInTest(),
						s.getStartUsers(), s.getEndUsers());
				if (call.getUid() <= splitAt) {
					return below.apply(call);
				} else {
					return above.apply(call);
				}
			}
		};
	}
	
//	public static Function<Double, Double> genTotalThroughput_FunctionOfUsersAtTime(Profile stages, Function<Double, Double> totalBytesGivenUsers) {
//		return time -> {
//			Optional<AbsoluteStage> find = stages.findAbsoluteStartEndTimeFromTime(time);
//			double usersNow = 0;
//			if (find.isPresent()) {
//				AbsoluteStage s = find.get();
//				usersNow = linear.evaluate(s.getStartTime(), s.getEndTime(), time,
//						s.getStartUsers(), s.getEndUsers());
//			}
//			return totalBytesGivenUsers.apply(usersNow);
//		};
//	}
	
}
