package kush.linesorter;

public class SortInfo {

	public final int linesProcessed;
	public final int linesTotal;
	public final int numberFiles;

	public SortInfo(final int linesProcessed, final int linesTotal, final int numberFiles) {
		this.linesProcessed = linesProcessed;
		this.linesTotal = linesTotal;
		this.numberFiles = numberFiles;
	}
}
