package perf.generate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static perf.generate.CsvTemplate.STARTTIME_PARAM;
import static perf.generate.CsvTemplate.TESTNAME_PARAM;
import static perf.generate.CsvTemplate.DefaultTemplate.TIME_INDEX;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import perf.generate.CsvTemplate.ColumnParam;

public class TestCsvTemplate {

	@Test
	void testGen() throws IOException {
		CsvTemplate template = CsvTemplate.defaultTemplate();

		Map<String, String> params = new HashMap<>();
		params.put(STARTTIME_PARAM, "May 17, 2023 at 11:21:18 AM EDT");
		params.put(TESTNAME_PARAM, "TestName");

		List<ColumnParam> columnParams = Arrays.asList( //
				new ColumnParam(TIME_INDEX, new String[] { "5000", "1000", "1500" }), //
				new ColumnParam(4, new String[] { "5", "8", "10" }));

		String expectCsv = new String(Files.readAllBytes(Paths.get("src/test/resources/generate/genFile.expect.csv")));
		assertEquals(norm(expectCsv), norm(template.generate(params, columnParams)));
	}

	private String norm(String str) {
		return str.replace("\r\n", "\n");
	}

}
