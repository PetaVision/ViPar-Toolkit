ImageNet Word List Generator
============================

Creates word lists of ImageNet hyponyms based on a user-specified category word.

##How it Works

ImageNet Word List Generator (**INWLG**) is a small Java tool that enables users to query [ImageNet](http://www.image-net.org/) to create lists of picturable noun words efficiently.

First, **INWLG** prompts the user for a category noun and specific definition, or gloss.  

Then a list of enumerated hyponyms is displayed and the option to exclude any hyponyms is given.  

Finally, everything is saved to a generated directory in JSON format.

**Example:**
    
    {   "category":"dog",
        "hyponyms":[
                  "affenpinscher",
		          "afghan hound",
		          "airedale",
		          "airedale terrier",
		          "alaskan malamute",
		          "alsatian",
		          "american foxhound",
		          "american pit bull terrier",
		          "american staffordshire terrier",
		          "american water spaniel",
		          "appenzeller",
		          "attack dog",
		          "australian terrier",
		          "badger dog",
		          "basenji",
		          "basset",
		          "basset hound",
		          "beagle",
		          "etc..."
              ],
    }

##Download

ImageNet Word List Generator comes as both a stand-alone, runnable JAR file and an open-source Java project available here in this repository. Users who only wish to use the program as-is should simply download the `inwlg.jar` file and run it. See [Usage](#usage).

##Usage

To use the tool, simply open a Terminal window, navigate to the directory containing the `inwlg.jar` file, and run the following command:

> `java -jar -Xmx1024M inwlg.jar`


####Command arguments:

| Argument	| Description |
|---|---|
| *java -jar* |	**MANDATORY** - Runs Java expecting a runnable JAR file. |
| *-Xmx1024M* | **MANDATORY** -	Gives the run a maximum of 1 gigabyte of memory. |
| *inwlg.jar* | **MANDATORY** - Specifies the runnable JAR file. |


##Requirements

VineScraper needs 1 gigabyte of memory in order to process the noun subset of [WordNet](http://wordnet.princeton.edu/) used by [ImageNet](http://www.image-net.org/).

VineScraper also requires Java. Download the latest version [here](http://www.java.com/).

##Credits

Mentor: Dr. Garrett Kenyan
