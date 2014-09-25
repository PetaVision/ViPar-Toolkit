package dannydelott.vinefetch;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;

import com.eclipsesource.json.JsonObject;

/**
 * A small program to fetch the files located in a JSON file. There must be only
 * one JSON object per line, and each line must have the filepath stored in the
 * field "download_url".
 * 
 * 
 * @author dannydelott
 * 
 */
public class Main {

	/**
	 * Creates a timestamped output directory from user input, then fetches the
	 * download url and the (.mp4) file located there.
	 * 
	 * Ex: java -jar fetch.jar vipar-result.json "dog-vines" false
	 * 
	 * @param args
	 *            [0] filepath to JSON file to parse<br />
	 *            [1] name of output directory, be descriptive!<br />
	 *            [2] print the html or not of each vine page
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		System.out.println();
		Timer timer = new Timer();
		timer.begin();

		// --------------------------------
		// 1. Set user inputs and timestamp
		// --------------------------------

		String resultsFile = args[0];
		String saveDirectory = args[1];
		String printHtml = args[2];

		long timestamp = System.currentTimeMillis();

		// ---------------------------
		// 2. Creates output directory
		// ---------------------------

		String outputDirectory = saveDirectory + "." + timestamp;
		createDirectory(outputDirectory);
		File outputResultsFile = new File(outputDirectory + "/vines.json");

		System.out.println("SAVE DIRECTORY:\t\t" + outputDirectory);

		// --------------------------------------------
		// 3. Loops over resultsFile and fetches videos
		// --------------------------------------------

		File file = new File(resultsFile);

		// counts number to download
		System.out.println("QUANTITY TO DOWNLOAD:\t" + countLines(resultsFile));

		// begins downloading
		int count = 0;
		int downloaded = 0;
		Scanner scanner = new Scanner(file);
		System.out.println("\nBEGIN DOWNLOADING\n");
		while (scanner.hasNextLine()) {

			// creates the json object from line
			JsonObject json = JsonObject.readFrom(scanner.nextLine());
			count++; // increments vine counter

			// gets the object id and prints out downloading prompt
			String id = json.get("id").asString();
			System.out.print("[" + count + "]\t\t" + id + "...");

			// gets the download url
			String downloadUrl;
			downloadUrl = parseVideoUrl(json.get("url").asString(), printHtml);
			if (downloadUrl == null) {
				System.out.println("video not found.");
				continue;
			}

			// adds the download url to the json object
			json.add("download_url", downloadUrl);

			// creates the File object for grabbing
			File f = new File(outputDirectory + "/" + id + ".mp4");

			// fetches the file
			URL url = new URL(downloadUrl);
			FileUtils.copyURLToFile(url, f);

			// adds json line to results json file
			FileUtils.writeStringToFile(outputResultsFile, json.toString()
					+ "\n", true);

			// completes the downloading prompt
			System.out.println("complete.");
			downloaded++;
		}

		timer.end();

		System.out.println("\nDOWNLOAD COMPLETE\n");
		System.out.println("FILE DOWNLOADED:\t\t" + downloaded + " of " + count
				+ "\n");
		timer.printFormattedExecutionTime();
	}

	private static void createDirectory(String path) {
		File f = new File(path);
		if (!f.isDirectory()) {
			f.mkdir();
		}
	}

	/**
	 * Parses the given url and returns a String containing the download url to
	 * the video.
	 * 
	 * @return String containing the .mp4 url to the vine video
	 * 
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws NullPointerException
	 */
	public static String parseVideoUrl(String url, String printHtml) {

		// gets the url's html
		String html = null;

		try {
			html = sendGet(url);
			if (printHtml.toLowerCase().contentEquals("true")) {
				System.out.println(html);
			}
		} catch (IOException e) {
			return null;
		}

		// if 404 or the url suffix is missing from the html
		if (html == null || html.contains("Page not found Ñ Vine")
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

	public static int countLines(String filename) throws IOException {
		InputStream is = new BufferedInputStream(new FileInputStream(filename));
		try {
			byte[] c = new byte[1024];
			int count = 0;
			int readChars = 0;
			boolean empty = true;
			while ((readChars = is.read(c)) != -1) {
				empty = false;
				for (int i = 0; i < readChars; ++i) {
					if (c[i] == '\n') {
						++count;
					}
				}
			}
			return (count == 0 && !empty) ? 1 : count;
		} finally {
			is.close();
		}
	}
}
