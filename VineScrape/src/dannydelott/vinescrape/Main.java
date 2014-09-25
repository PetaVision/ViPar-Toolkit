package dannydelott.vinescrape;

import java.io.File;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.HashSet;

import org.apache.commons.io.FileUtils;

import cmu.arktweetnlp.Tagger;

import com.eclipsesource.json.JsonObject;

import dannydelott.vinescrape.bufferthread.BufferType;
import dannydelott.vinescrape.bufferthread.ProcessingBundle;
import dannydelott.vinescrape.bufferthread.ProcessingFinishedListener;
import dannydelott.vinescrape.bufferthread.ProcessingThread;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;

public class Main {

	// ------------------
	// RUN TIME ARGUMENTS
	// ------------------

	// instantiates default values
	private static int MAX_VINES_TO_SCRAPE = Defaults.MAX_VINES_TO_SCRAPE;
	private static int MAX_TWEETS_TO_SCRAPE = Defaults.MAX_TWEETS_TO_SCRAPE;
	private static int VINES_PER_FILE = Defaults.VINES_PER_FILE;

	// ---------------------------
	// HASHTAG SEGMENTER WORD LIST
	// ---------------------------

	private static HashSet<String> largeWordList;

	// --------------------------------
	// POS TAGGER AND DEPENDENCY PARSER
	// --------------------------------

	private static Tagger tagger;
	private static HashSet<String> posTags;

	private static LexicalizedParser lexicalizedParser;
	private static HashSet<String> relationTags;

	// ----------------
	// OUTPUT LOCATION
	// ----------------

	// holds the generated output directory
	// eg: "vines.1411006480/"
	private static String outputDirectory;

	// holds the generated current output file
	// eg: "vines.0.json"
	private static String outputFile;

	// holds the number vines in the current output file
	private static int numVinesInFile;

	// -----------
	// TWITTER API
	// -----------

	private static TwitterStream ts;
	private static HashSet<String> urls;

	// ------------------
	// VALUES IN run.json
	// ------------------

	private static int numVinesScraped;
	private static long numTweetsScraped;
	private static Timer runTimer;
	private static JsonObject runJson;

	// ------------
	// BUFFER LOGIC
	// ------------

	private static HashSet<Status> rawTweets_A;
	private static HashSet<Status> rawTweets_B;
	private static boolean isProcessing_A;
	private static boolean isProcessing_B;
	private static BufferType currentBuffer;
	private static ProcessingFinishedListener finishedListener = new ProcessingFinishedListener() {

		@Override
		public void onProcessFinished(int vinesScraped, int vinesInFile,
				String output, BufferType buffer,
				HashSet<String> duplicateUrls, HashSet<String> pos,
				HashSet<String> relations) {

			// -----------------
			// UPDATES HASH SETS
			// -----------------

			synchronized (this) {

				urls = duplicateUrls;

				if (posTags.addAll(pos)) {
					ExportFile.saveStringHashSet(posTags, outputDirectory + "/"
							+ "pos_tags.txt");
				}

				if (relationTags.addAll(relations)) {
					ExportFile.saveStringHashSet(relationTags, outputDirectory
							+ "/" + "relation_tags.txt");
				}

			}

			// -------------------
			// UPDATES OUTPUT FILE
			// -------------------

			numVinesInFile = vinesInFile;
			outputFile = output;

			// --------------------------------------------
			// PRINTS CURRENT TOTAL NUMBER OF VINES SCRAPED
			// --------------------------------------------

			numVinesScraped = vinesScraped;
			System.out.println("TOTAL VINES:\t" + vinesScraped);

			// ------------------------------------
			// CLEARS THE BUFFER THAT WAS PROCESSED
			// ------------------------------------

			switch (buffer) {
			case A:
				rawTweets_A.clear();
				isProcessing_A = false;
				return;
			case B:
				rawTweets_B.clear();
				isProcessing_B = false;
				break;
			}

			// -------------------------------------------------------------
			// STOPS TWITTER API ONCE NUMBER OF VINES TO DOWNLOAD IS REACHED
			// -------------------------------------------------------------

			if (vinesScraped >= MAX_VINES_TO_SCRAPE) {

				// ends timer and prints final execution time
				runTimer.end();
				runTimer.printFormattedExecutionTime();

				// stops twitter api
				ts.cleanUp();
				ts.shutdown();

			}

		}
	};

	// ---------------------
	// THREAD ERROR HANDLING
	// ---------------------

	private static UncaughtExceptionHandler threadHandler = new Thread.UncaughtExceptionHandler() {
		@Override
		public void uncaughtException(Thread t, Throwable e) {

			e.printStackTrace();
		}
	};

	// ///////
	// MAIN //
	// ///////

