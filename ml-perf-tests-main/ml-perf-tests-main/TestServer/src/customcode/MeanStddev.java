package customcode;

import org.apache.commons.math3.distribution.NormalDistribution;

import com.ibm.rational.test.lt.kernel.services.ITestExecutionServices;

import perf.generate.Generator;

public class MeanStddev implements com.ibm.rational.test.lt.kernel.custom.ICustomCode2 {

	static GauusianGenerator gen = null;

	static class GauusianGenerator implements Generator {
		double mean;
		double stdev;
		NormalDistribution gaussian = new NormalDistribution(0, 1);

		public GauusianGenerator(double mean, double stdev) {
			this.gaussian = new NormalDistribution(mean, stdev);
		}

		@Override
		public synchronized String nextAction(Invocation call) {
			int delayMs = (int) Math.max(0.0, gaussian.sample());
			return "/throughput/?size=12000&delay=" + delayMs;
		}

	}

	static synchronized void ensureInitialized(int meanMs, int stddevMs) {
		if (gen == null) {
			gen = new GauusianGenerator(meanMs, stddevMs);
		}
	}

	public MeanStddev() {
	}

	public String exec(ITestExecutionServices tes, String[] args) {
		if (gen == null) {
			ensureInitialized(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
		}
		return gen.nextAction(Util.userInfo(tes));
	}

}
