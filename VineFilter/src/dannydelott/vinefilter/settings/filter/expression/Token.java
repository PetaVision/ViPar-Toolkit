package dannydelott.vinefilter.settings.filter.expression;

import dannydelott.vinefilter.Messages;
import dannydelott.vinefilter.settings.SettingsFile;
import dannydelott.vinefilter.settings.filter.expression.values.Tokens;
import dannydelott.vinefilter.settings.filter.expression.values.TokenType;

public class Token {

	// ///////////////////
	// GLOBAL VARIABLES //
	// ///////////////////

	private SettingsFile settings;
	private String rawToken;

	private boolean hasNotOperator;
	private TokenType tokenType;
	private String tokenValue;

	private boolean flagError;

	// ///////////////
	// CONSTRUCTORS //
	// ///////////////

	public static Token newInstance(SettingsFile s, String t) {
		Token ret = new Token(s, t);
		if (ret.getFlagError()) {
			System.out.println("filter expression token is null");
			return null;
		}
		return ret;
	}

	private Token(SettingsFile s, String t) {
		settings = s;
		rawToken = t;
		parseToken();
	}

	public Token() {

	}

	// /////////////////
	// PUBLIC METHODS //
	// /////////////////

	/**
	 * Returns the {@code TokenType} of the raw token string.
	 * 
	 * @param t
	 *            raw token string
	 * @return {@code TokenType} contained in raw token string
	 */
	public static TokenType getTokenType(String t) {

		// @TargetWord
		if (t.contentEquals(Tokens.TARGET_WORD_TOKEN)) {
			return TokenType.TARGET_WORD;
		}

		// @TargetWord:NO_SPACES
		else if (t.contentEquals(Tokens.TARGET_WORD_NO_SPACES_TOKEN)) {
			return TokenType.TARGET_WORD_NO_SPACES;
		}

		// @Literal
		else if (t.startsWith(Tokens.LITERAL_TOKEN)) {
			return TokenType.LITERAL;
		}

		// @PosTag
		else if (t.startsWith(Tokens.POS_TAG_TOKEN)) {
			return TokenType.POS_TAG;
		}

		// @Strings
		else if (t.startsWith(Tokens.STRING_OBJECT_TOKEN)) {
			return TokenType.STRINGS_OBJECT;
		}

		// throws flag if token type cannot be parsed
		else {
			return null;
		}
	}

	// //////////////////
	// PRIVATE METHODS //
	// //////////////////

	private void parseToken() {

		String tempToken = rawToken;

		// parses out a possible not-operator and returns a substring
		// ex: tempToken = [@PosTag:VBG]
		tempToken = parseNotOperator(tempToken);

		// parses out the token type and returns a substring
		// ex: tempToken = VBG]
		tempToken = parseTokenType(tempToken);

		// returns if error or nothing is left in substring
		if (flagError || tempToken == null) {
			return;
		}

		parseTokenValue(tempToken);

	}

	private String parseNotOperator(String t) {

		String tempToken = t;

		// Checks first char in token for not-operator
		if (tempToken.charAt(0) == Tokens.NOT) {

			hasNotOperator = true;

			// chops off not-operator
			tempToken = tempToken.substring(1);

		} else {
			hasNotOperator = false;
		}

		return tempToken;

	}

	private String parseTokenType(String t) {

		String tempToken = t;

		// @TargetWord
		if (tempToken.contentEquals(Tokens.TARGET_WORD_TOKEN)) {
			tokenType = TokenType.TARGET_WORD;
			tokenValue = null;
			flagError = false;
			return null;
		}

		// @TargetWord:NO_SPACES
		else if (tempToken.contentEquals(Tokens.TARGET_WORD_NO_SPACES_TOKEN)) {
			tokenType = TokenType.TARGET_WORD_NO_SPACES;
			tokenValue = null;
			flagError = false;
			return null;
		}

		// @Literal
		else if (tempToken.startsWith(Tokens.LITERAL_TOKEN)) {
			tokenType = TokenType.LITERAL;

			// chops off "[@Literal:"
			tempToken = tempToken.substring(Tokens.LITERAL_TOKEN.length());
			return tempToken;

		}

		// @PosTag
		else if (tempToken.startsWith(Tokens.POS_TAG_TOKEN)) {
			tokenType = TokenType.POS_TAG;

			// chops off "[@PosTag:"
			tempToken = tempToken.substring(Tokens.POS_TAG_TOKEN.length());
			return tempToken;
		}

		// @Strings
		else if (tempToken.startsWith(Tokens.STRING_OBJECT_TOKEN)) {
			tokenType = TokenType.STRINGS_OBJECT;

			// chops off "[@Strings:"
			tempToken = tempToken
					.substring(Tokens.STRING_OBJECT_TOKEN.length());
			return tempToken;
		}

		// throws flag if token type cannot be parsed
		else {
			System.out.println(Messages.FilterExpressionToken_errorToken
					+ " (\"" + rawToken + "\")");
			flagError = true;
			return null;
		}
	}

	private void parseTokenValue(String t) {

		boolean isValid = false;

		String tempToken = t.replace("]", "");

		// @PosTag:VALUE
		if (tokenType == TokenType.POS_TAG) {

			isValid = settings.getRunConfiguration().containsPosTag(tempToken);

			if (isValid) {
				tokenValue = tempToken.toLowerCase();
			} else {
				System.out
						.println(Messages.FilterExpressionToken_errorTokenValue
								+ " (\"" + rawToken + "\")");
				flagError = true;
				return;
			}
		}

		// @Strings:VALUE
		if (tokenType == TokenType.STRINGS_OBJECT) {
			isValid = settings.containsStringObject(tempToken);

			if (isValid) {
				tokenValue = tempToken.toLowerCase();
			} else {
				System.out
						.println(Messages.FilterExpressionToken_errorTokenValue
								+ " (\"" + rawToken + "\")");
				flagError = true;
				return;
			}
		}

		// @Literal:VALUE
		if (tokenType == TokenType.LITERAL) {
			tokenValue = tempToken;
		}
	}

	// /////////////////
	// GLOBAL GETTERS //
	// /////////////////

	public boolean getFlagError() {
		return flagError;
	}

	public boolean hasNotOperator() {
		return hasNotOperator;
	}

	public TokenType getTokenType() {
		return tokenType;
	}

	public String getTokenValue() {
		return tokenValue;
	}

}