	/**
	 * Launches an instance of the Twitter Streaming API and begins scraping all
	 * tweets into a HashSet<Status> buffer. Once the Twitter Streaming API
	 * begins to rate limit the stream, we send the current tweets to a new
	 * thread for processing where it is determined if the tweets are vines.
	 * 
	 * @param args
	 *            [0] vines to scrape (Default: 10,000,000)
	 * 
	 * @param args
	 *            [1] tweets to scrape (Default: -1 for infinite)
	 * 
	 * @param args
	 *            [2] number of vines to output per file (Default: 1000)
	 */
	public static void main(String[] args) {

		// ------------
		// 1.
		// STARTS TIMER
		// ------------

		runTimer = new Timer();
		runTimer.begin();
		String startDate = runTimer.getStartDate();

		// -----------------------------------------------------------------
		// 2.
		// INSTANTIATES RUNTIME ARGUMENTS, POS TAGGER, AND DEPENDENCY PARSER
		// READS IN HASHTAG SEGMENTER WORD LIST
		// -----------------------------------------------------------------

		instantiateRuntimeArguments(args);
		instantiatePosTagger();
		instantiateGrammarDependencyParser();
		instantiateLargeWordList();

		// -------------------------------------
		// 3.
		// INITIALIZES OUTPUT DIRECTORY AND FILE
		// -------------------------------------

		outputDirectory = "vines." + (System.currentTimeMillis() / 1000);
		ExportFile.createDirectory(outputDirectory);
		outputFile = outputDirectory + "/" + "vines." + numVinesScraped
				+ ".json";

		// -----------------------------------
		// 4.
		// INITIALIZES BUFFERS AND URL HASHSET
		// -----------------------------------

		rawTweets_A = new HashSet<Status>();
		rawTweets_B = new HashSet<Status>();
		isProcessing_A = false;
		isProcessing_B = false;
		currentBuffer = BufferType.A;
		numVinesScraped = 0;
		numTweetsScraped = 0;
		urls = new HashSet<String>();
		posTags = new HashSet<String>();
		relationTags = new HashSet<String>();

		// --------------
		// 5.
		// SAVES run.json
		// --------------

		runJson = new JsonObject();
		runJson.add("start_date", startDate);
		try {
			FileUtils.writeStringToFile(
					new File(outputDirectory + "/run.json"),
					runJson.toString(), false);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// -------------------
		// 6.
		// SETS UP TWITTER API
		// -------------------

		// gets the Streaming API
		ts = TwitterStreamBuilderUtil.getStream();

		// sets keyword to track
		FilterQuery fq = new FilterQuery();
		String keyword[] = { "http" };
		fq.track(keyword);

		// adds the streaming listener
		ts.addListener(new StatusListener() {
			public void onStatus(Status status) {

				if (numTweetsScraped == MAX_TWEETS_TO_SCRAPE) {
					runTimer.end();
					runTimer.printFormattedExecutionTime();

					ts.cleanUp();
					ts.shutdown();

					System.exit(0);
				}

				// fills rawTweets_A
				if ((currentBuffer == BufferType.A) && !isProcessing_A) {

					// adds tweet to buffer if not full
					if (rawTweets_A.size() < Defaults.MAX_BUFFER_SIZE) {
						rawTweets_A.add(status);
						numTweetsScraped++;
						return;
					}

					// switch buffer to rawTweets_B and
					// processes buffer in ProcessingThread class
					if (rawTweets_A.size() == Defaults.MAX_BUFFER_SIZE) {
						processCurrentBuffer();
					}
				}

				// fills rawTweets_B
				if ((currentBuffer == BufferType.B) && !isProcessing_B) {

					// adds tweet to buffer if not full
					if (rawTweets_B.size() < Defaults.MAX_BUFFER_SIZE) {
						rawTweets_B.add(status);
						numTweetsScraped++;
						return;
					}

					// switches buffer to rawTweets_A and
					// processes buffer in ProcessingThread class
					if (rawTweets_B.size() == Defaults.MAX_BUFFER_SIZE) {
						processCurrentBuffer();
					}
				}

			}

			public void onDeletionNotice(
					StatusDeletionNotice statusDeletionNotice) {
			}

			public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
				processCurrentBuffer();
			}

			public void onException(Exception ex) {
				ex.printStackTrace();
			}

			@Override
			public void onScrubGeo(long arg0, long arg1) {
			}

			@Override
			public void onStallWarning(StallWarning arg0) {
				System.out.println(arg0.getMessage());
			}
		});

		// ---------------
		// 7.
		// RUN TWITTER API
		// ---------------

		// filters tweets by keyword and sends them to listener
		ts.filter(fq);

	}

	// //////////////////
	// PRIVATE METHODS //
	// //////////////////

