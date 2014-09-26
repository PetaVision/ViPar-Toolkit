package dannydelott.vinefilter.settings.filter;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import dannydelott.vinefilter.Messages;
import dannydelott.vinefilter.settings.EntryType;
import dannydelott.vinefilter.settings.SettingsFile;
import dannydelott.vinefilter.settings.config.dataset.Vine;
import dannydelott.vinefilter.settings.filter.expression.Expression;
import dannydelott.vinefilter.settings.filter.expression.evaluators.FilterEvaluator;
import dannydelott.vinefilter.settings.filter.expression.values.FilterType;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class Filter {

	// ///////////////////
	// GLOBAL VARIABLES //
	// ///////////////////

	// holds the JSON filter object
	private JsonObject object;

	// holds the settings file to pass into GrammarDependency
	private SettingsFile settings;

	// holds the parent filter
	private Filter parent;

	// / holds the filter values
	private EntryType entry;
	private String name;
	private FilterType type;
	private String description;
	private boolean enabled;
	private boolean isChild;
	private String rawTextContains;
	private String rawScrubbedTextContains;
	private GrammarDependency grammarDependency;

	// holds children for parent filter (String name of child, Filter filter)
	private Hashtable<String, Filter> children;

	// holds the parsed filter expression groups in textContains
	private Expression textContains;
	private boolean hasTextContains;

	// holds the parsed filter expression groups in scrubbedTextContains
	private Expression scrubbedTextContains;
	private boolean hasScrubbedTextContains;

	// holds the parsed filter expression groups found in grammarDependency
	private Expression grammarDependencyGovernor;
	private Expression grammarDependencyDependent;
	private boolean hasGrammarDependencyGovernorExpression;
	private boolean hasGrammarDependencyDependentExpression;

	// holds the evaluator
	private FilterEvaluator filterEvaluator;

	// holds error flags
	private boolean flagFilter;
	private boolean flagChildren;
	private boolean flagVineEvaluator;

	// holds the number of tabs to use when printing out the name of the filter
	// during parseFilter()
	private int numTabs;

	// ///////////////////////
	// FACTORY CONSTRUCTORS //
	// ///////////////////////

	// PARENT filter
	public static Filter newInstance(JsonObject j, SettingsFile s, int n) {

		Filter r = new Filter(j, s, n);

		if (r.getFlagFilter() || r.getFlagVineEvaluator()) {
			return null;
		}

		return r;
	}

	// CHILD filter
	/**
	 * Builds a new instance of Filter for a child object.
	 * 
	 * @param j
	 *            child JsonObject
	 * @param s
	 *            SettingsFile object for error checking and Filter building
	 * @param p
	 *            parent Filter object
	 * @return
	 */
	public static Filter newInstance(JsonObject j, SettingsFile s, Filter p) {
		Filter r = new Filter(j, s, p);
		if (r.getFlagFilter() || r.getFlagVineEvaluator()) {
			return null;
		}

		return r;
	}

	// ///////////////
	// CONSTRUCTORS //
	// ///////////////

	// PARENT filter
	private Filter(JsonObject j, SettingsFile s, int n) {
		object = j;
		settings = s;
		numTabs = n;
		flagFilter = false;

		// sets object values from JsonObject
		parseFilter(object);
		if (flagFilter) {
			return;
		}

		// builds the filter expressions from the object values
		buildFilterExpressionGroups();
		if (flagFilter) {
			return;
		}

		System.out.println("success");

		filterEvaluator = new FilterEvaluator(settings, this);
	}

	// CHILD filter
	private Filter(JsonObject j, SettingsFile s, Filter p) {
		object = j;
		settings = s;
		parent = p;
		flagFilter = false;

		// sets object values from JsonObject
		parseFilter(object);
		if (flagFilter) {
			return;
		}

		replaceChildParentValues();

		// builds the filter expressions from the object values
		buildFilterExpressionGroups();
		if (flagFilter) {
			return;
		}

		filterEvaluator = new FilterEvaluator(settings, this);

	}

	// /////////////////
	// PUBLIC METHODS //
	// /////////////////

	public boolean hasScrubbedTextContains() {
		if (scrubbedTextContains != null) {
			return true;
		} else {
			return false;
		}
	}

	public boolean hasTextContains() {
		if (textContains != null) {
			return true;
		} else {
			return false;
		}
	}

	public boolean hasGrammarDependency() {
		if (grammarDependency != null) {
			return true;
		} else {
			return false;
		}
	}

	public boolean hasGrammarDependencyGovernorExpression() {
		return hasGrammarDependencyGovernorExpression;
	}

	public boolean hasGrammarDependencyDependentExpression() {
		return hasGrammarDependencyDependentExpression;
	}

	public boolean hasChildren() {
		if (children == null) {
			return false;
		} else if (children.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean evaluateVine(Vine v) {

		// returns false if the vine does not contain the grammar dependency as
		// one of it's relations
		if (hasGrammarDependency()) {
			if (!v.containsGrammarDependency(grammarDependency.getRelation())) {
				return false;
			}
		}

		// ------------------------
		// 1.
		// Evaluates children first
		// ------------------------

		if (hasChildren()) {

			// loops over children
			Enumeration<String> enumKey = children.keys();
			while (enumKey.hasMoreElements()) {

				// gets child from hashtable
				String name = enumKey.nextElement();
				Filter child = children.get(name);

				// returns false if child is bad
				// ie: bad is false as VALIDATE type, or true and VOID type
				if (child.evaluateVine(v)
						&& child.getFilterType() == FilterType.VOID) {
					System.out.println(child.getName() + " is bad");
					return false;
				}
			}
		}

		// ----------------
		// 2.
		// Evaluates filter
		// ----------------
		filterEvaluator.setVine(v);
		boolean r = filterEvaluator.evaluateVine();
		return r;
	}

	// //////////////////
	// PRIVATE METHODS //
	// //////////////////

	private void parseFilter(JsonObject j) {

		// holds the JSON object
		JsonObject object = j;
		if (object == null) {
			flagFilter = true;
			return;
		}

		// holds the requested JSON value
		JsonValue temp;

		// ***must be set to true during parse or throw error flag***
		boolean hasOptionalField = false;

		// resets filter expression group flags
		hasTextContains = false;
		hasScrubbedTextContains = false;
		hasGrammarDependencyGovernorExpression = false;
		hasGrammarDependencyDependentExpression = false;

		// resets error flag
		flagFilter = false;

		// -----------------------
		// 1.
		// PARSES MANDATORY FIELDS
		// -----------------------

		// 1. "entry"
		temp = object.get("entry");
		if (temp == null) {
			System.out.println(Messages.Filter_errorEntry);
			flagFilter = true;
			return;
		} else if (temp.asString().toLowerCase().contentEquals("filter")) {
			entry = EntryType.FILTER;
		} else {
			System.out.println(Messages.Filter_errorEntry);
			flagFilter = true;
			return;
		}

		// 2. "name"
		temp = object.get("name");
		if (temp == null) {
			System.out.println(Messages.Filter_errorName);
			flagFilter = true;
			return;
		} else if (temp.isString()) {
			name = temp.asString();

		} else {
			System.out.println(Messages.Filter_errorName);
			flagFilter = true;
			return;
		}

		// 3. "type"
		temp = object.get("type");
		if (temp == null) {
			System.out.println(Messages.Filter_errorType);
			flagFilter = true;
			return;
		} else if (temp.asString().toLowerCase().contentEquals("validate")) {
			type = FilterType.VALIDATE;
		} else if (temp.asString().toLowerCase().contentEquals("void")) {
			type = FilterType.VOID;
		} else {
			System.out.println(Messages.Filter_errorType);
			flagFilter = true;
			return;
		}

		// 4. "description"
		temp = object.get("description");
		if (temp == null) {
			System.out.println(Messages.Filter_errorDescription);
			flagFilter = true;
			return;
		} else if (temp.isString()) {
			description = temp.asString();
		} else {
			System.out.println(Messages.Filter_errorDescription);
			flagFilter = true;
			return;
		}

		// ---------------------
		// 2.
		// PARSES DEFAULT FIELDS
		// ---------------------

		// Parses fields which have defaults.
		//
		// The user may specify these fields or not. They are completely
		// optional, however if they are not set, the defaults will be used.

		// 1. "enabled"
		temp = object.get("enabled");
		if (temp != null && temp.isBoolean()) {
			enabled = temp.asBoolean();
		} else if (temp != null && !temp.isBoolean()) {
			System.out.println(Messages.Filter_errorEnabled);
			flagFilter = true;
			return;
		} else {
			// default
			enabled = true;
		}

		// 2. "isChild"
		temp = object.get("isChild");
		if (temp != null && temp.isBoolean()) {
			isChild = temp.asBoolean();
		} else if (temp != null && !temp.isBoolean()) {
			System.out.println(Messages.Filter_errorIsChild);
			flagFilter = true;
			return;
		} else {
			// default
			isChild = false;
		}

		if (!isChild) {
			System.out.print(makeTabs() + name + "...");
		}

		// -------------------------
		// 3.
		// PARSES FILTER EXPRESSIONS
		// -------------------------

		// Parses filter expressions. (Must use at least one)
		//
		// The user must specify one or more of the following fields in order to
		// properly construct a Filter object. Simply put, there is no filter
		// without an expression to validate. These can be associated with the
		// text body or the scrubbed text body, or the user may construct a
		// grammar dependency object.

		// 1. "textContains"
		temp = object.get("textContains");
		if (temp != null && temp.isString()) {
			hasOptionalField = true;
			hasTextContains = true;
			rawTextContains = temp.asString();
		} else if (temp != null && !temp.isString()) {
			System.out.println(Messages.Filter_errorTextContains);
			flagFilter = true;
			return;
		}

		// 2. "scrubbedTextContains"
		temp = object.get("scrubbedTextContains");
		if (temp != null && temp.isString()) {
			hasScrubbedTextContains = true;
			hasOptionalField = true;
			rawScrubbedTextContains = temp.asString();
		} else if (temp != null && !temp.isString()) {
			System.out.println(Messages.Filter_errorScrubbedTextContains);
			flagFilter = true;
			return;
		}

		// 3. "grammarDependency"
		temp = object.get("grammarDependency");
		if (temp != null) {

			// throws flag if value is not a JSON object
			if (!temp.isObject()) {
				flagFilter = true;
				return;
			}

			// sets to true, since grammar dependency may be the only filter
			// expression
			hasOptionalField = true;

			// sets the grammar dependency
			grammarDependency = GrammarDependency.newInstance(this,
					temp.asObject(), settings);

			// throws flag if GrammarDependency object is bad
			if (grammarDependency == null) {
				flagFilter = true;
				return;
			}

			if (grammarDependency.hasGovernor()) {
				hasGrammarDependencyGovernorExpression = true;
			}

			if (grammarDependency.hasDependent()) {
				hasGrammarDependencyDependentExpression = true;
			}

		}

		// checks that at least one optional field was given
		if (!hasOptionalField) {
			System.out.println(Messages.Filter_errorMissingOptionalField);
			flagFilter = true;
			return;
		}

		// -----------------------
		// 3.
		// PARSES OPTIONAL FIELDS
		// ----------------------

		// Parses optional fields. (completely optional)
		//
		// These fields are completely optional. There are no set defaults. If
		// these are not present in a "filter" entry, nothing happens.

		temp = object.get("children");
		if (temp != null) {
			if (temp.isArray()) {
				buildChildFilters(temp.asArray());
				if (flagChildren) {
					flagFilter = true;
					return;
				}
			}
		}
	}

	private String makeTabs() {
		String tabs = "";
		for (int i = 0; i < numTabs; i++) {
			tabs = tabs + "\t";
		}
		return tabs;
	}

	private void buildChildFilters(JsonArray j) {

		Filter childFilter;

		// holds the child filter as JSON object
		JsonObject tempObj;

		// holds the "isChild" value
		JsonValue tempVal;

		// builds a list from the "children" array values
		List<JsonValue> childNamesInFilter = j.asArray().values();

		// initializes children list
		children = new Hashtable<String, Filter>();

		// resets error flag
		flagChildren = false;

		// iterates over children names
		for (JsonValue childName : childNamesInFilter) {

			if (childName.isString()) {

				// checks if child name in from current filter exists in
				// settings file childNames hashset
				// ROLL CALL!
				if (!settings.getChildNames().contains(childName.asString())) {
					System.out.println("failed");
					System.out
							.println(Messages.GrammarDependency_errorChildNotFound);
					flagChildren = true;
					break;
				}

				// gets the child filter JSON object
				tempObj = settings.findJsonObjectFilterByName(
						childName.asString(), true);

				// child filter doesn't exist
				if (tempObj == null) {
					System.out.println("failed");
					System.out
							.println(Messages.GrammarDependency_errorChildNotFound);
					flagChildren = true;
					break;
				}

				// "isChild" doesn't exist or isn't boolean
				tempVal = tempObj.get("isChild");
				if (tempVal == null || !tempVal.isBoolean()) {
					System.out.println("failed");
					System.out.println(Messages.Filter_errorIsChild);
					flagChildren = true;
					break;
				}

				// "isChild" is false
				if (!tempVal.asBoolean()) {
					System.out.println("failed");
					System.out.println(Messages.Filter_errorChildMustBeTrue);
					flagChildren = true;
					break;
				}

				// creates Filter object for child
				// NOTE: child filters can have their own children too!
				childFilter = Filter.newInstance(tempObj, settings, this);
				if (childFilter == null) {
					System.out.println("failed");
					flagChildren = true;
					break;
				}
				children.put(childFilter.getName(), childFilter);

			} else {
				System.out.println("failed");
				flagChildren = true;
				break;
			}
		}

	}

	private void replaceChildParentValues() {

		// replace with the parent expressions
		if (grammarDependency.hasGovernor()) {

			String gov = grammarDependency.getGovernor();
			
			gov = gov.replace("[@Parent:GOVERNOR]", parent
					.getGrammarDependency().getGovernor());
			
			gov = gov.replace("[@Parent:DEPENDENT]", parent
					.getGrammarDependency().getDependent());

			grammarDependency.setGovernor(gov);
		}

		if (grammarDependency.hasDependent()) {

			String dep = grammarDependency.getDependent();

			dep = dep.replace("[@Parent:GOVERNOR]", parent
					.getGrammarDependency().getGovernor());
			
			dep.replace("[@Parent:DEPENDENT]", parent.getGrammarDependency()
					.getDependent());

			grammarDependency.setDependent(dep);
		}

	}

	private void buildFilterExpressionGroups() {

		Expression tmpGroups;
		
		if (hasTextContains) {
			tmpGroups = Expression.newInstance(settings, rawTextContains);

			if (tmpGroups == null) {
				flagFilter = true;
				return;
			}

			textContains = tmpGroups;

		}

		if (hasScrubbedTextContains) {
			tmpGroups = Expression.newInstance(settings,
					rawScrubbedTextContains);

			if (tmpGroups == null) {
				flagFilter = true;
				return;
			}

			scrubbedTextContains = tmpGroups;
		}

		if (hasGrammarDependencyGovernorExpression) {
			tmpGroups = Expression.newInstance(settings,
					grammarDependency.getGovernor());

			if (tmpGroups == null) {
				flagFilter = true;
				return;
			}

			grammarDependencyGovernor = tmpGroups;
		}

		if (hasGrammarDependencyDependentExpression) {
			tmpGroups = Expression.newInstance(settings,
					grammarDependency.getDependent());

			if (tmpGroups == null) {
				System.out.println("error");
				flagFilter = true;
				return;
			}

			grammarDependencyDependent = tmpGroups;
		}
	}

	// /////////////////
	// GLOBAL GETTERS //
	// /////////////////

	public boolean getFlagFilter() {
		return flagFilter;
	}

	public boolean getFlagChildren() {
		return flagChildren;
	}

	public boolean getFlagVineEvaluator() {
		return flagVineEvaluator;
	}

	public Expression getTextContains() {
		return textContains;
	}

	public Expression getScrubbedTextContains() {
		return scrubbedTextContains;
	}

	public Expression getGrammarDependencyGovernor() {
		return grammarDependencyGovernor;
	}

	public Expression getGrammarDependencyDependent() {
		return grammarDependencyDependent;
	}

	public Filter getParentFilter() {
		return parent;
	}

	public EntryType getEntryType() {
		return entry;
	}

	public String getName() {
		return name;
	}

	public FilterType getFilterType() {
		return type;
	}

	public String getDescription() {
		return description;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public boolean isChild() {
		return isChild;
	}

	public String getRawTextContains() {
		return rawTextContains;
	}

	public String getRawScrubbedTextContains() {
		return rawScrubbedTextContains;
	}

	public GrammarDependency getGrammarDependency() {
		return grammarDependency;
	}

	public Hashtable<String, Filter> getChildren() {
		return children;
	}

	public FilterEvaluator getFilterEvaluator() {
		return filterEvaluator;
	}

}
