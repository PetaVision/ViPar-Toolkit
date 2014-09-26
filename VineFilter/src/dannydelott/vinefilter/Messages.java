package dannydelott.vinefilter;

public final class Messages {

	// ////////////
	// Main.java //
	// ////////////
	public static final String Main_errorMissingSettingsFile = "\nERROR: No settings file given at runtime.";
	public static final String Main_errorBadPosTags = "\nERROR: Could not load the part-of-speech tags file.";
	public static final String Main_errorBadRelationTags = "\nERROR: Could not load the grammar relation tags file.";
	public static final String Main_errorBadDatasetFile = "\nERROR: Could not parse dataset file.";
	public static final String Main_beginParsing = "\nBEGIN PARSING\n";

	// ////////////////////////
	// RunConfiguration.java //
	// ////////////////////////

	public static final String RunConfig_errorStartAt = "\nERROR: Missing or incorrect value given at \"startAt\" in settings file.  Must be an integer greater than 0.";
	public static final String RunConfig_errorNumToCollect = "\nERROR: Missing or incorrect value given at \"numToCollect\" in settings file.  Must be an integer greater than 0 or use -1 to process the entire dataset.";
	public static final String RunConfig_errorValidationQuota = "\nERROR: Missing or incorrect value given at \"validationQuota\" in settings file.  Must be an integer greater than 0.";
	public static final String RunConfig_errorTargetWordsFilePath = "\nERROR: Missing or incorrect file path given at \"targetWordsFile\" in settings file.";
	public static final String RunConfig_errorParseException = "\nERROR: JSON syntax error in settings file.  incorrect JSON in entry: \"config\".";
	public static final String RunConfig_errorKeepSimpleSearch = "\nERROR: Missing or incorrect value at \"keepSimpleSearch\" in settings file.  Must be either true or false.";
	public static final String RunConfig_errorDatasetDirectory = "\nERROR: Missing or incorrect file path given at \"dataSetDirectory\" in settings file.";
	public static final String RunConfig_errorPosTagsFilePath = "\nERROR: Missing or incorrect file path given at \"posTagsFile\" in settings file.";
	public static final String RunConfig_errorRelationTagsFilePath = "\nERROR: Missing or invlaid file path given at \"relationTagsFile\" in settings file.";
	public static final String RunConfig_parsingPosAndRelationTagsDataset = "Parsing out part-of-speech tags and/or grammar relation tags from dataset...";
	public static final String RunConfig_loadingRunConfiguration = "LOADING:\tRun configuration from settings file...";
	public static final String RunConfig_loadingTargetWords = "\t\tTarget words from run configuration...";
	public static final String RunConfig_loadingDataset = "\t\tDataset from run configuration...";
	public static final String RunConfig_loadingPosAndRelationTagsFile = "\t\tPart-of-speech and grammar relation tag files from run configuration...";

	// ///////////////////
	// TargetWords.java //
	// ///////////////////

	public static final String TargetWords_errorBadCategoryValue = "\nERROR: Missing or incorrect value given at \"category\" in target words file.";
	public static final String TargetWords_errorBadHyponymsValue = "\nERROR: Missing or incorrect value given at \"hyponyms\" in target words file.";

	// ////////////////////
	// SettingsFile.java //
	// ////////////////////

	public static final String SettingsFile_errorJsonObjects = "ERROR: Could not parse out JSON objects in settings file. Check that Strings values are between double quotations and that numbers and booleans are not inside any quotations.";
	public static final String SettingsFile_errorEntryType = "\nERROR: Missing or incorrect value given at \"entry\" in settings file.";
	public static final String SettingsFile_errorConfigEntry = "\nERROR: Missing or duplicate entry \"config\" in settings file.";
	public static final String SettingsFile_loadingStringsObjects = "\t\tStrings objects from settings file...";
	public static final String SettingsFile_errorGrammarFilters = "\nERROR: Missing entry \"filter\".  Settings files must contain at least one grammar filter.";
	public static final String SettingsFile_loadingFilters = "\t\tFilters from settings file...\n\nFILTERS:";
	public static final String SettingsFile_settings = "\nSETTINGS:\t";
	public static final String SettingsFile_filePath = "File path:\t\t\t";
	public static final String SettingsFile_startAt = "\t\tStarting vine:\t\t\t";
	public static final String SettingsFile_keepSimpleSearch = "\t\tKeep simple search:\t\t";
	public static final String SettingsFile_numToCollect = "\t\tNum. results to collect:\t";
	public static final String SettingsFile_validationQuota = "\t\tFilter validation quota:\t";

	// ////////////
	// Vine.java //
	// ////////////

	public static final String Vine_errorId = "\nERROR: Missing or incorrect value given at \"id\" in dataset file.";
	public static final String Vine_errorUrl = "\nERROR: Missing or incorrect value given at \"url\" in dataset file.";
	public static final String Vine_errorText = "\nERROR: Missing or incorrect value given at \"text\" in dataset file.";
	public static final String Vine_errorScrubbedText = "\nERROR: Missing or incorrect value given at \"scrubbed_text\" in dataset file.";
	public static final String Vine_errorPosTags = "\nERROR: Missing or incorrect value given at \"pos_tags\" in dataset file.";
	public static final String Vine_errorGrammarDependencies = "\nERROR: Missing or incorrect value given at \"grammar_dependencies\" in dataset file.";

