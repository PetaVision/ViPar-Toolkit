package dannydelott.vinescraper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;

import org.apache.commons.io.FileUtils;

import com.eclipsesource.json.JsonObject;

public class ExportFile {

	public static void appendJsonObjectToFile(JsonObject j, String filepath) {

		String str = j.toString();

		try {
			FileUtils.writeStringToFile(new File(filepath), str + "\n", true);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void createDirectory(String path) {
		File f = new File(path);
		if (!f.isDirectory()) {
			f.mkdir();
		}
	}

	public static final void externalizeFile(String externalPath,
			String internalPath) throws IOException {
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

	public static void saveStringHashSet(HashSet<String> tags, String file) {

		try {
			FileUtils.writeLines(new File(file), tags, false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
