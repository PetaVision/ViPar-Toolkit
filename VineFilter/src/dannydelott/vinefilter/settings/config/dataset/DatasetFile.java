package dannydelott.vinefilter.settings.config.dataset;

import dannydelott.vinefilter.settings.Setup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.eclipsesource.json.JsonObject;

public class DatasetFile {

	// ////////////////////
	// GLOBAL VARIABLES //
	// ///////////////////

	private String filePath;

	private BufferedReader br;
	private FileReader fr;
	private File file;

	private boolean flagError;

	// //////////////////////
	// FACTORY CONSTRUCTOR //
	// //////////////////////

	public static DatasetFile newInstance(String fp) {
		DatasetFile d = new DatasetFile(fp);
		if (d.getFlagError()) {
			return null;
		}
		return d;
	}

	// //////////////
	// CONSTRUCTOR //
	// //////////////

	private DatasetFile(String fp) {
		filePath = fp;
		file = new File(filePath);
		openDatasetFileStream();
	}

	// /////////////////
	// PUBLIC METHODS //
	// /////////////////

	public Vine nextVine() {

		String line = "";
		boolean newEntry;
		String entry = null;
		JsonObject j;
		Vine v;
		try {

			// gets a line that is a valid entry
			while (line.isEmpty() || line != null) {

				line = br.readLine();
				if (line == null) {
					return null;
				} else if (Setup.isSkipChar(line)) {
					line = "";
				} else {
					break;
				}

			}

			// handles new entry
			if (line.startsWith("{")) {
				newEntry = true;
				entry = line;

				// does next line if single-line entry
				if (line.endsWith("}")) {

					// returns vine
					entry = entry.replace("\t", " ");
					j = JsonObject.readFrom(entry);
					v = Vine.newInstance(j);
					if (v == null) {
						return null;
					} else {
						return v;
					}

				}

				// handles multi-line contents
				while (newEntry && (line = br.readLine()) != null) {

					// skips new lines and comments
					if (Setup.isSkipChar(line)) {
						continue;
					}

					entry = entry + line;

					if (line.endsWith("}")) {

						// adds entry
						entry = entry.replace("\t", " ");
						j = JsonObject.readFrom(entry);
						
						v = Vine.newInstance(j);
						if (v == null) {
							return null;
						} else {
							return v;
						}
					}
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	private void openDatasetFileStream() {

		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void closeDatasetFileStream() {
		try {
			fr.close();
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean getFlagError() {
		return flagError;
	}
}