	// /////////////////////
	// StringsObject.java //
	// /////////////////////

	public static final String StringsObject_errorName = "\nERROR: Missing or incorrect value given at \"name\" in settings file Strings object.";
	public static final String StringsObject_errorDescription = "\nERROR: Missing or incorrect value given at \"description\" in settings file Strings object.";
	public static final String StringsObject_errorAssignToPosTag = "\nERROR: Missing or incorrect value given at \"assignToPosTag\" in settings file Strings object.  Must contain a valid part-of-speech tag.";
	public static final String StringsObject_errorLemmatize = "\nERROR: Missing or incorrect value given at \"lemmatize\" in settings file Strings object.  Must be true or false.";
	public static final String StringsObject_errorValues = "\nERROR: Missing or incorrect value given at \"values\" in settings file Strings object.  Must be a JSON array of Strings.";

	// //////////////
	// Filter.java //
	// //////////////

	public static final String Filter_errorParseException = "\nERROR: JSON syntax error in settings file.  incorrect JSON in entry \"filter\".";
	public static final String Filter_errorEntry = "\nERROR: Missing or incorrect value given at \"entry\" in settings file filter object.";
	public static final String Filter_errorName = "\nERROR: Missing or incorrect value given at \"name\" in settings file filter objects.";
	public static final String Filter_errorType = "\nERROR: Missing or incorrect value given at \"type\" in settings file filter objects. Must be either \"validate\" or \"void\".";
	public static final String Filter_errorDescription = "\nERROR: Missing or incorrect value given at \"description\" in settings file filter objects.";
	public static final String Filter_errorEnabled = "\nERROR: Incorrect value given at \"enabled\" in settings file filter objects.  Must be either true or false.";
	public static final String Filter_errorIsChild = "\nERROR: Missing or incorrect value given at \"isChild\" in settings file filter objects.  Must be either true or false.";
	public static final String Filter_errorChildMustBeTrue = "\nERROR: Filter must have \"isChild\" set to true.";
	public static final String Filter_errorTextContains = "\nERROR: Incorrect value given at \"textContains\" in settings file filter objects.  Must be a String value.";
	public static final String Filter_errorScrubbedTextContains = "\nERROR: Incorrect value given at \"scrubbedTextContains\". Must be a String value.";
	public static final String Filter_errorMissingOptionalField = "\nERROR: Missing field at entry \"filter\".  Filters must contain at least one of the following fields: \"textContains\", \"scrubbedTextContains\", or \"grammarDependency\".";

	// /////////////////////////
	// GrammarDependency.java //
	// /////////////////////////

	public static final String GrammarDependency_errorRelation = "\nERROR: Missing or incorrect value given at \"relation\" in settings file filter objects.";
	public static final String GrammarDependency_errorGovernor = "\nERROR: Incorrect value given at \"governor\" in settings file filter objects.";
	public static final String GrammarDependency_errorDependent = "\nERROR: Incorrect value given at \"dependent\" in settings file filter objects.";
	public static final String GrammarDependency_errorChildrenNames = "\nERROR: Incorrect value(s) given at \"children\" in settings file filter objects.";
	public static final String GrammarDependency_errorChildNotFound = "\nERROR: Incorrect child name given at \"children\" in settings file filter objects.";
	public static final String GrammarDependency_errorChild = "\nERROR: Unable to parse child filter object.";
	public static final String GrammarDependency_errorMissingOptionalField = "\nERROR: Missing field at entry \"grammarDependency\". Must contain either \"governor\" or \"dependent\" fields.";

	// //////////////////////////////
	// Expression.java //
	// //////////////////////////////

	public static final String Expression_errorParenthesis = "\nERROR: Missing or incorrect usage of parenthesis.";
	public static final String Expression_errorBrackets = "\nERROR: Missing or incorrect usage of brackets.";
	public static final String Expression_errorLogicType = "\nERROR: Missing or incorrect usage of logic operators.";

	// ////////////////////////////////
	// Argument.java //
	// ////////////////////////////////

	public static final String Argument_errorPosTag = "\n\nERROR: Missing or incorrect part-of-speech tag.";
	public static final String Argument_errorStringsObject = "\n\nERROR: Missing or incorrect Strings object name.";
	public static final String Argument_errorFilterExpression = "\n\nERROR: Missing or incorrect filter expression.";
	public static final String Argument_errorTokenType = "\n\nERROR: Missing or incorrect token.";
	public static final String Argument_errorParent = "\n\nERROR: Missing or incorrect parent value.";

	// /////////////////////////////
	// Token.java //
	// /////////////////////////////

	public static final String FilterExpressionToken_errorToken = "\n\nERROR: Missing or incorrect filter expression token.";
	public static final String FilterExpressionToken_errorTokenValue = "\n\nERROR: Missing or incorrect filter expression token value.";

	// ///////////////////////////
	// ExpressionEvaluator.java //
	// ///////////////////////////

	public static final String ExpressionEvaluator_errorGetExpression = "\n\nERROR: Could not retrieve expression from filter.";
	public static final String ExpressionEvaluator_errorInterpretResults = "\n\nERROR: Could not interpret filter evaluation results.";
}
