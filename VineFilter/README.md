 
#VineFilter
Filters Vine video text descriptions by their linguistic meta-data (ie: parts-of-speech and grammar relationships) in order to return videos containing a high-probability of having the user-specified visual object/entity in the video content itself.

##How it Works

VineScrape is a multi-threaded Java program that accesses the [Twitter Streaming API](https://dev.twitter.com/streaming/overview) and collects Status objects that contain unique URLs to Vine videos.

VineScrape then navigates to the URLs and parses out the text description (if any), ID, and full URL to the .mp4 video. The text description is scrubbed of emoticons and other micro-text phenomena and an attempt to segment interior hashtags is made.

Next, VineScrape tags out the scrubbed text description's parts-of-speech using the [CMU Twitter Part-of-Speech Tagger](http://www.ark.cs.cmu.edu/TweetNLP/). The POS tags are then fed into the [Stanford CoreNLP Grammar Dependency Parser](http://nlp.stanford.edu/software/stanford-dependencies.shtml) to tag out the grammar relationships.

Finally, everything is saved to a generated directory in JSON format.

**Example:**
    
    {   "id":514887792674209792,
        "url":"http://vine.co/v/b003Tzbjxxd",
        "text":"Cutest thing I've ever seen. Perfect timing! \\ud83d\\ude38\\ud83d\\ude3b\\ud83d\\ude39",
        "scrubbed_text":"Cutest thing I've ever seen. Perfect timing!",
        "pos_tags":["JJS-Cutest",
                    "NN-thing",
                    "PRP-I've",
                    "RB-ever",
                    "VBN-seen",
                    ".-.",
                    "JJ-Perfect",
                    "NN-timing",
                    ".-!"
                    ],
        "grammar_dependencies":["nsubj(Perfect-7,Cutest-1)",
                                "dep(Cutest-1,thing-2)",
                                "dobj(seen-5,thing-2)",
                                "nsubj(seen-5,I've-3)",
                                "advmod(seen-5,ever-4)",
                                "rcmod(thing-2,seen-5)",
                                "root(ROOT-0,Perfect-7)",
                                "dobj(Perfect-7,timing-8)"
                                ],
        "download_url":"https://v.cdn.vine.co/r/videos/9477573C-A7B9-4718-8F91-DD7F960678FD-28581-000015F9AA8A427D_1.1.mp4"
    }

##Download

VineScrape comes as both a stand-alone, runnable JAR file and an open-source Java project available here in this repository. Users who only wish to use the program as-is should simply download the file below and run it. See [Usage](#usage).

#####[vinescrape.jar](https://drive.google.com/file/d/0B2ZkhS0DTgoleFFvazZ2dFh5eFE/edit?usp=sharing) **(Click to download)**

*Due to the filesizes of the Stanford CoreNLP libary and the CMU Twitter Part-of-Speech Tagger the stand-alone, runnable JAR file is too large (~123MB) to host on Github.*

##New Users

You must create a Twitter application to begin accessing the APIs. Visit [dev.twitter.com/apps](https://apps.twitter.com/) and log in with your Twitter account to create a new app. You will then be given your API keys.

Make a new file called `twitter_credentials.json` and copy and paste the following snippet inside, substituting in your API keys. Save this file in the same directory as `vinescrape.jar`.

**twitter_credentials.json**

    {   "consumer_key":"*************************",
        "consumer_secret":"**************************************************",
        "access_token":"**************************************************",
        "access_secret":"*********************************************" 
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
