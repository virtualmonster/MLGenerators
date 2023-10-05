package perf.generate;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.io.csv.CsvWriteOptions;

/**
 * Generates Performance exported CSV statistic report using a template and
 * parameters.
 */
public class CsvTemplate {

	@Getter
	@AllArgsConstructor
	public static class ColumnParam {
		int index;
		String[] data;
	}

	public static class DefaultTemplate {
		public static final int COUNTERS_PER_ROW = 192;
		public static final int TIME_INDEX = 0;
		public static final int USERS_INDEX = 47;
		public static final int BYTES_RECV_INDEX = 50;
		public static final int BYTES_SENT_INDEX = 53;
	}

	public static final String TESTNAME_PARAM = "@@TESTNAME@@";
	public static final String STARTTIME_PARAM = "@@STARTTIME@@";

	private final String template;
	private final int countersPerRow;

	public CsvTemplate(String template, int countersPerRow) {
		this.template = template;
		this.countersPerRow = countersPerRow;
	}

	public String generate(Map<String, String> params, List<ColumnParam> columnParams) {
		String genOut = template;
		for (Map.Entry<String, String> entry : params.entrySet()) {
			genOut = genOut.replace(entry.getKey(), entry.getValue());
		}

		int rowCount = columnParams.stream().mapToInt(p -> p.getData().length).max().orElse(0);
		Table table = Table.create();
		for (int i = 0; i < countersPerRow; i++) {
			StringColumn column = StringColumn.create("Column " + i, rowCount);
			for (ColumnParam param : columnParams) {
				if (param.getIndex() == i) {
					String[] data = param.getData();
					for (int j = 0; j < data.length; j++) {
						column.set(j, data[j]);
					}
					break;
				}
			}

			table.addColumns(column);
		}

		StringWriter stringWriter = new StringWriter();
		table.write().csv(CsvWriteOptions.builder(stringWriter).header(false).build());
		stringWriter.flush();
		return genOut + "\n" + stringWriter.getBuffer().toString();
	}

	public static CsvTemplate defaultTemplate() {
		try (InputStream inputStream = CsvTemplate.class.getClassLoader()
				.getResourceAsStream("generate/statsFileTemplate.csv")) {
			return new CsvTemplate(IOUtils.toString(inputStream, StandardCharsets.UTF_8),
					DefaultTemplate.COUNTERS_PER_ROW);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

}
