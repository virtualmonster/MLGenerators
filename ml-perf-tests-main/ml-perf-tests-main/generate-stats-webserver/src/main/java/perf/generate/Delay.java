package perf.generate;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

public class Delay {

	@Data
	@Builder
	public static class Stage {
		private Integer fromUsers;
		private Integer toUsers;
		private Long minMs;
		private Long maxMs;
	}
	
	@Getter
	public static class Profile {
		private List<Stage> stages = new ArrayList<>();
		
		public Profile add(Stage stage) {
			stages.add(stage);
			return this;
		}
	}
	
	private Profile profile;
	
	public Delay(Profile profile) {
		this.profile = profile;
	}

	public long generateNext(int activeUsers) {
		for (Stage stage : profile.stages) {
			int fromUsers = stage.fromUsers != null ? stage.fromUsers.intValue() : 0;
			int toUsers = stage.toUsers != null ? stage.toUsers.intValue() : 500;
			long minMs = stage.minMs != null ? stage.minMs.longValue() : 0;
			long maxMs = stage.maxMs != null ? stage.maxMs.longValue() : 10 * 1000;
			if (activeUsers >= fromUsers && activeUsers <= toUsers) {
				long range = maxMs - minMs;
				double percent = (double) (activeUsers - fromUsers) / (toUsers - fromUsers);
				long delay = minMs + Math.round(range * percent);
				return delay;
			}
		}
		return 50;
	}
	

}
