package dannydelott.vinescraper.bufferthread;

import java.util.HashSet;

import twitter4j.Status;
import cmu.arktweetnlp.Tagger;

import com.eclipsesource.json.JsonObject;

import dannydelott.vinescraper.Timer;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;

public class ProcessingBundle {

	// ///////////////////
	// GLOBAL VARIABLES //
	// ///////////////////

	private HashSet<Status> tweets;
	private BufferType bufferType;

	private String outputDirectory;
	private String outputFile;
	private int numVinesPerFile;
	private int numVinesInFile;

	private int numVinesScraped;
	private long numTweetsScraped;

	private JsonObject runJson;
	private Timer runTimer;

	private Tagger tagger;
	private LexicalizedParser lexicalizedParser;

	ProcessingFinishedListener finishedListener;

	// //////////////
	// CONSTRUCTOR //
	// //////////////

	public ProcessingBundle() {

	}

	// /////////////////
	// GLOBAL SETTERS //
	// /////////////////

	public void setTweets(HashSet<Status> tweets) {
		this.tweets = tweets;
	}

	public void setBufferType(BufferType bufferType) {
		this.bufferType = bufferType;
	}

	public void setOutputDirectory(String outputDirectory) {
		this.outputDirectory = outputDirectory;
	}

	public void setOutputFile(String outputFile) {
		this.outputFile = outputFile;
	}

	public void setNumVinesScraped(int numVinesScraped) {
		this.numVinesScraped = numVinesScraped;
	}

	public void setNumVinesInFile(int numVinesInFile) {
		this.numVinesInFile = numVinesInFile;
	}

	public void setNumVinesPerFile(int numVinesPerFile) {
		this.numVinesPerFile = numVinesPerFile;
	}

	public void setNumTweetsScraped(long numTweetsScraped) {
		this.numTweetsScraped = numTweetsScraped;
	}

	public void setRunJson(JsonObject runJson) {
		this.runJson = runJson;
	}

	public void setRunTimer(Timer runTimer) {
		this.runTimer = runTimer;
	}

	public void setTagger(Tagger tagger) {
		this.tagger = tagger;
	}

	public void setLexicalizedParser(LexicalizedParser lexicalizedParser) {
		this.lexicalizedParser = lexicalizedParser;
	}

	// //////////////////
	// GLOBAL GETTTERS //
	// //////////////////

	public HashSet<Status> getTweets() {
		return tweets;
	}

	public BufferType getBufferType() {
		return bufferType;
	}

	public String getOutputDirectory() {
		return outputDirectory;
	}

	public String getOutputFile() {
		return outputFile;
	}

	public int getNumVinesScraped() {
		return numVinesScraped;
	}

	public int getNumVinesInFile() {
		return numVinesInFile;
	}

	public int getNumVinesPerFile() {
		return numVinesPerFile;
	}

	public long getNumTweetsScraped() {
		return numTweetsScraped;
	}

	public JsonObject getRunJson() {
		return runJson;
	}

	public Timer getRunTimer() {
		return runTimer;
	}

	public Tagger getTagger() {
		return tagger;
	}

	public LexicalizedParser getLexicalizedParser() {
		return lexicalizedParser;
	}

}
