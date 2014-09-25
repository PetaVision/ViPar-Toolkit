package dannydelott.inwlg;

import java.util.List;

public final class Printer {

	// //////////////
	// CONSTRUCTOR //
	// //////////////

	// /////////////////
	// PUBLIC METHODS //
	// /////////////////

	public static final void printEnumeratedGlossesFromOffsets(ImageNet in,
			List<String> offsets) {
		ImageNetAPI imageNetApi = new ImageNetAPI(
				in.getWordNetGlossesHashtable());
		int glossIndex = 1;
		System.out.println("\t#\tGLOSS");
		Printer.printHorizontalLine(1);
		for (int i = 0; i < offsets.size(); i++) {
			System.out.println("\t" + glossIndex + "\t"
					+ imageNetApi.getGlossFromOffset(offsets.get(i)) + "\n");
			List<String> temp = imageNetApi.getWordsFromOffset(offsets.get(i));
			System.out.println("\t\tSynset:\t" + temp.toString());
			if (i != offsets.size() - 1) {
				Printer.printHorizontalLine(1);
			}
			glossIndex++;
		}
	}

	public static final void printEnumeratedList(List<String> list) {
		System.out.println("\t#\tHYPONYM");
		Printer.printHorizontalLine(1);
		int index = 1;
		for (String str : list) {
			System.out.println("\t" + index + "\t" + str);
			index++;
		}

	}

	public static final void printIndexedHyponymList(List<IndexedHyponym> list) {
		System.out.println("\n\t#\tHYPONYM");
		Printer.printHorizontalLine(1);
		for (IndexedHyponym i : list) {
			System.out.println("\t" + i.getIndex() + "\t" + i.getHyponym());
		}
	}

	public static void printHorizontalLine(int tabs) {

		int t = tabs;
		if (t == 0) {
			t = -8;
		} else {
			t = t * 8;
		}

		for (int i = 0; i < tabs; i++) {
			System.out.print("\t");
		}
		for (int j = 0; j < 125 - t; j++) {
			System.out.print("-");

		}
		System.out.println();
	}

}
