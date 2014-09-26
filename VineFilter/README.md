 
#VineFilter
Filters Vine video text descriptions by their linguistic meta-data (ie: parts-of-speech and grammar relationships) in order to return videos containing a high-probability of having the user-specified visual object/entity in the video content itself.

The real power of the ViPar toolkit is demonstrated in this tool. 

##How it Works

VineFilter is a Java program that uses Vine meta-data obtained via [VineScrape](https://github.com/DannyDelott/ViPar-Toolkit/tree/master/VineScrape) to select specific videos based on any number of user-specified criteria.  

VineFilter uses a settings file that contains all of these user-specified criteria to return datasets of vine videos. See [Setup](#setup) to learn how to create a VineFilter settings file.

##Download

VineFilter comes as both a stand-alone, runnable JAR file and an open-source Java project available here in this repository. Users who only wish to use the program as-is should simply download the `filter.jar` file and run it. See [Usage](#usage).

##Setup

This section covers the creation of the VineFilter settings file.  This file is separated into three parts, a **Run Configuration**, **Strings Objects**, and **Filters**. In order for VineFilter to successfully read in your settings file, please be sure your settings file conforms to the syntax described in the sections below.

---

####1. Run Configuration

**MANDATORY** - The **Run Configuration** is the first element in the settings file. It specifies the location to the Vine meta-data along with other necessary information about the VineFilter run you are going to launch.

**settings.json**

    // ////////////////////
    // RUN CONFIGURATION //
    // ////////////////////
    
    {	"entry":"config",
     "startAt":100,
	    "numToCollect":100,
	    "validationQuota":2,
	    "targetWordsFile":"/Users/dannydelott/Desktop/TargetWords/tw-cifar-dog.json",
	    "datasetDirectory":"/Users/dannydelott/Desktop/vine_english",
	    "posTagsFile":"/Users/dannydelott/Desktop/vine_english/pos_tags.txt",
	    "relationTagsFile":"/Users/dannydelott/Desktop/vine_english/relation_tags.txt"
	    }

##Usage

To use the tool, simply open a Terminal window, navigate to the directory containing the `vinescrape.jar` and your `twitter_credentials.json` file, and append the following optional runtime arguments at launch:

> `java -jar -Xmx2048M vinescrape.jar <NUM_VINES_TO_SCRAPE> <NUM_TWEETS_TO_SCRAPE> <NUM_VINES_PER_OUTPUT_FILE>`


####Runtime arguments:

| Argument	| Description |
|---|---|
| *java -jar -Xmx2048M* |	**MANDATORY** - Runs Java with 2 gigabytes of memory expecting a runnable JAR file. |
| *vinescrape.jar* | **MANDATORY** -	Specifies the runnable JAR file. |
| *NUM_VINES_TO_SCRAPE* |	Number of vines to scrape. (Default: 10,000,000) |
| *NUM_TWEETS_TO_SCRAPE* |	Number of Tweet objects to collect before finishing the run. (Default: -1 for infinite) |
| *NUM_VINES_PER_OUTPUT_FILE* |	Number of scraped Vine JSON objects to output per file. (Default: 1,000) |

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
