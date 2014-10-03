#ViPar Toolkit

Scrape, process, and filter Twitter's Vine video service to create unique video datasets on-the-fly.

The code in this respository should be used in the following order:

###1. [VineScrape](https://github.com/DannyDelott/ViPar-Toolkit/tree/master/VineScrape)
Collects annotated links to Vine videos from the Twitter Streaming API and generates linguistic meta-data. Requires a Twitter developer account.

###2. [ImageNet Word list Generator](https://github.com/DannyDelott/ViPar-Toolkit/tree/master/ImageNet%20Word%20List%20Generator)
Creates word lists of ImageNet hyponyms based on a user-specified category word.
Need to generate list prior to running VineFilter.

###3. [VineFilter](https://github.com/DannyDelott/ViPar-Toolkit/tree/master/VineFilter)
Filters Vine video text descriptions by their linguistic meta-data (ie: parts-of-speech and grammar relationships) in order to return videos containing a high-probability of having the user-specified visual object/entity in the video content itself. The real **power** of the ViPar toolkit is demonstrated in this tool.

###4. [VineFetch](https://github.com/DannyDelott/ViPar-Toolkit/tree/master/VineFetch)
Downloads Vine videos from URLs stored in a user-specified JSON file.

###5. [VineAnalyze](https://github.com/DannyDelott/ViPar-Toolkit/tree/master/VineAnalyze)
Easily view and annotate Vine videos downloaded by VineFetch. Much faster than individually opening files.

