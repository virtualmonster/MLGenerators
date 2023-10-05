package customcode;

import org.apache.commons.math3.distribution.NormalDistribution;

import com.ibm.rational.test.lt.kernel.services.ITestExecutionServices;

import perf.generate.Generator;

public class LockStep implements com.ibm.rational.test.lt.kernel.custom.ICustomCode2 {

	static SlopeGenerator gen = null;

	static class SlopeGenerator implements Generator {
		double mean;
		double stdev;
		double maxIncrease;
		double maxUsers;
		NormalDistribution gaussian = new NormalDistribution(0, 1);

		public SlopeGenerator(double mean, double stdev, double maxIncrease, double maxUsers) {
			this.gaussian = new NormalDistribution(mean, stdev);
			this.maxIncrease = maxIncrease;
			this.maxUsers = maxUsers;
		}

		@Override
		public synchronized String nextAction(Invocation call) {
			int delayMs = (int) Math.max(0.0, gaussian.sample());
			if (maxUsers > 0) {
				delayMs += ((double)call.getUid() / (double)maxUsers) * maxIncrease;
			}
			return "/throughput/?size=12000&delay=" + delayMs;
		}

	}

	static synchronized void ensureInitialized(int meanMs, int stddevMs, int maxIncrease, int maxUsers) {
		if (gen == null) {
			gen = new SlopeGenerator(meanMs, stddevMs, maxIncrease, maxUsers);
		}
	}

	public LockStep() {
	}

	public String exec(ITestExecutionServices tes, String[] args) {
		if (gen == null) {
			ensureInitialized(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
		}
		return gen.nextAction(Util.userInfo(tes));
	}

}
