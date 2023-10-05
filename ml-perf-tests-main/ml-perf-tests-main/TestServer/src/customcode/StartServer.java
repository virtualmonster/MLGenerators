package customcode;

import java.util.concurrent.atomic.AtomicBoolean;

import com.ibm.rational.test.lt.kernel.services.ITestExecutionServices;

import testserver.TestServer;


public class StartServer implements com.ibm.rational.test.lt.kernel.custom.ICustomCode2 {
	
	static AtomicBoolean STARTED = new AtomicBoolean(false); 

	public StartServer() {
	}

	public String exec(ITestExecutionServices tes, String[] args) {
		if (STARTED.compareAndSet(false, true)) {
			launchServer(tes, args);
			return "STARTED";
		} else {
			try {
				Thread.sleep(10*1000);
			} catch (InterruptedException e) {
				tes.getTestLogManager().reportMessage(e.toString());
			}
			return "RUNNING";
		}
	}
	
	public String launchServer(ITestExecutionServices tes, String[] args) {
		Thread server = new Thread(() -> {
			try {
				tes.getTestLogManager().reportMessage("Starting Server");
				TestServer.main(new String[] {});
			} catch (Exception e) {
				tes.getTestLogManager().reportMessage(e.toString());
			}				
		});
		server.setDaemon(true);
		server.start();
		try {
			Thread.sleep(10*1000);
		} catch (InterruptedException e) {
			tes.getTestLogManager().reportMessage(e.toString());
		}
		return null;
	}

}
