package dannydelott.vinefilter.settings.filter.expression;

import java.util.LinkedHashMap;

import dannydelott.vinefilter.Messages;
import dannydelott.vinefilter.settings.SettingsFile;
import dannydelott.vinefilter.settings.filter.expression.values.LogicType;
import dannydelott.vinefilter.settings.filter.expression.values.Tokens;

public class Argument {

	// ///////////////////
	// GLOBAL VARIABLES //
	// ///////////////////

	private SettingsFile settings;
	private String group;

	private LinkedHashMap<Token, LogicType> tokens;

	// holds the error flag
	private boolean flagError;

	// //////////////////////
	// FACTORY CONSTRUCTOR //
	// //////////////////////

	public static Argument newInstance(SettingsFile s, String g) {

		Argument rea = new Argument(s, g);
		if (rea.getFlagError()) {
			System.out.println("filter expression argument is null");
			return null;
		}

		return rea;
	}

	// //////////////
	// CONSTRUCTOR //
	// //////////////

	private Argument(SettingsFile s, String g) {

		settings = s;
		group = g;
		tokens = parseTokens();

	}

	// //////////////////
	// PRIVATE METHODS //
	// //////////////////

	private LinkedHashMap<Token, LogicType> parseTokens() {

		// Checks argument tokens.
		//
		// Checks the argument to be sure that if the user intended to use an
		// argument token (eg: @TargetWord, @PosTag, @Strings, @Literal), that
		// they spelled it correctly.

		if (!checkGroupTokensSyntax()) {
			System.out.println(Messages.Argument_errorFilterExpression + " (\""
					+ group + "\")");
			flagError = true;
			return null;
		}

		// Initializes remainder to the full argument. This will be reset as
		// groups and logical operators are split off.
		String remainder = group;
		// System.out.println("group: " + group);
		if (remainder == null) {
			return null;
		}

		// holds the return filter expression arguments and logic types
		LinkedHashMap<Token, LogicType> tempTokens = new LinkedHashMap<Token, LogicType>();
		Token tempToken;
		LogicType tempLogicType = null;

		// holds the current argument and logic parsed
		String[] token = null;
		String[] logic = null;

		while (true) {

			if (remainder == null) {
				// System.out.println("remainder is null");
				break;
			}

			// gets the next group from what's left of the filter expression
			token = Expression.nextToken(remainder);
			if (token == null) {
				System.out.println("token is null");
				return null;
			}

			// sets remainder
			remainder = token[1];

			// creates Token from argument
			tempToken = Token.newInstance(settings, token[0]);

			// System.out.println("created token: "
			// + tempToken.getTokenType().toString());
			// System.out.println("remainder: " + remainder);

			if (remainder == null) {
				tempTokens.put(tempToken, tempLogicType);
				return tempTokens;
			}

			// gets the logic operator from the remainder
			if (remainder != null) {
				logic = Expression.nextLogic(remainder);
				if (logic.length == 0) {
					System.out.println("logic is null");
					flagError = true;
					return null;
				}
			}

			// sets the logicType if logic array contains 2 elements
			if (logic.length == 2) {
				if (logic[0].contentEquals(Tokens.AND)) {
					tempLogicType = LogicType.AND;
				}
				if (logic[0].contentEquals(Tokens.OR)) {
					tempLogicType = LogicType.OR;
				}

				// sets the remainder
				remainder = logic[1];

				// System.out.println("logic remainder: " + remainder);
			}

			// add group and logic type to map
			tempTokens.put(tempToken, tempLogicType);

		}

		return tempTokens;
	}

	private boolean checkGroupTokensSyntax() {

		String[] tmpArray;
		String token;

		// returns true if no argument tokens are present
		if (!group.contains("[@")) {
			return true;
		}

		// ------------------------
		// GETS THE ARGUMENT TOKEN
		// ------------------------

		// splits before the open-bracket to ensure we get 2 parts.
		//
		// EG: "[@Literal:dog]" or "[@TargetWord]"
		// tmpArray [0] => "[" or "["
		// tmpArray [1] => "Literal:dog]" or "TargetWord]"
		tmpArray = group.split("@", 2);

		// splits at ":" if an argument value is present,
		// otherwise just remove the close-bracket,
		// eg: "Literal"
		if (tmpArray[1].contains(":")) {
			tmpArray = tmpArray[1].split(":", 2);
			token = tmpArray[0];
		} else {
			token = tmpArray[1].replace("]", "");
		}

		// -------------------------
		// CHECKS THE ARGUMENT TOKEN
		// -------------------------

		if (!token.contentEquals(Tokens.TARGET_WORD)
				&& !token.contentEquals(Tokens.POS_TAG)
				&& !token.contentEquals(Tokens.STRING_OBJECT)
				&& !token.contentEquals(Tokens.LITERAL)) {
			return false;
		} else {
			return true;
		}

	}

	// /////////////////
	// GLOBAL GETTERS //
	// /////////////////

	public boolean getFlagError() {
		return flagError;
	}

	public LinkedHashMap<Token, LogicType> getTokens() {
		return tokens;
	}
}
