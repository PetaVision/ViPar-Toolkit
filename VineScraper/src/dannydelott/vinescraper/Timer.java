package dannydelott.vinescraper;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.time.DurationFormatUtils;

public class Timer {

	private long startTime;
	private long endTime;
	private long executionTime;

	public Timer() {

	}

	// /////////////////
	// PUBLIC METHODS //
	// /////////////////

	public void begin() {
		startTime = System.currentTimeMillis();
	}

	public void end() {
		endTime = System.currentTimeMillis();
		executionTime = endTime - startTime;
	}

	public void printFormattedExecutionTime() {
		System.out.println(getFormattedExecutionTime());
	}

	// //////////////////
	// GLOBAL GETTERS //
	// //////////////////

	public long getStartTime() {
		return startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public long getExecutionTime() {
		return executionTime;
	}

	public String getFormattedExecutionTime() {
		return DurationFormatUtils.formatDurationWords(executionTime, true,
				false);
	}

	public String getStartDate() {
		Date start = new Date(startTime);
		SimpleDateFormat x = new SimpleDateFormat();
		return x.format(start);
	}

	public String getEndDate() {
		Date end = new Date(endTime);
		SimpleDateFormat x = new SimpleDateFormat();
		return x.format(end);
	}

	public String getDate() {
		Date d = new Date(System.currentTimeMillis());
		SimpleDateFormat x = new SimpleDateFormat();
		return x.format(d);
	}

}
