VineFetch
=====

Download Vine videos from URLs stored in a user-specified JSON file.

##How it Works

VineFetch is a Java program that reads in a user-specified JSON file containing URLs to Vine videos and downloads the (.MP4) video content. Vine videos which no longer exist are skipped while succesfully downloaded videos are saved to a generated directory.

Finally, the meta-data of the successfully downloaded videos are stored in a generated `vines.json` file.

##Download

VineFetch comes as both a stand-alone, runnable JAR file and an open-source Java project available here in this repository. Users who only wish to use the program as-is should simply download the file below and run it. See [Usage](#usage).

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

####Example run:

> `java -jar fetch.jar filter-result.json dog`  

   - Launches VineFetch on the file `filter-result.json` and stores the (.MP4) files in a directory called "dog". 


##Requirements

VineScraper requires Java. Download the latest version [here](http://www.java.com/).

##Possible Issues

The actual number of videos downloaded may be fewer than the number in the JSON file since some videos may no longer exist on Vine.

##Credits

Mentor: Dr. Garrett Kenyan
