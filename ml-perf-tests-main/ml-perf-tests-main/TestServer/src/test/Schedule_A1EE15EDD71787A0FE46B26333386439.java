/**@generated
 * WARNING � Changes you make to this file may be lost.
 *           File is generated and may be re-generated without warning.
 * @RPT-Core-generated Version 8.6
 */
package test;

import com.ibm.rational.test.lt.datacorrelation.execution.sub.DataSub;
import com.ibm.rational.test.lt.datacorrelation.execution.sub.IDataSub;
import com.ibm.rational.test.lt.datacorrelation.execution.sub.ISubRule;
import com.ibm.rational.test.lt.datacorrelation.execution.sub.SubRule;
import com.ibm.rational.test.lt.execution.http.tes.*;
import com.ibm.rational.test.lt.kernel.action.IContainer;
import com.ibm.rational.test.lt.kernel.action.IKThinkControl;
import com.ibm.rational.test.lt.kernel.action.impl.Container;
import com.ibm.rational.test.lt.kernel.action.impl.For;
import com.ibm.rational.test.lt.kernel.action.impl.KDelay;
import com.ibm.rational.test.lt.kernel.action.impl.KThrow;
import com.ibm.rational.test.lt.kernel.action.impl.UserGroup;
import com.ibm.rational.test.lt.kernel.data.FeatureOptionsDataAreaCreation;
import com.ibm.rational.test.lt.kernel.services.*;
import com.ibm.rational.test.lt.kernel.services.RPTEventStructure;
import java.util.HashMap;

@SuppressWarnings("all")
public class Schedule_A1EE15EDD71787A0FE46B26333386439 extends com.ibm.rational.test.lt.kernel.action.impl.Schedule {

	public Schedule_A1EE15EDD71787A0FE46B26333386439(IContainer parent, String name) {
		super(parent, name, "A1EE15EDD71787A0FE46B26333386439");
		super.actualScheduleName = "Throughput-ConstantUsers";
		setThinkMax(10000);
		setThinkScheme(IKThinkControl.RECORDED);

	}

	public void execute() {

		this.addEventBehavior(
				new RPTEventStructure(new HTTPPageTitleVPEvent(), null, 1, "Page title verification failed"));
		this.addEventBehavior(
				new RPTEventStructure(new HTTPResponseCodeVPEvent(), null, 1, "Response code verification failed"));
		this.addEventBehavior(
				new RPTEventStructure(new HTTPResponseSizeVPEvent(), null, 1, "Response size verification failed"));
		this.addEventBehavior(
				new RPTEventStructure(new RPTFailVPEvent(), null, 1, "Content Verification Point failed"));
		this.addEventBehavior(new RPTEventStructure(new RPTConnectEvent(), null, 1, "Connection failed"));
		this.addEventBehavior(new RPTEventStructure(new RPTAuthenticationEvent(), null, 1, "Authentication failed"));
		this.addEventBehavior(new RPTEventStructure(new RPTDataPoolEOFEvent(),
				new RPTStopUserEvent("End of dataset reached"), 1, "End of dataset reached"));
		this.addEventBehavior(
				new RPTEventStructure(new RPTTestVerdictFailedEvent(), null, 2, "Test verdict was set to fail"));
		this.addEventBehavior(new RPTEventStructure(new RPTReferenceEvent(), null, 1, "Failed to extract reference"));
		this.addEventBehavior(new RPTEventStructure(new RPTSubstitutionEvent(), null, 1, "Substitution failed"));
		this.addEventBehavior(new RPTEventStructure(new RPTServerTimeoutEvent(), null, 1, "Timeout"));
		this.addEventBehavior(
				new RPTEventStructure(new RPTDroppedConnectionEvent(), null, 1, "Server dropped connection"));
		this.addEventBehavior(
				new RPTEventStructure(new RPTCustomCodeVPFailureEvent(), null, 1, "Custom Verification Point failed"));
		this.addEventBehavior(new RPTEventStructure(new RPTCustomCodeExceptionEvent(),
				new RPTStopUserEvent("Custom Code reported an unhandled exception"), 1,
				"Custom Code reported an unhandled exception"));

		HashMap http_hm = new HashMap();

		this.addInitially(new FeatureOptionsDataAreaCreation(this, "com.ibm.rational.test.lt.feature.http", http_hm));
		this.addUserGroup(new UserGroup_1(this));

		setMaxHealthFailuresToLog(50, -1, 0);
		super.execute();
	}

	public class UserGroup_1 extends UserGroup {

		public UserGroup_1(IContainer parent) {
			super(parent, "User Group 1", "A1EE0ADCB3C0BD6CE632B93931363537");

			setNumUsers(100.0);

		}

		public IContainer createTesterWorkload(IContainer parent) {
			return scenario_1(parent);
		}

		private Container scenario_1(IContainer parent) {
			Container scenario = new Container(parent, "Default Scenario", "A1EE0ADCB3C0BD77E632B93931363537") {

				public void reportStopMessage() {
				}

				public void reportForcedStopMessage() {
				}

				public void execute() {
					this.add(new test.StartServer_Test_A1EDDD423DD06140F474B56631393731(this,
							"A1EE0AF65ACCFC40E632B93931363537") {
						public void execute() {
							this.setRtbEnabled(false);
							super.execute();
						}
					});

					For loopNm_1 = Loop_1(this);
					this.add(loopNm_1);
					loopNm_1.addVarsToInit(null);

					this.add(new com.ibm.rational.test.lt.execution.protocol.impl.HTTPUserComplete(this));
					super.execute();
				}
			};

			return scenario;
		}

	}

	private For Loop_1(IContainer parent) {
		For forLoop = new For(parent, "Loop1", "A1EE0ADCB3C0BD7AE632B93931363537", -1, 0, 0, 0, 1, true, false, null) {

			public void executeLoop() {

				this.add(new test.ThroughputSimulation_Test_A1EE15E76414D6B7FE46B26333386439(this,
						"A1EE15ECDE037DE0FE46B26333386439") {
					public void execute() {
						this.setRtbEnabled(false);
						super.execute();
					}
				});

				this.add(new KDelay(this, "Delay (100 ms.)", "A1EE0ADEA2D04640E632B93931363537", 100));
				super.executeLoop();
			}

		};
		return forLoop;
	}

}