VineFetch
=====

Download Vine videos from URLs stored in a user-specified JSON file.

##How it Works

VineFetch is a Java program that reads in a user-specified JSON file containing URLs to Vine videos and downloads the .MP4 video content. Vine videos which no longer exist are skipped while succesfully downloaded videos are saved to a generated directory.

Finally, the meta-data of the successfully downloaded videos are stored in a generated `vines.json` file.

##Download

VineFetch comes as both a stand-alone, runnable JAR file and an open-source Java project available here in this repository. Users who only wish to use the program as-is should simply download the file below and run it. See [Usage](#usage).

##New Users



##Usage

To use the tool, simply open a Terminal window, navigate to the directory containing the `fetch.jar` , and append the following optional runtime arguments at launch:

> `java -jar fetch.jar <FILEPATH_TO_JSON_FILE> <NAME_OF_OUTPUT_DIRECTORY>`


####Runtime arguments:

| Argument	| Description |
|---|---|
| *java -jar* |	**MANDATORY** - Runs Java expecting a runnable JAR file. |
| *fetch.jar* | **MANDATORY** -	Specifies the runnable JAR file. |
| *FILEPATH_TO_JSON_FILE* |	**MANDATORY** - Filepath to JSON file containing download urls. |
| *NAME_OF_OUTPUT_DIRECTORY* | **MANDATORY** - Name of output directory. Be descriptive! |

####Example runs:

> `java -jar fetch.jar`  

   - Launches VineScraper with all the default values.  

> `java -jar fetch.jar`  
   
   - Scrapes Vines ad infinitum until the run is killed.  
   - All remaining runtime arguments take their default values. 
   
> `java -jar fetch.jar`   
   
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
