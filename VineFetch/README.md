VineFetch
=====

Download Vine videos from URLs stored in a user-specified JSON file.

##How it Works

VineFetch is a Java program that reads in a user-specified JSON file containing URLs to Vine videos and downloads the .MP4 video content. Vine videos which no longer exist are skipped while succesfully downloaded videos are saved to a generated directory.

Finally, the meta-data of the successfully downloaded videos are stored in a generated `vines.json` file.

##Download

VineScraper comes as both a stand-alone, runnable JAR file and an open-source Java project available here in this repository. Users who only wish to use the program as-is should simply download the file below and run it. See [Usage](#usage).

#####[vinescraper.jar](https://drive.google.com/file/d/0B2ZkhS0DTgolNHRRblRUWFJYMkE/edit?usp=sharing) **(Click to download)**

*Due to the filesizes of the Stanford CoreNLP libary and the CMU Twitter Part-of-Speech Tagger the stand-alone, runnable JAR file is too large (~123MB) to host on Github.*

##New Users

You must create a Twitter application to begin accessing the APIs. Visit [dev.twitter.com/apps](https://apps.twitter.com/) and log in with your Twitter account to create a new app. You will then be given your API keys.

Make a new file called `twitter_credentials.json` and copy and paste the following snippet inside, substituting in your API keys. Save this file in the same directory as `vinescraper.jar`.

**twitter_credentials.json**

    {   "consumer_key":"*************************",
        "consumer_secret":"**************************************************",
        "access_token":"**************************************************",
        "access_secret":"*********************************************" 
    }

##Usage

To use the tool, simply open a Terminal window, navigate to the directory containing the `vinescraper.jar` and your `twitter_credentials.json` file, and append the following optional runtime arguments at launch:

> `java -jar -Xmx2048M vinescraper.jar <NUM_VINES_TO_SCRAPE> <NUM_TWEETS_TO_SCRAPE> <NUM_VINES_PER_OUTPUT_FILE>`


####Runtime arguments:

| Argument	| Description |
|---|---|
| *java -jar -Xmx2048M* |	**MANDATORY** - Runs Java with 2 gigabytes of memory expecting a runnable JAR file. |
| *vinescraper.jar* | **MANDATORY** -	Specifies the runnable JAR file. |
| *NUM_VINES_TO_SCRAPE* |	Number of vines to scrape. (Default: 10,000,000) |
| *NUM_TWEETS_TO_SCRAPE* |	Number of Tweet objects to collect before finishing the run. (Default: -1 for infinite) |
| *NUM_VINES_PER_OUTPUT_FILE* |	Number of scraped Vine JSON objects to output per file. (Default: 1,000) |

####Example runs:

> `java -jar -Xmx2048M vinescraper.jar`  

   - Launches VineScraper with all the default values.  

> `java -jar -Xmx2048M vinescraper.jar -1`  
   
   - Scrapes Vines ad infinitum until the run is killed.  
   - All remaining runtime arguments take their default values. 
   
> `java -jar -Xmx2048M vinescraper.jar -1 100000`   
   
   - Scrapes Vines ad infinitum until the run is killed.  
   - Kills run after 1,000,000 Tweets have been scraped.  
   - Remaining runtime argument takes its default value.    

> `java -jar -Xmx2048M vinescraper.jar -1 -1 10000`  
   
   - Scrapes Vines ad infinitum until the run is killed.   
   - Scrapes Tweets ad infinitum until the run is killed.  
   - Stores 10,000 Vine JSON objects per output file.   



##Requirements

VineScraper needs 2 gigabytes of memory in order to tag out the linguistic meta-data.

VineScraper also requires Java. Download the latest version [here](http://www.java.com/).

##Possible Issues

The actual number of vines per output file may vary by one or two vines due to concurrency.

##Credits

Mentor: Dr. Garrett Kenyan
