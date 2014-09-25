package dannydelott.vinescraper.hashtags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.math.NumberUtils;

import cmu.arktweetnlp.Tagger.TaggedToken;
import dannydelott.vinescraper.bufferthread.TagType;
import dannydelott.vinescraper.bufferthread.Vine;

public class HashTagSegmenter {

	// ///////////////////
	// GLOBAL VARIABLES //
	// ///////////////////

	/**
	 * List of words used for segmenting hashtags.
	 */
	private HashSet<String> largeWordList;

	private List<TaggedToken> taggedTokens = new ArrayList<TaggedToken>();
	private boolean canSegment = true;

	// //////////////
	// CONSTRUCTOR //
	// //////////////

	public HashTagSegmenter(HashSet<String> largeWordList,
			List<TaggedToken> taggedTokens) {

		this.largeWordList = largeWordList;
		this.taggedTokens = taggedTokens;
	}

	// /////////////////
	// PUBLIC METHODS //
	// /////////////////

	/**
	 * Segments the tokens that are hashtags in between words or punctuation.
	 */
	public String segmentInteriorHashTags() throws IOException {

		// separates tokens from taggedTokens
		List<String> tokens = Vine.getPosTagsOrTokens(TagType.TOKEN,
				taggedTokens);

		HashTagEvaluator hte = new HashTagEvaluator(tokens);

		int firstWord = 0;
		int lastWord = 0;

		// finds first word
		for (int i = 0; i < tokens.size(); i++) {
			if (hte.isTokenWord(i)) {
				firstWord = i;
				break;
			}
		}

		// finds last word (could also be a punctuation mark)
		for (int i = tokens.size() - 1; i >= 0; i--) {
			if (hte.isTokenWord(i)) {
				lastWord = i;
				break;
			}

			if (i - 1 > 0 && !hte.isTokenHashTag(i - 1)
					&& hte.isTokenPunctuation(i)) {
				lastWord = i;
				break;
			}
		}

		// segments interior hash tags
		for (int i = 0; i < tokens.size(); i++) {

			// token is a hashtag, checks
			if (hte.isTokenHashTag(i) && (i > firstWord && i <= lastWord)) {

				// segments token
				List<String> segmentedToken = segmentHashTag(tokens.get(i));

				if (segmentedToken == null) {
					canSegment = false;
					return null;
				}

				// removes hashtagged token from list
				tokens.remove(i);

				// adds segments to the list in place of hashtagged token
				for (int j = 0; j < segmentedToken.size(); j++) {
					tokens.add(i + j, segmentedToken.get(j));
				}
			}

		}

		return listOfTokensToString(tokens);

	}

	public void printList(List<String> list) {
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
	}

	// /////////////////
	// GLOBAL GETTERS //
	// /////////////////

	public boolean getCanSegment() {
		return canSegment;
	}

	// //////////////////
	// PRIVATE METHODS //
	// //////////////////

	/**
	 * Segments the words of the token (eg: "#iwant2eatfood")
	 */
	private List<String> segmentHashTag(String text) {

		// holds crude segments from number split
		List<String> crudeSegments = new ArrayList<String>();

		// holds completely segmented tokens
		List<String> tempSegments = new ArrayList<String>();
		List<String> finalSegments = new ArrayList<String>();

		// sets the token to lower case
		StringBuilder tokenText = new StringBuilder(text.toLowerCase());

		// checks for hashtag
		if (tokenText.charAt(0) == '#') {

			// deletes the hashtag char
			tokenText = tokenText.deleteCharAt(0);

			// splits the token text into crude segments when a number exists
			// eg: "iwant2eatfood" -> ['iwant', '2', 'eatfood']
			Matcher m = Pattern.compile("[\\d.]+|\\D+").matcher(tokenText);
			while (m.find()) {
				crudeSegments.add(m.group());
			}

			// segments items from crude segments list
			// eg: temp[0] = ['iwant'] ->
			// segments = ['i','want']
			for (int i = 0; i < crudeSegments.size(); i++) {

				// if crude item is a number, add it to the segments list
				if (NumberUtils.isNumber(crudeSegments.get(i))) {
					finalSegments.add(crudeSegments.get(i));
				} else {

					// if crude item is not a number, segment and add each
					// new item to the segments list
					tempSegments = getSegments(crudeSegments.get(i));

					// adds new segments list to final segments
					if (tempSegments != null) {
						for (int j = 0; j < tempSegments.size(); j++) {
							finalSegments.add(tempSegments.get(j));
						}
					} else {
						// adds crude segment to list if it cannot be segmented
						return null;
					}
				}
			}

		}

		return finalSegments;
	}

	/**
	 * Takes in a text (eg: "iwant2eatfood") and returns the segments as a list
	 * of strings. Returns null if unable to segment.
	 */
	private List<String> getSegments(String text) {
		List<String> segments = new ArrayList<String>();
		String currentSegment = "";
		StringBuilder trimmedText = new StringBuilder(text);
		StringBuilder finalText = new StringBuilder(text);
		boolean foundLastWord = true;

		while (trimmedText.length() >= 0) {

			// returns text if text is empty or the last word is not found
			if ((trimmedText.length() == 0 && segments.size() == 0)
					|| foundLastWord == false) {
				segments.clear();
				segments.add(text);
				return segments;
			}
			// returns the segments if crude segment text has no more characters
			else if (trimmedText.length() == 0 && segments.size() > 0) {
				return segments;
			}
			// segments the crude segment text if not empty
			else if (trimmedText.length() > 0) {

				// adds text to segments list if text exists in Hashtable
				if (largeWordList.contains(trimmedText.toString())) {

					// stores the segment for easy removal
					currentSegment = trimmedText.toString();

					// adds it to list
					segments.add(currentSegment);

					// deletes the current segment from front of finalText
					finalText = new StringBuilder(finalText.delete(0,
							currentSegment.length()));

					// resets newText
					trimmedText = new StringBuilder(finalText.toString());

				}
				// trims last letter of crude segment text if key doesn't exist
				else {

					trimmedText = trimmedText
							.deleteCharAt(trimmedText.length() - 1);

					if (trimmedText.length() == 0) {
						foundLastWord = false;
					}
				}
			}
		}

		// returns null if unable to segment
		return null;

	}

	/**
	 * Converts a list of tokens into a string.
	 */
	private String listOfTokensToString(List<String> list) {
		String temp = "";
		for (int i = 0; i < list.size(); i++) {

			// if the next token is a punctuation mark
			if ((i + 1 < list.size())
					&& list.get(i + 1).matches(("\\p{Punct}+"))) {

				temp = temp + list.get(i);

			} else {
				temp = temp + list.get(i) + " ";
			}
		}

		return temp.trim();
	}

}
