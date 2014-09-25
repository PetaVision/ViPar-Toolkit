package dannydelott.vinescrape;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.eclipsesource.json.JsonObject;

import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterStreamBuilderUtil {

	public static TwitterStream getStream() {

		List<String> objects = getJsonObjectsFromFileAsString("twitter_credentials.json");

		JsonObject credentials = JsonObject.readFrom(objects.get(0));

		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true);
		cb.setOAuthConsumerKey(credentials.get("consumer_key").asString());
		cb.setOAuthConsumerSecret(credentials.get("consumer_secret").asString());
		cb.setOAuthAccessToken(credentials.get("access_token").asString());
		cb.setOAuthAccessTokenSecret(credentials.get("access_secret")
				.asString());

		TwitterStream t = new TwitterStreamFactory(cb.build()).getInstance();

		return t;

	}

	private static List<String> getJsonObjectsFromFileAsString(String f) {
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

	private static boolean isSkipChar(String l) {
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

}
