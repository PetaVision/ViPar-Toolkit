package dannydelott.vinefilter.settings.filter.expression.values;

public final class Tokens {

	public static final char L_PAREN = '(';
	public static final char R_PAREN = ')';
	public static final char L_BRACE = '{';
	public static final char R_BRACE = '}';
	public static final char L_BRACKET = '[';
	public static final char R_BRACKET = ']';

	public static final String AND = "&&";
	public static final String OR = "||";
	public static final char NOT = '!';

	public static final String PARENT_GOVERNOR_TOKEN = "[@Parent:GOVERNOR]";
	public static final String PARENT_DEPENDENT_TOKEN = "[@Parent:DEPENDENT]";

	public static final String TARGET_WORD_TOKEN = "[@TargetWord]";
	public static final String TARGET_WORD_NO_SPACES_TOKEN = "[@TargetWord:NO_SPACES]";

	public static final String LITERAL_TOKEN = "[@Literal:";
	public static final String POS_TAG_TOKEN = "[@PosTag:";
	public static final String STRING_OBJECT_TOKEN = "[@Strings:";

	public static final String TARGET_WORD = "TargetWord";
	public static final String LITERAL = "Literal";
	public static final String POS_TAG = "PosTag";
	public static final String STRING_OBJECT = "Strings";

}
