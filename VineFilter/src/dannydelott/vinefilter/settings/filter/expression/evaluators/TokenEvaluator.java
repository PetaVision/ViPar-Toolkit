package dannydelott.vinefilter.settings.filter.expression.evaluators;

import java.util.ArrayList;
import java.util.List;

import dannydelott.vinefilter.settings.SettingsFile;
import dannydelott.vinefilter.settings.config.dataset.GrammarDependency;
import dannydelott.vinefilter.settings.config.dataset.TaggedToken;
import dannydelott.vinefilter.settings.config.dataset.Vine;
import dannydelott.vinefilter.settings.filter.FieldType;
import dannydelott.vinefilter.settings.filter.Filter;
import dannydelott.vinefilter.settings.filter.expression.Token;
import dannydelott.vinefilter.settings.filter.expression.values.TokenType;

public class TokenEvaluator {

	private SettingsFile settings;
	private Filter filter;
	private FieldType fieldType;
	private GrammarDependency relation;
	private Vine vine;
	private List<Token> tokens;
	private boolean notOperator;

	// //////////////
	// CONSTRUCTOR //
	// //////////////

	public TokenEvaluator(SettingsFile s, Filter f) {
		settings = s;
		filter = f;
	}

	// /////////////////
	// PUBLIC METHODS //
	// /////////////////

	public boolean evaluateTokens() {

		boolean isPosTagToken = false;
		List<String> possibleStrings = null;

		// ---------------------------------------------------------------
		// 1. Converts tokens into list of possible strings.
		//
		// EXAMPLE:
		// [@Literal:#][@TargetWord:NO_SPACES] => "#rhodesianridgeback"
		// [@Literal:my ][@Strings:DOG_BREEDS] => "my rhodesian ridgeback"
		// ---------------------------------------------------------------

		// checks if tokens list contains a single pos tag token.
		if ((tokens.size() == 1)
				&& (tokens.get(0).getTokenType() == TokenType.POS_TAG)) {
			isPosTagToken = true;
		}

		else {

			possibleStrings = buildPossibleStrings();

		}

		// ---------------------------------------------------------
		// 2. Evaluates possible strings depending on the field type
		// ---------------------------------------------------------

		switch (fieldType) {

		case TEXT_CONTAINS:

			// returns true if text contains a possible string
			for (String string : possibleStrings) {

				// contains text but not operator is true
				// eg: ![@Literal:dog] == false
				if (vine.containsStrictString(string, false) && notOperator) {
					return false;
				}

				// contains text and not operator is false
				// eg: [!@Literal:dog] == true
				else if (vine.containsStrictString(string, false)
						&& !notOperator) {
					return true;
				}
			}

			break;

		case SCRUBBED_TEXT_CONTAINS:

			// returns true if scrubbed text contains a possible string
			for (String string : possibleStrings) {

				// contains text but not operator is true
				if (vine.containsStrictString(string, true) && notOperator) {
					return false;
				}

				// contains text and not operator is false
				else if (vine.containsStrictString(string, true)
						&& !notOperator) {
					return true;
				}
			}
			break;

		case GRAMMAR_DEPENDENCY_GOVERNOR:

			if (isPosTagToken) {
				boolean b = evaluatePosTagToken(FieldType.GRAMMAR_DEPENDENCY_GOVERNOR);
				// System.out.println("EVALUATION POS TAG: " + b);
				return b;
			}

			for (String targetWord : possibleStrings) {
				// System.out.println(targetWord);
				boolean containsTargetWord = vine
						.containsTargetWordInGrammarDependency(targetWord,
								relation, FieldType.GRAMMAR_DEPENDENCY_GOVERNOR);
				if (containsTargetWord || (!containsTargetWord && notOperator)) {
					return true;
				}
			}
			break;

		case GRAMMAR_DEPENDENCY_DEPENDENT:

			if (isPosTagToken) {
				return evaluatePosTagToken(FieldType.GRAMMAR_DEPENDENCY_DEPENDENT);
			}

			for (String targetWord : possibleStrings) {

				if (vine.containsTargetWordInGrammarDependency(targetWord,
						relation, FieldType.GRAMMAR_DEPENDENCY_DEPENDENT)) {
					return true;
				}
			}
			break;

		default:
			break;
		}

		return false;

	}

	// //////////////////
	// PRIVATE METHODS //
	// //////////////////

