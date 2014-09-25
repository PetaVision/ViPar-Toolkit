package dannydelott.inwlg;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.eclipsesource.json.JsonObject;

public class OutputLog {

	private File filePath;

	// //////////////
	// CONSTRUCTOR //
	// //////////////

	public OutputLog(String path) throws IOException {
		filePath = new File(path);
	}

	// /////////////////
	// PUBLIC METHODS //
	// /////////////////

	public void writeJsonItemToFile(JsonItem j) throws IOException {
		JsonObject json = j.getJsonObject();

		FileWriter fw = new FileWriter(filePath, true);
		BufferedWriter writer = new BufferedWriter(fw);

		try {
			fw.append(json.toString() + "\n");
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			writer.close();
			fw.close();
		}

	}

}
