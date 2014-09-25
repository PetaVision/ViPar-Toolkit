package dannydelott.vinescraper.hashtags;

import java.util.List;

public class HashTagEvaluator {

	private static List<String> listOfTokens;
	private static String text;

	// //////////////
	// CONSTRUCTOR //
	// //////////////

	public HashTagEvaluator(List<String> k) {
		listOfTokens = k;
		text = listOfTokensToString(listOfTokens);
	}

	/*
	 * Returns true if tokens list contains only hash tags.
	 */
	public boolean isChainOfHashTagsUrlsAndAtMentions() {
		for (int i = 0; i < listOfTokens.size(); i++) {
			if (!isTokenHashTag(i) && !isTokenUrl(i) && !isTokenAtMention(i)) {
				return false;
			}
		}

		return true;
	}

	/*
	 * Converts a list of tokens into a string.
	 */
	public String listOfTokensToString(List<String> list) {
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

	public boolean isTokenHashTag(int i) {
		if (listOfTokens.get(i).startsWith("#")) {
			return true;
		}
		return false;
	}

	public boolean isTokenPunctuation(int i) {
		if (listOfTokens.get(i).matches("\\p{Punct}+")) {
			return true;
		}
		return false;
	}

	public boolean isTokenAtMention(int i) {
		if (listOfTokens.get(i).startsWith("@")) {
			return true;
		}
		return false;
	}

	public boolean isTokenUrl(int i) {
		if (listOfTokens.get(i).contains("http")) {
			return true;
		}
		return false;
	}

	public boolean isTokenWord(int i) {
		if (!isTokenHashTag(i) && !isTokenPunctuation(i)
				&& !isTokenAtMention(i)) {
			return true;
		}
		return false;
	}

	public String getText() {
		return text;
	}
}
