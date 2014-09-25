package dannydelott.vinescraper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HtmlParser {

	/**
	 * 
	 * Parses the given url and returns a String containing the download url to
	 * the video.
	 * 
	 * @return String containing the .mp4 url to the vine video
	 * 
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws NullPointerException
	 */
	public static String parseVideoUrl(String html) {

		// if 404 or the url suffix is missing from the html
		if (html == null || html.contains("Page not found")
				|| html.contains("<title>Vine</title>")) {
			return null;
		}

		// parses out the .mp4 file path
		String[] split = html.split("<meta itemprop=\"contentURL\" content=\"");
		split = split[1].split("\\?versionId");
		return split[0];

	}

	public static String sendGet(String u) throws IOException {

		String url = u;
		String USER_AGENT = "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2049.0 Safari/537.36";
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

	public static String parseText(String html) {

		// if 404 or the url suffix is missing from the html
		if (html == null || html.contains("Page not found")
				|| html.contains("<title>Vine</title>")) {
			return null;
		}

		// parses out the description
		String[] split = html.split("description\": \"");
		split = split[1].split("\", \"");
		return split[0];

	}

}
