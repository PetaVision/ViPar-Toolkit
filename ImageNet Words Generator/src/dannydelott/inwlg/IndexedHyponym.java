package dannydelott.inwlg;

public class IndexedHyponym {

	private int index;
	private String hyponym;

	public IndexedHyponym(int i, String h) {
		index = i;
		hyponym = h;
	}

	public int getIndex() {
		return index;
	}

	public String getHyponym() {
		return hyponym;
	}

}
