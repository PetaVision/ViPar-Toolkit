package dannydelott.vinefilter.settings;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.eclipsesource.json.ParseException;

public final class Setup {

	// //////////////////
	// PUBLIC METHODS //
	// //////////////////

	public static List<String> getSettingsfilePaths() {

		// get list of files
		File folder = new File(System.getProperty("user.dir"));
		File[] listOfFiles = folder.listFiles();

		ArrayList<String> files = new ArrayList<String>();

		// check files for "tw-" prefix
		for (int i = 0; i < listOfFiles.length; i++) {

			if (listOfFiles[i].isFile()) {
				if (listOfFiles[i].getName().startsWith("s-")) {
					files.add(listOfFiles[i].getName());
				}
			}
		}

		return files;
	}

	public static List<JsonObject> getJsonObjectsFromFile(String f) {

		// holds the current json object from the list
		JsonObject temp;

		// holds the JsonObject set to be returned
		List<JsonObject> list = new ArrayList<JsonObject>();

		// builds list of json objects from the file
		List<String> objects = getJsonObjectsFromFileAsString(f);

		// loops through objects list
		for (String object : objects) {

			try {

				// makes object
				temp = JsonObject.readFrom(object);

				// add json object to set;
				list.add(temp);

			} catch (ParseException e) {
				return null;
			} catch (UnsupportedOperationException e) {
				return null;
			}

		}
		return list;

	}

	public static List<String> getJsonObjectsFromFileAsString(String f) {
		BufferedReader br;
		FileReader fr;
		File file = new File(f);
		String line;
		String entry = null;
		boolean newEntry;

		List<String> s = new ArrayList<String>();

		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);

			// read in first line of file
			while ((line = br.readLine()) != null) {

				line = line.trim();

				// skips comments and empty new lines
				if (isSkipChar(line)) {
					continue;
				}

				// handles new entry
				if (line.startsWith("{")) {
					newEntry = true;
					entry = line;

					// does next line if single-line entry
					if (line.endsWith("}")) {

						// adds entry
						entry = entry.replace("\t", " ");
						s.add(entry);
						continue;
					}

					// handles multi-line contents
					while (newEntry && (line = br.readLine()) != null) {

						// skips new lines and comments
						if (isSkipChar(line)) {
							continue;
						}

						entry = entry + line;

						if (line.endsWith("}")) {

							// adds entry
							entry = entry.replace("\t", " ");
							s.add(entry);
							newEntry = false;
						}
					}
				}

				// next entry
				newEntry = true;
			}
			fr.close();
			br.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return s;

	}

	public static List<String> convertJsonArrayToStringList(JsonArray j) {

		List<String> temp = new ArrayList<String>();

		Iterator<JsonValue> i = j.iterator();
		while (i.hasNext()) {
			JsonValue value = i.next();
			if (value.isString()) {
				temp.add(value.asString());
			} else {
				return null;
			}
		}

		return temp;
	}

	public static boolean isSkipChar(String l) {
		if (l == null) {
			return false;
		}
		String line = l.trim();
		if (line.equals("\n"))
			return true;
		else if (line.startsWith("//"))
			return true;
		else
			return false;

	}

	public static String sendGet(String u) throws IOException {

		String url = u;
		String USER_AGENT = "Mozilla/5.0";
		URL obj = new URL(url);

		try {
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// handles the response code 301 redirect from vine URLs
			String redirect = con.getHeaderField("Location");
			if (redirect != null) {
				obj = new URL(redirect);
				con = (HttpURLConnection) obj.openConnection();
			}

			// optional default is GET
			con.setRequestMethod("GET");

			// add request header
			con.setRequestProperty("User-Agent", USER_AGENT);

			int responseCode = con.getResponseCode();

			// System.out.println("\nSending 'GET' request to URL : " + url);
			// System.out.println("Response Code : " + responseCode);

			if (responseCode > 200) {
				return null;
			}

			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
			return response.toString();

		} catch (Exception e) {
			return null;
		}

	}

}
