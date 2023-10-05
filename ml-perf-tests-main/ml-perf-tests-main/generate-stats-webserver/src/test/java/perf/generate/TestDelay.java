package perf.generate;

import static org.junit.jupiter.api.Assertions.*;

import java.io.StringWriter;

import org.junit.jupiter.api.Test;

import perf.generate.Delay.Profile;
import perf.generate.Delay.Stage;
import tech.tablesaw.api.IntColumn;
import tech.tablesaw.api.LongColumn;
import tech.tablesaw.api.Table;

class TestDelay {

	@Test
	void test() {
		Delay delay = new Delay(new Profile() //
				.add(Stage.builder().fromUsers(0).toUsers(100).minMs(200L).minMs(1000L).build())
		);
		IntColumn usersCol = IntColumn.create("USERS");
		LongColumn timeCol = LongColumn.create("TIME");
		LongColumn respCol = LongColumn.create("RESP");
		
		long timeMs = 0;
		for (int users = 1; users < 100; users++) {
			for (int i = 0; i < 10; i++) {
				usersCol.append(users);
				timeCol.append(timeMs);
				respCol.append(delay.generateNext(users));
				timeMs += 100;
			}
		}
		
		Table t = Table.create(timeCol, usersCol, respCol);
		
		StringWriter csvOut = new StringWriter();
		t.write().csv(csvOut);
		System.out.println(csvOut.toString());
	}

}