	private static void instantiateLargeWordList() {

		try {
			ExportFile.externalizeFile("temp/large-word-list.txt",
					"/large-word-list.txt");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		File file = new File("temp/large-word-list.txt");
		try {
			largeWordList = new HashSet<String>(FileUtils.readLines(file));
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
	}

	/**
	 * 
	 * Takes in the string array of runtime arguments and assigns them to their
	 * global variables.
	 * 
	 * @param args
	 *            [0] vines to scrape (Default: 10,000,000)
	 * @param args
	 *            [1] tweets to scrape (Default: -1 for infinite)
	 * @param args
	 *            [2] number of vines to output per file (Default: 1000)
	 */
	private static void instantiateRuntimeArguments(String[] args) {
		if (args.length == 1) {
			MAX_VINES_TO_SCRAPE = Integer.parseInt(args[0]);
		} else if (args.length == 2) {
			MAX_VINES_TO_SCRAPE = Integer.parseInt(args[0]);
			MAX_TWEETS_TO_SCRAPE = Integer.parseInt(args[1]);
		} else if (args.length == 3) {
			MAX_VINES_TO_SCRAPE = Integer.parseInt(args[0]);
			MAX_TWEETS_TO_SCRAPE = Integer.parseInt(args[1]);
			VINES_PER_FILE = Integer.parseInt(args[2]);
		}
	}

	/**
	 * Instantiates the ARK TweetNLP part-of-speech tagger.
	 */
	private static void instantiatePosTagger() {

		// -------------------------
		// EXTERNALIZES TAGGER MODEL
		// -------------------------

		try {
			ExportFile.externalizeFile("temp/tagger-model.txt",
					"/tagger-model.txt");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// -------------------
		// INSTANTIATES TAGGER
		// -------------------

		tagger = new Tagger();
		try {
			// loads Penn Treebank style tags
			tagger.loadModel("temp/tagger-model.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Instantiates the Stanford Grammar Dependency Parser.
	 */
	private static void instantiateGrammarDependencyParser() {

		// -------------------------------
		// INSTANTIATES LEXICALIZED PARSER
		// -------------------------------

		System.setProperty("parse.model",
				"edu/stanford/nlp/models/lexparser/englishPCFG.caseless.ser.gz");

		lexicalizedParser = LexicalizedParser
				.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.caseless.ser.gz");

	}

	/**
	 * Sends the currentBuffer to a thread for processing, setting the
	 * isProcessing boolean to true to prevent new statuses from being added to
	 * the buffer. Also switches the currentBuffer so that new statuses go to
	 * the other buffer while processing.
	 */
	private static void processCurrentBuffer() {

		// if both threads are currently processing, do nothing.
		if (isProcessing_A && isProcessing_B) {
			return;
		}

		switch (currentBuffer) {
		case A:

			// skips if buffer size is smaller than minimum buffer size
			if (rawTweets_A.size() < Defaults.MIN_BUFFER_SIZE) {
				return;
			}

			try {

				// sets to processing
				isProcessing_A = true;

				// creates processing bundle
				ProcessingBundle bundle_A = new ProcessingBundle();
				bundle_A.setTweets(rawTweets_A);
				bundle_A.setBufferType(BufferType.A);
				bundle_A.setOutputDirectory(outputDirectory);
				bundle_A.setOutputFile(outputFile);
				bundle_A.setNumVinesInFile(numVinesInFile);
				bundle_A.setNumVinesPerFile(VINES_PER_FILE);
				bundle_A.setNumVinesScraped(numVinesScraped);
				bundle_A.setNumTweetsScraped(numTweetsScraped);
				bundle_A.setRunTimer(runTimer);
				bundle_A.setRunJson(runJson);
				bundle_A.setTagger(tagger);
				bundle_A.setLexicalizedParser(lexicalizedParser);

				// changes current buffer to continue scraping
				currentBuffer = BufferType.B;

				// makes thread for processing buffer
				Thread thread1 = new Thread(new ProcessingThread(bundle_A,
						urls, largeWordList, finishedListener));
				thread1.setUncaughtExceptionHandler(threadHandler);

				// launches thread
				thread1.start();

				return;

			} catch (Throwable t) {
				t.printStackTrace();
			}

			break;
		case B:

			// skips if buffer size is smaller than minimum buffer size
			if (rawTweets_B.size() < Defaults.MIN_BUFFER_SIZE) {
				return;
			}

			try {

				// sets to processing
				isProcessing_B = true;

				// creates processing bundle
				ProcessingBundle bundle_B = new ProcessingBundle();
				bundle_B.setTweets(rawTweets_B);
				bundle_B.setBufferType(BufferType.B);
				bundle_B.setOutputDirectory(outputDirectory);
				bundle_B.setOutputFile(outputFile);
				bundle_B.setNumVinesInFile(numVinesInFile);
				bundle_B.setNumVinesPerFile(VINES_PER_FILE);
				bundle_B.setNumVinesScraped(numVinesScraped);
				bundle_B.setNumTweetsScraped(numTweetsScraped);
				bundle_B.setRunTimer(runTimer);
				bundle_B.setRunJson(runJson);
				bundle_B.setTagger(tagger);
				bundle_B.setLexicalizedParser(lexicalizedParser);

				// changes current buffer to continue scraping
				currentBuffer = BufferType.A;

				// makes thread for processing buffer
				Thread thread2 = new Thread(new ProcessingThread(bundle_B,
						urls, largeWordList, finishedListener));

				thread2.setUncaughtExceptionHandler(threadHandler);

				// launches thread
				thread2.start();

				return;

			} catch (Throwable t) {
				t.printStackTrace();
			}

			break;
		}

	}
}
