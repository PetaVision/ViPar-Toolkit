package dannydelott.inwlg;

import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Prompt {

	private String exclusions = null;
	private int[] exclusionsArray = null;
	private boolean badExclusionsFlag;

	// //////////////
	// CONSTRUCTOR //
	// //////////////

	public Prompt() {
		badExclusionsFlag = false;
	}

	// //////////////////
	// PUBLIC METHODS //
	// //////////////////

	public String getCategoryFromUserInput() {
		boolean continueFlag = false;
		Scanner scanSelection = new Scanner(System.in);
		String category = null;
		// get category from user
		while (!continueFlag) {
			Printer.printHorizontalLine(0);
			System.out
					.print("PROMPT:\tType in a category noun (eg: 'bird', 'plane', etc)"
							+ "\n\tType 'done' to exit: ");
			category = scanSelection.nextLine().toLowerCase();

			Printer.printHorizontalLine(0);
			if (category.contentEquals("done")) {
				return null;
			}
			continueFlag = true;
		}

		return category;

	}

	public Object getGlossSelectionFromUserInput(int g) {
		boolean continueFlag = false;
		Scanner scanSelection = new Scanner(System.in);
		int gloss = 0;
		int numOfGlosses = g;

		// get category from user
		while (!continueFlag) {
			Printer.printHorizontalLine(0);
			System.out
					.print("PROMPT:\tType the number of the gloss that fits the intended meaning "
							+ "\n\tType 'back' to re-enter the category noun:  ");

			String input = scanSelection.nextLine().toLowerCase();

			// if not an integer or 'back'
			if (!input.matches("^\\d+$") && !input.contentEquals("back")) {
				continue;
			}

			// 'back'
			if (input.contentEquals("back")) {
				return null;
			}

			gloss = Integer.parseInt(input);

			// if number is out of bounds
			if (gloss > numOfGlosses || gloss <= 0) {
				continue;
			}

			continueFlag = true;

		}

		return gloss;
	}

	public Object getSynsetTypeFromUserInput() {
		boolean continueFlag = false;
		Scanner scanSelection = new Scanner(System.in);
		int type;

		// gets exclusions from user
		while (!continueFlag) {
			Printer.printHorizontalLine(0);
			System.out.print("PROMPT:\tType (1) for first children subset "
					+ "\n\tType (2) for the whole synset subtree "
					+ "\n\tType 'back' to re-select the gloss: ");

			String input = scanSelection.nextLine().toLowerCase();

			// if not 1, 2, or 'back'
			if (!input.matches("^[1-2]$") && !input.contentEquals("back")) {
				continue;
			}

			Printer.printHorizontalLine(0);

			// 'back'
			if (input.contentEquals("back")) {
				System.out.println();
				return null;
			}

			type = Integer.parseInt(input);

			return type;

		}

		// never used
		return null;
	}

	public void getExclusionsFromUserInput(List<String> list) {

		boolean continueFlag = false;
		badExclusionsFlag = false;

		Scanner scanSelection = new Scanner(System.in);

		// gets exclusions from user
		while (!continueFlag) {
			Printer.printHorizontalLine(0);
			System.out
					.print("PROMPT: Type the numbers that correspond to the hyponyms you wish to exclude (separated by a [SPACE])"
							+ "\n\tType 'skip' to include all of the hyponyms "
							+ "\n\tType 'back' to reselect a subset type: ");

			exclusions = scanSelection.nextLine().toLowerCase();

			// bad input
			if (!exclusions.contentEquals("skip")
					&& !exclusions.contentEquals("back")
					&& !exclusions.matches("^([0-9]*\\s+)*[0-9]*$")) {
				continue;
			}
			continueFlag = true;

		}

		// evaluate input

		// don't exclude anything option
		if (exclusions.contentEquals("skip")) {
			exclusions = "skip";
			return;
		}

		if (exclusions.contentEquals("back")) {
			exclusions = "back";
			return;
		}

		// splits exclusions into int array
		String[] parts = exclusions.split(" ");
		exclusionsArray = new int[parts.length];
		for (int i = 0; i < parts.length; i++) {

			// reprompts if the user gave an above/below bound hyponym number
			if (Integer.parseInt(parts[i]) > list.size()
					|| Integer.parseInt(parts[i]) < 1) {
				Printer.printHorizontalLine(0);
				System.out.println("\nERROR:\t" + Integer.parseInt(parts[i])
						+ " is not a valid hyponym number.\n");
				badExclusionsFlag = true;

			}
			exclusionsArray[i] = Integer.parseInt(parts[i]);
		}

		// reprompts if the user gave duplicate hyponym numbers
		if (hasDuplicates(exclusionsArray)) {
			Printer.printHorizontalLine(0);
			System.out.println("\nERROR:\tDuplicate hyponym numbers found.\n");
			badExclusionsFlag = true;
		}

	}

	public Object getConfirmationFromUserInput() {

		boolean continueFlag = false;
		Scanner scanSelection = new Scanner(System.in);

		// gets exclusions from user
		while (!continueFlag) {

			Printer.printHorizontalLine(0);
			System.out
					.print("CONFIRM: Type (1) if the exclusions are correct"
							+ "\n\t Type 'back' to reselect exclusions: ");

			String input = scanSelection.nextLine().toLowerCase();

			// bad input
			if (!input.matches("^[1]$") && !input.contentEquals("back")) {
				continue;
			}

			// 'back'
			if (input.contentEquals("back")) {
				return false;
			}

			continueFlag = true;
		}
		return true;
	}

	// /////////////////
	// GLOBAL GETTERS //
	// /////////////////

	public boolean getFlagBadExclusions() {
		return badExclusionsFlag;
	}

	public int[] getExclusionsArray() {
		return exclusionsArray;
	}

	public String getExclusionsString() {
		return exclusions;
	}

	// //////////////////
	// PRIVATE METHODS //
	// //////////////////

	private boolean hasDuplicates(final int[] array) {
		Set<Integer> lump = new HashSet<Integer>();
		for (int i : array) {
			if (lump.contains(i))
				return true;
			lump.add(i);
		}
		return false;
	}

}
