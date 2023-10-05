package customcode;

import com.ibm.rational.test.lt.kernel.IDataArea;
import com.ibm.rational.test.lt.kernel.engine.impl.Engine;
import com.ibm.rational.test.lt.kernel.impl.Time;
import com.ibm.rational.test.lt.kernel.services.ITestExecutionServices;
import com.ibm.rational.test.lt.kernel.services.IVirtualUserInfo;

import perf.generate.Generator.Invocation;

public class Util {

	public static Invocation userInfo(ITestExecutionServices tes) {
		IVirtualUserInfo vuInfo = (IVirtualUserInfo) tes.findDataArea(IDataArea.VIRTUALUSER).get(IVirtualUserInfo.KEY);
		return new Invocation(Engine.getInstance().getVirtualUsersActive(), vuInfo.getUID(), Time.timeInTest());
	}

}
