 
#VineFilter
Filters Vine video text descriptions by their linguistic meta-data (ie: parts-of-speech and grammar relationships) in order to return videos containing a high-probability of having the user-specified visual object/entity in the video content itself.

The real power of the ViPar toolkit is demonstrated in this tool. 

##How it Works

VineFilter is a Java program that uses Vine meta-data obtained via [VineScrape](https://github.com/DannyDelott/ViPar-Toolkit/tree/master/VineScrape) to select specific videos based on any number of user-specified criteria.  

VineFilter uses a settings file that contains all of these user-specified criteria to return datasets of vine videos. See [Setup](#setup) to learn how to create a VineFilter settings file.

##Download

VineFilter comes as both a stand-alone, runnable JAR file and an open-source Java project available here in this repository. Users who only wish to use the program as-is should simply download the `filter.jar` file and run it. See [Usage](#usage).

##Setup

This section covers the creation of the VineFilter settings file.  This file is in JSON format and is separated into three parts-- **Run Configuration**, **Strings Objects**, and **Filters**. In order for VineFilter to successfully read in your settings file, please be sure your settings file conforms to the syntax described in the sections below and that all fields marked **MANDATORY** are present in your settings file objects.


---

####1. Run Configuration

**MANDATORY** - The **Run Configuration** is the first element in the settings file. It specifies the location to the Vine meta-data along with other necessary information about the VineFilter run you are going to launch.  

To create the Run Configuration section, simply copy and paste the following JSON object into an empty file (eg: `settings.json`), substituting the arguments for their respective values.



**Syntax:**
    
    {	"entry":"config",
		"startAt":<NUM_VINE_TO_START_AT>,
		"numToCollect":<NUM_VINES_TO_COLLECT>,
		"validationQuota":<NUM_FILTERS_TO_VALIDATE>,
		"targetWordsFile":"<FILEPATH_TO_TARGET_WORDS>",
		"datasetDirectory":"<FILEPATH_TO_VINE_DIRECTORY>",
		"posTagsFile":"<FILEPATH_TO_POS_TAGS>",
		"relationTagsFile":"<FILEPATH_TO_RELATION_TAGS>"
    }
    
| Field | Argument | Description |
|---|---|---|
| *entry* | "config" |	**MANDATORY** - Use "config" to specify the run configuration. |
| *startAt* | NUM_VINE_TO_START_AT| **MANDATORY** - The nth Vine becomes the starting point. (Use 1 to start at the first Vine in the dataset.)|
| *numToCollect* | NUM_VINES_TO_COLLECT| **MANDATORY** - The number of accepted/filtered Vines to collect.|
| *validationQuota* | NUM_FILTERS_TO_VALIDATE| **MANDATORY** - The number of Filters that must return TRUE in order for a Vine to be accepted.|
| *targetWordsFile* | FILEPATH_TO_TARGET_WORDS| **MANDATORY** - The location of the target words file.|
| *datasetDirectory* | FILEPATH_TO_VINE_DIRECTORY| **MANDATORY** - The location of the directory containing the Vine meta-data.|
| *posTagsFile* | FILEPATH_TO_POS_TAGS| **MANDATORY** - The location of the `pos_tags.txt` file associated with the Vine meta-data.|
| *relationTagsFile* | FILEPATH_TO_RELATION_TAGS| **MANDATORY** - The location of the `relation_tags.txt` associated with the Vine meta-data.|
  
  
  
**Example:**

    // ////////////////////
    // RUN CONFIGURATION //
    // ////////////////////
   
    {	"entry":"config",
		"startAt":1,
		"numToCollect":100,
		"validationQuota":2,
		"targetWordsFile":"Users/johndoe/Desktop/target-words.json",
		"datasetDirectory":"Users/johndoe/Desktop/vines.1410565068",
		"posTagsFile":"Users/johndoe/Desktop/vines.1410565068/pos_tags.txt",
		"relationTagsFile":"Users/johndoe/Desktop/vines.1410565068/relation_tags.txt"
    }
    
> - Sets VineFilter to begin filtering dataset at the first Vine.  
> - Collects 100 filtered Vines.  
> - Requires two Filters be TRUE before accepting a filtered Vine.  
> - Sets the paths to the dataset and its associated `pos_tags.txt` and `relation_tags.txt` files.  
    

---

####2. Strings Objects

*OPTIONAL* - The **Strings Objects** section specifies String literals that can be used as Filter expression tokens.

To create a Strings Object, simply copy and paste the following JSON object into a settings file (eg: `settings.json`), substituting the arguments for their respective values.

**Syntax:**
    
    {	"entry":"s",
		"name":"<NAME_OF_STRINGS_OBJECT>",
		"description":"<DESCRIPTION>",
		"values":["<STRING_LITERAL_0>", "<STRING_LITERAL_1>", "<ETC...>"]
    }
    
| Field | Argument | Description |
|---|---|---|
| *entry* | "s" |	**MANDATORY** - Use "s" to specify a Strings Object. |
| *name* | NAME_OF_STRINGS_OBJECT| **MANDATORY** - The name to use when calling the Strings Object in a Filter expression token.|
| *description* | DESCRIPTION | **MANDATORY** - A brief description about the Strings Objects values and intended use.|
| *values* | STRING_LITERAL_*| **MANDATORY** - The String literals to include in this String Object. |



**Example:**

    // //////////
    // STRINGS //
    // //////////
    
    {	"entry":"s",
		"name":"DEMONSTRATIVE_PRONOUNS",
		"description":"English demonstrative pronouns used as determiners.",
		"values":["this", "that", "these", "those"]
    }
    
> - Creates a Strings Object named "DEMONSTRATIVE_PRONOUNS".
> - Includes a description for user purposes.
> - Sets a JSON array with the intended String values.

---


####3. Filters

**MANDATORY** - The **Filters** section of the settings file is where users will create the specific parameters by which Vines will be accepted or trashed.  These parameters can be based off of the text description as-is or the grammatical structure of the text description given in the linguistic meta-data.

To create a Filter, simply copy and paste the following JSON object into a settings file (eg: `settings.json`), substituting the arguments for their respective values.  

**Syntax:**
    
    {	"entry":"filter",
		"name":"<NAME_OF_FILTER>",
		"type":"<FILTER_TYPE>",
		"description":"<DESCRIPTION>",
		"enabled":<BOOLEAN>,
		"isChild":<BOOLEAN>,
		"textContains":"<FILTER_EXPRESSION>",
		"scrubbedTextContains":"<FILTER_EXPRESSION>",
		"grammarDependency": {
			"relation":"<RELATION_TAG>",
			"governor":"<FILTER_EXPRESSION>",
			"dependent":"<FILTER_EXPRESSION>"
			}
    }
    
| Field | Argument | Description |
|---|---|---|
| *entry* | "filter" |	**MANDATORY** - Use "filter" to specify a Filter. |
| *name* | NAME_OF_FILTER| **MANDATORY** - The name to use when calling the Filter.|
| *type* | FILTER_TYPE| **MANDATORY** - Specifies the Filter type. Use "void" to trash the Vine if the Filter is accepted. Use "validate" to increment the `validationQuota` if the Filter is accepted.|
| *description* | DESCRIPTION | **MANDATORY** - A brief description about the Filter values and intended use.|
| *enabled* | BOOLEAN | *OPTIONAL* - Specifies if the Filter is to be used. (Default set to TRUE.)|
| *isChild* | BOOLEAN | *OPTIONAL* - Specifies if the Filter is a child of other Filters. (Default set to FALSE.)|
| *textContains* | FILTER_EXPRESSION | *OPTIONAL* - The expression to evaluate over the Vine's text description. |
| *scrubbedTextContains* | FILTER_EXPRESSION | *OPTIONAL* - The expression to evaluate over the Vine's scrubbed text description.|
| *grammarDependency* |  | *OPTIONAL* - Sets the filter to evaluate a specific grammar relation. |
| *relation* | RELATION_TAG | **MANDATORY** - Specifies the relation tag to evaluate if `grammarDependency` is set. |
| *governor* | FILTER_EXPRESSION | *OPTIONAL* - The expression to evaluate over the relation's governor value.|
| *dependent* | FILTER_EXPRESSION | *OPTIONAL* - The expression to evaluate over the relation's dependent value.|



The `FILTER_EXPRESSION` argument is constructed similarily to a typical if-statement. An explanation of each of the acceptable tokens for a filter expression is given in the table below.

**Filter Expression Syntax:**

    (<TOKEN> <LOGIC_TYPE> <TOKEN>) <LOGIC_TYPE>  (<TOKEN> <LOGIC_TYPE> <TOKEN>)
    
- NOTE: Nested parenthesis currently are not supported. (See [Possible Issues](#possible-issues))

**Token Syntax:**

    [@<NAME>:<VALUE>]

**Filter Expression:**

| Name | Values | Description |
|---|---|---|
| *TargetWord* | none or "NO_SPACES" | Represents any target word from the run configuration's target words file.  Use "NO_SPACES" to represent the target words as 1-grams, eg: for use in hashtags.|
| *Literal* | String literal| Represents the exact string literal specified after the ":".|
| *Strings* | <NAME_OF_STRINGS_OBJECT>| Represents the string values specified in the Strings Object.|
| *PosTag* | <POS_TAG> | Represents the required part-of-speech tag. For use in `grammarDependency` only.|
| *Parent* | "GOVERNOR" or "DEPENDENT" | Represents the entire filter expression inside the parent Filter's `governor` or `dependent` field.|


**Examples:**

    // /////////
    // FILTER //
    // /////////
    
    {	"entry":"filter",
		"name":"prep_like",
		"type":"void",
		"description":"Void the tweet if the target word exists inside a prepositional phrase using 'like'.  (Ex: 'red like an apple' prep_like(red,apple))",
		"grammarDependency": {
			"relation":"prep_like",
			"dependent":"([@TargetWord])"
			}
    }
    
> - Creates a void filter called "prep_like".
> - Sets a filter expression on the Vine object's "prep_like" grammar relation.
> - Voids the Vine if the Vine's meta-data has a "prep_like" grammar relation containing a target word as the dependent.

    // /////////
    // FILTER //
    // /////////
    
    {	"entry":"filter",
		"name":"hashtag",
		"type":"validate",
		"description":"Validate the tweet if a target word exists as a hashtag.",
		"textContains":"([@Literal:#][@TargetWord:NO_SPACES])"
    }
    
> - Creates a validate filter called "hashtag".
> - Sets a filter expression on the Vine object's text description.
> - Validates the Vine if the text contains a target word 1-gram with a '#' hashtag in front.


    // /////////
    // FILTER //
    // /////////
    
    {	"entry":"filter",
		"name":"demPronoun",
		"type":"validate",
		"description":"Validate the tweet if the target word is modified by a demonstrative pronoun. (Ex: 'watching this movie' det(movie,this))",
		"grammarDependency":{
		"relation":"det",
		"governor":"([@TargetWord])",
		"dependent":"([@Strings:DEMONSTRATIVE_PRONOUNS])"}
    }
    
> - Creates a validate filter called "demPronoun".
> - Sets a filter expression on the Vine object's "det" grammar relation.
> - Validates the Vine if the Vine's meta-data contains a target word in the governor and a string literal from the Strings Object named "DEMONSTRATIVE_PRONOUNS".



##Usage

To use the VineFilter, open a Terminal window, navigate to the directory containing the `filter.jar`  file, and append the following runtime arguments at launch:

> `java -jar filter.jar <FILEPATH_TO_SETTINGS_FILE> <DO_SINGLE_VINE>`


####Runtime arguments:

| Argument	| Description |
|---|---|
| *java -jar* |	**MANDATORY** - Runs Java expecting a runnable JAR file. |
| *filter.jar* | **MANDATORY** - Specifies the runnable JAR file. |
| *FILEPATH_TO_SETTINGS_FILE* |	The location to the settings file. |
| *DO_SINGLE_VINE* |	TRUE or FALSE.  Run VineFilter on an individual Vine object. (Default: FALSE) |

####Example runs:

> `java -jar -Xmx2048M vinescrape.jar`  

   - Launches VineScrape with all the default values.  

> `java -jar -Xmx2048M vinescrape.jar -1`  
   
   - Scrapes Vines ad infinitum until the run is killed.  
   - All remaining runtime arguments take their default values. 
   
> `java -jar -Xmx2048M vinescrape.jar -1 100000`   
   
   - Scrapes Vines ad infinitum until the run is killed.  
   - Kills run after 1,000,000 Tweets have been scraped.  
   - Remaining runtime argument takes its default value.    

> `java -jar -Xmx2048M vinescrape.jar -1 -1 10000`  
   
   - Scrapes Vines ad infinitum until the run is killed.   
   - Scrapes Tweets ad infinitum until the run is killed.  
   - Stores 10,000 Vine JSON objects per output file.   



##Requirements

VineScrape needs 2 gigabytes of memory in order to tag out the linguistic meta-data.

VineScrape also requires Java. Download the latest version [here](http://www.java.com/).

##Possible Issues

The actual number of vines per output file may vary by one or two vines due to concurrency.

##Credits

Mentor: Dr. Garrett Kenyan
