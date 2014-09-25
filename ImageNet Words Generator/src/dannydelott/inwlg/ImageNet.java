package dannydelott.inwlg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

public class ImageNet {

	private static Hashtable<String, List<String>> nounsList;
	private static Hashtable<String, String> glosses;
	private static HashSet<String> binomialNomenclature;

	// //////////////
	// CONSTRUCTOR //
	// //////////////

	public ImageNet() {
		try {
			externalizeFile("temp/index.noun", "/index.noun");
			externalizeFile("temp/gloss.txt", "/gloss.txt");
			externalizeFile("temp/binomial_nomenclature.txt",
					"/binomial_nomenclature.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		loadWordNetNouns();
		loadWordNetGlosses();
		loadBinomialNomenclature();
	}

	// /////////////////
	// GLOBAL GETTERS //
	// /////////////////

	public Hashtable<String, List<String>> getWordNetNounsHashtable() {
		return nounsList;
	}

	public Hashtable<String, String> getWordNetGlossesHashtable() {
		return glosses;

	}

	public HashSet<String> getBinomialNomenclatureHashSet() {
		return binomialNomenclature;
	}

	// //////////////////
	// PRIVATE METHODS //
	// //////////////////

	private void externalizeFile(String externalPath, String internalPath)
			throws IOException {
		File directory = new File("temp");
		if (!directory.exists()) {
			directory.mkdir();
		}
		File file = new File(externalPath);

		if (!file.exists()) {
			InputStream stream = Main.class.getClass().getResourceAsStream(
					internalPath);

			if (stream == null) {
			}
			OutputStream resStreamOut = null;
			int readBytes;
			byte[] buffer = new byte[4096];
			try {
				resStreamOut = new FileOutputStream(new File(externalPath));
				while ((readBytes = stream.read(buffer)) > 0) {
					resStreamOut.write(buffer, 0, readBytes);
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			} finally {
				stream.close();
				resStreamOut.close();
			}
		}
	}

	private static void loadWordNetNouns() {
		nounsList = new Hashtable<String, List<String>>();
		File file = new File("temp/index.noun");
		Scanner scanner;
		String line;
		String[] parts;
		String word;
		List<String> offsets = new ArrayList<String>();

		try {

			scanner = new Scanner(file);
			while (scanner.hasNextLine()) {

				offsets = new ArrayList<String>();

				// gets the line
				line = scanner.nextLine().trim();

				// splits the line
				parts = line.split(" ");

				// stores the word
				word = parts[0].replace("_", " ");

				// stores the offsets
				for (int i = 0; i < parts.length; i++) {
					if (parts[i].matches("^[0-9]{8}$")) {
						offsets.add(parts[i]);
					}
				}

				// adds elements to hashtable
				nounsList.put(word, offsets);

			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void loadWordNetGlosses() {
		glosses = new Hashtable<String, String>();
		File file = new File("temp/gloss.txt");
		Scanner scanner;
		String line;
		String[] parts;
		String offset;
		String gloss;

		try {

			scanner = new Scanner(file);
			while (scanner.hasNextLine()) {

				// gets the line
				line = scanner.nextLine().trim();

				// splits the line
				parts = line.split("\t");

				// stores the word
				offset = parts[0].replace("n", "");

				// stores the gloss
				gloss = parts[1];

				// adds elements to hashtable
				glosses.put(offset, gloss);

			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void loadBinomialNomenclature() {
		binomialNomenclature = new HashSet<String>();
		String line;
		try {

			BufferedReader br = new BufferedReader(new FileReader(
					"temp/binomial_nomenclature.txt"));

			try {
				while ((line = br.readLine()) != null) {

					// gets the line
					line = line.trim();

					// adds elements to hash set
					binomialNomenclature.add(line);

				}
			} catch (IOException e1) {
				e1.printStackTrace();
			} finally {
				br.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
