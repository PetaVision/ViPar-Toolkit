package dannydelott.inwlg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

public class ImageNetAPI {

	private Hashtable<String, String> glosses;

	public ImageNetAPI(Hashtable<String, String> g) {
		glosses = g;
	}

	// /////////////////
	// PUBLIC METHODS //
	// /////////////////

	public List<String> getWordsFromOffset(String offset) {
		// get the html
		String html = sendGet(
				"http://www.image-net.org/api/text/wordnet.synset.getwords?wnid=n"
						+ offset).toLowerCase();
		if (html.contains("invalid url!")) {
			return null;
		}
		List<String> words = new ArrayList<String>(Arrays.asList(html
				.split("\n")));
		return words;
	}

	public List<String> getHyponymOffsetsFromOffset(String offset,
			int synset_type) {
		String html = null;
		if (synset_type == 1) {
			html = sendGet("http://www.image-net.org/api/text/wordnet.structure.hyponym?wnid=n"
					+ offset);
		} else if (synset_type == 2) {
			html = sendGet("http://www.image-net.org/api/text/wordnet.structure.hyponym?wnid=n"
					+ offset + "&full=1");
		}
		html = html.replace("-n", "");
		List<String> hyponyms = new ArrayList<String>(Arrays.asList(html
				.split("\n")));
		return hyponyms;
	}

	public String getGlossFromOffset(String offset) {
		if (glosses.containsKey(offset)) {
			return glosses.get(offset);
		}
		return null;
	}

	// //////////////////
	// PRIVATE METHODS //
	// //////////////////

	// HTTP GET request
	private String sendGet(String urlToRead) {
		URL url;
		HttpURLConnection conn;
		BufferedReader rd;
		String line;
		String result = "";
		try {
			url = new URL(urlToRead);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			rd = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			while ((line = rd.readLine()) != null) {
				result += line + "\n";
			}
			rd.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
