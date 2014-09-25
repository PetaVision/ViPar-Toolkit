package dannydelott.inwlg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;

public class Main {

	public static void main(String[] args) {

		/*
		 * Setup
		 */
		boolean nextFlag;
		boolean backFlag;

		boolean categoryFlag = true;
		String category = "";

		boolean glossFlag = true;
		String glossSelection = null;

		boolean subsetTypeFlag = true;
		boolean promptSubsetTypeFlag = true;
		List<String> hyponymWords = null;

		boolean exclusionsFlag = true;
		boolean skipExclusionsFlag = false;
		int[] exclusions = null;
		List<IndexedHyponym> exclusionsList = null;

		boolean confirmationFlag = true;

		System.out.println("\n\tLoading Image-Net files...\n");

		// externalizes and loads ImageNet file
		ImageNet in = new ImageNet();

		// gets the ImageNetAPI
		ImageNetAPI imageNetApi = new ImageNetAPI(
				in.getWordNetGlossesHashtable());

		// holds words as keys and offset lists as values
		Hashtable<String, List<String>> imageNet = in
				.getWordNetNounsHashtable();

		Prompt prompt = new Prompt();

		do {

			/*
			 * 0. Initialize to false to begin prompts
			 */
			backFlag = false;

			/*
			 * 1. CATEGORY SELECTION
			 */

			nextFlag = false;

			// prompt user for category
			if (categoryFlag) {
				categoryFlag = false;

				while (!nextFlag) {

					category = prompt.getCategoryFromUserInput();

					// 'done'
					if (category == null) {
						return;
					}

					// print blank line before displaying gloss selection
					System.out.println();

					// if word does not exist in WordNet
					if (!imageNet.containsKey(category)) {
						System.out.println("ERROR:\t'" + category
								+ "' does not exist in WordNet.\n");
						continue;
					}

					nextFlag = true; // break while-loop
				}
			}

			/*
			 * 2. GLOSS SELECTION
			 */

			nextFlag = false;
			if (glossFlag) {
				glossFlag = false;

				while (!nextFlag && !backFlag) {

					// gets the glosses from the category offsets
					List<String> offsets = imageNet.get(category);
					System.out
							.println("\tSelect the intended definition of the category word:\n");

					Printer.printEnumeratedGlossesFromOffsets(in, offsets);
					System.out.println();

					// gets the user's gloss selection as integer
					Object selection = prompt
							.getGlossSelectionFromUserInput(offsets.size());

					// 'back'
					if (selection == null) {
						backFlag = true;
						continue;
					}
					glossSelection = offsets.get((Integer) selection - 1);
					nextFlag = true;
				}

				if (backFlag) {
					categoryFlag = true; // go here
					glossFlag = true;
					subsetTypeFlag = true;
					exclusionsFlag = true;
					confirmationFlag = true;
					continue;
				}
			}

			/*
			 * 3. SUBSET TYPE SELECTION AND STORE HYPONYMS
			 */

			nextFlag = false;

			if (subsetTypeFlag) {
				subsetTypeFlag = false;
				promptSubsetTypeFlag = true;

				while (promptSubsetTypeFlag) {

					// only prompt once unless set to true in inner loop
					promptSubsetTypeFlag = false;

					while (!nextFlag && !backFlag) {

						// gets the synset type as an integer (1 or 2)
						Object synsetType = prompt.getSynsetTypeFromUserInput();

						// 'back'
						if (synsetType == null) {
							backFlag = true;
							continue;
						}

						System.out
								.println("\n\tRetrieving hyponyms from Image-Net...");

						// gets raw synset offsets according to type
						List<String> hyponymOffsets = imageNetApi
								.getHyponymOffsetsFromOffset(glossSelection,
										(Integer) synsetType);

						// gets the words from the synsets and puts them in
						// hashset to remove duplicates
						HashSet<String> set = new HashSet<String>();
						for (String offset : hyponymOffsets) {
							List<String> temp = imageNetApi
									.getWordsFromOffset(offset);
							if (temp != null) {
								set.addAll(temp);
							}
						}

						// removes binomial nomenclatures
						set.removeAll(in.getBinomialNomenclatureHashSet());

						// puts hyponym words into alphabetized list
						hyponymWords = new ArrayList<String>(set);
						java.util.Collections.sort(hyponymWords);

						// no hyponyms found, reprompt for type
						if (hyponymWords.size() == 0) {
							backFlag = true; // break out of inner
												// while-loop
							promptSubsetTypeFlag = true; // reiterate outer
															// while-loop
							continue;
						}

						nextFlag = true;

					}

					if (backFlag) {
						// reprompts for type if no hyponyms found
						if (promptSubsetTypeFlag) {
							backFlag = false;
							continue;
						}
						categoryFlag = false;
						glossFlag = true; // go here
						subsetTypeFlag = true;
						exclusionsFlag = true;
						confirmationFlag = true;
						continue; // break out of outer loop
					}
				}

				// 'back'
				if (backFlag) {
					continue;
				}
			}

			/*
			 * 5. EXCLUSION SELECTION
			 */
			nextFlag = false;

			if (exclusionsFlag) {
				exclusionsFlag = false;
				skipExclusionsFlag = false;

				while (!nextFlag && !backFlag) {

					if (!prompt.getFlagBadExclusions()) {
						// prints hyponyms
						System.out.println();

						Printer.printEnumeratedList(hyponymWords);
						System.out.println();
					}

					// gets exclusions from user input
					prompt.getExclusionsFromUserInput(hyponymWords);

					// keep all hyponyms ('skip' command)
					if (prompt.getExclusionsString().contentEquals("skip")) {
						nextFlag = true;
						skipExclusionsFlag = true;
						continue;
					}

					// 'back'
					if (prompt.getExclusionsString().contentEquals("back")) {
						backFlag = true;
						continue;
					}

					// bad exclusions given
					if (prompt.getFlagBadExclusions()) {
						continue;
					}

					// stores user's exclusion input in List
					exclusionsList = new ArrayList<IndexedHyponym>();
					IndexedHyponym temp;
					exclusions = prompt.getExclusionsArray();
					for (int i = 1; i < hyponymWords.size() + 1; i++) {
						for (int j = 0; j < exclusions.length; j++) {
							if (i == exclusions[j]) {
								temp = new IndexedHyponym(i,
										hyponymWords.get(i - 1));
								exclusionsList.add(temp);
							}
						}
					}

					nextFlag = true;

				}

				if (backFlag) {

					categoryFlag = false;
					glossFlag = false;
					subsetTypeFlag = true;// go here
					exclusionsFlag = true;
					confirmationFlag = true;
					continue;
				}
			}

			/*
			 * 6. CONFIRMATION
			 */

			nextFlag = false;
			if (confirmationFlag) {
				confirmationFlag = false;
				while (!nextFlag && !backFlag) {
					Printer.printHorizontalLine(0);

					if (!skipExclusionsFlag) {

						// prints the exclusion selections
						System.out
								.println("\n\tConfirm that these exclusions are correct:");

						Printer.printIndexedHyponymList(exclusionsList);
						System.out.println("\n\t" + exclusionsList.size()
								+ " exclusions total.\n");

					
					}

					Object input = prompt.getConfirmationFromUserInput();
					Printer.printHorizontalLine(0);

					// 'back'
					if ((Boolean) input == false) {
						backFlag = true;
						continue;
					}

					// confirmed
					if ((Boolean) input == true) {
						nextFlag = true;
						continue;
					}
				}

				if (backFlag) {
					categoryFlag = false;
					glossFlag = false;
					subsetTypeFlag = false;
					exclusionsFlag = true; // go here
					confirmationFlag = true;
					continue;
				}
			}

			/*
			 * 6. Create final list of hyponyms to export from total -
			 * exclusions.
			 */

			// removes exclusion selections from list
			if (exclusions != null) {

				// get exclusions hyponyms
				List<String> temp = new ArrayList<String>();
				for (IndexedHyponym i : exclusionsList) {
					temp.add(i.getHyponym().toString());
				}

				// remove exclusions from list of total hyponyms
				hyponymWords.removeAll(temp);
			}

			/*
			 * 7. Create JSON object and export to file
			 */
			String[] hyponyms = hyponymWords.toArray(new String[hyponymWords
					.size()]);

			JsonItem j = new JsonItem(category, hyponyms);
			try {
				OutputLog ol = new OutputLog("target-words.json");
				ol.writeJsonItemToFile(j);

				System.out.println("\n\t" + hyponymWords.size()
						+ " hyponyms for '" + category
						+ "' have been exported.\n");

				Printer.printEnumeratedList(hyponymWords);
				System.out.println();

			} catch (IOException e) {
				e.printStackTrace();
			}

			/*
			 * 8. Set to true for another loop iteration
			 */
			backFlag = true;
			categoryFlag = true; // go here
			glossFlag = true;
			subsetTypeFlag = true;
			exclusionsFlag = true;
			confirmationFlag = true;

		} while (backFlag);
	}
}