	private boolean evaluatePosTagToken(FieldType ft) {

		// Gets pos tag from filter token
		// eg: [@PosTag:VBG] => "VBG"
		String posTag = tokens.get(0).getTokenValue().toLowerCase();
		// System.out.println("pos tag: " + posTag);

		// gets list of dependencies by name 'relation'
		List<GrammarDependency> dependencies = vine
				.getGrammarDependencyByName(relation.getRelation());
		if (dependencies.isEmpty()) {
			return false;
		}

		// holds the pos-tagged token in the dependency field
		TaggedToken tempTaggedToken;

		// gets the pos-tagged token from the dependency field
		tempTaggedToken = relation.getTaggedTokenByFieldType(ft);
		if (tempTaggedToken == null) {
			return false;
		}
		// System.out.println(tempTaggedToken.getTag() + " - "
		// + tempTaggedToken.getToken());

		// checks tempTaggedToken for pos-tag match
		if (tempTaggedToken.getTag().toLowerCase().contentEquals(posTag)) {
			return true;
		}

		return false;
	}

	private List<String> buildPossibleStrings() {

		// -------------------
		// 1. Method variables
		// -------------------

		// result
		List<String> strings;

		// number of possible strings
		int tmpPossibleStrings = 0;
		int possibleStrings = 0;

		notOperator = false;

		// ----------------------------------------------------
		// 2. Gets the number possible strings to store in list
		// ----------------------------------------------------

		for (Token token : tokens) {

			// sets not operator
			if (token.hasNotOperator()) {
				notOperator = true;
			}

			// gets token's possible strings count
			tmpPossibleStrings = countPossibleStrings(token);

			// compares it to the final strings count
			if (tmpPossibleStrings > possibleStrings) {
				possibleStrings = tmpPossibleStrings;
			}
		}

		// ----------------------------------
		// 3. Builds list of possible strings
		// ----------------------------------

		// makes an empty List for possible strings
		strings = instantiatePossibleStringsList(possibleStrings);

		for (Token token : tokens) {

			switch (token.getTokenType()) {

			case LITERAL:

				// appends literal value to list elements
				for (int i = 0; i < possibleStrings; i++) {
					strings.set(i, strings.get(i) + token.getTokenValue());
				}
				break;

			case STRINGS_OBJECT:

				// appends strings object values to list elements
				for (int i = 0; i < possibleStrings; i++) {
					strings.set(
							i,
							strings.get(i)
									+ settings.getStringsObjects()
											.get(token.getTokenValue())
											.getValues().get(i));
				}
				break;

			case TARGET_WORD:

				// appends target word values to list elements
				for (int i = 0; i < possibleStrings; i++) {

					strings.set(i, strings.get(i)
							+ settings.getRunConfiguration().getTargetWords()
									.getTargetWords().get(i));
				}
				break;

			case TARGET_WORD_NO_SPACES:
				// appends target word values to list elements
				for (int i = 0; i < possibleStrings; i++) {

					strings.set(i, strings.get(i)
							+ settings.getRunConfiguration().getTargetWords()
									.getTargetWordsNoSpaces().get(i));
				}
				break;
			default:
				break;
			}

		}

		return strings;

	}

	/**
	 * Returns an List of {@code size} number of empty strings.
	 * 
	 * @param size
	 *            number of elements to put in array
	 * @return List of {@code size} empty strings
	 */
	private List<String> instantiatePossibleStringsList(int size) {
		List<String> result = new ArrayList<String>();
		for (int i = 0; i < size; i++) {
			result.add("");
		}
		return result;
	}

	private int countPossibleStrings(Token t) {

		int possibleStrings = 0;

		switch (t.getTokenType()) {

		// gets 1 for the literal
		case LITERAL:
			possibleStrings = 1;
			break;

		// gets number of pos tags
		case POS_TAG:
			possibleStrings = settings.getRunConfiguration().getPosTags()
					.size();
			break;

		// gets number of strings in the strings object
		case STRINGS_OBJECT:

			possibleStrings = settings.getStringsObjects()
					.get(t.getTokenValue()).getValues().size();
			break;

		// gets number of target words in target words object
		case TARGET_WORD:
			possibleStrings = settings.getRunConfiguration().getTargetWords()
					.getTargetWords().size();
			break;

		// gets number of target words without spaces in target words object
		case TARGET_WORD_NO_SPACES:
			possibleStrings = settings.getRunConfiguration().getTargetWords()
					.getTargetWordsNoSpaces().size();

			break;
		default:
			possibleStrings = 0;
		}

		return possibleStrings;
	}

	// /////////////////
	// GLOBAL SETTERS //
	// /////////////////

	public void setVine(Vine v) {
		vine = v;
	}

	public void setFieldType(FieldType ft) {
		fieldType = ft;
	}

	public void setTokens(List<Token> t) {
		tokens = t;
	}

	public void setGrammarDependency(GrammarDependency r) {
		relation = r;
	}
}
