package dannydelott.vinefilter.settings.config.dataset;

import dannydelott.vinefilter.settings.filter.FieldType;

public class GrammarDependency {

	Vine vine;
	String relation;
	EnumeratedToken governor;
	EnumeratedToken dependent;

	// //////////////
	// CONSTRUCTOR //
	// //////////////

	public GrammarDependency(Vine v, String rel, EnumeratedToken gov,
			EnumeratedToken dep) {
		vine = v;
		relation = rel;
		governor = gov;
		dependent = dep;
	}

	// /////////////////
	// PUBLIC METHODS //
	// /////////////////

	public TaggedToken getTaggedTokenByFieldType(FieldType ft) {

		// token and position in grammar dependency
		EnumeratedToken enumToken = null;

		// pos-tag and token in the vine that matches enumToken position
		TaggedToken taggedToken;

		// -----------------------------------------------
		// 1. Gets the governor or dependent to operate on
		// -----------------------------------------------

		if (ft == FieldType.GRAMMAR_DEPENDENCY_GOVERNOR) {
			enumToken = governor;
		} else if (ft == FieldType.GRAMMAR_DEPENDENCY_DEPENDENT) {
			enumToken = dependent;
		} else {
			return null;
		}

		// -------------------------------------------------------
		// 2. Sets pos-tagged token from enumerated token position
		// -------------------------------------------------------

		// sets pos-tagged token from enumerated token position
		taggedToken = vine.getTaggedTokens().get((enumToken.getPosition() - 1));

		// verifies token from taggedToken equals the token from enumToken
		if (!taggedToken.getToken().contentEquals(enumToken.getToken())) {
			return null;
		}

		return taggedToken;

	}

	// /////////////////
	// GLOBAL GETTERS //
	// /////////////////

	public String getGrammarDependencyAsString() {
		String s = relation + "[" + governor.getToken() + ", " + dependent.getToken() + "]";
		return s;
	}

	public String getRelation() {
		return relation;
	}

	public EnumeratedToken getGovernor() {
		return governor;
	}

	public EnumeratedToken getDependent() {
		return dependent;
	}
}
