ViPar Toolkit
=====

Scrape, process, and filter Twitter's Vine video service to create unique video datasets on-the-fly.

1. BACKGROUND
 
Computer vision algorithms can be trained to recognize objects by learning on video datasets of labeled categories.  Today’s computer vision datasets contain discrete numbers of videos or still-images, such as the NeoVision2 Heli and Tower Datasets1 or the ImageNet Image Dataset2.  However, high-quality datasets like these take time to create are often expensive to setup, film, and annotate.  Additionally, video datasets like these are limited in the breadth of object categories represented and in the amount of training data available.
 
Twitter’s Vine service contains hundreds of millions of videos between 1 and 6 seconds long that often have short, 140-character max “tweets” associated with them.  The tweets are typically descriptive text about the video content, although not strictly so.  Using a set of predefined grammar filters, ViPar attempts to analyze the tweet text and identify videos that have a high probability of containing user-specified target words within the video content itself.

Included in the ViPar toolkit are programs to handle scraping an initial stream of Vine videos, parsing the scraped Vine content, and fetching/analyzing the video results.  The current progress of each of these programs will be discussed in this report.

2. SCRAPING VINES

Vine itself does not currently offer an API for programmers to gain access to their vast database of videos.  Instead, the ViPar toolkit must scrape the Twitter Streaming API3, which provides a constant feed of newly-posted tweets, and look for the string literal, “vine.co/v/” in the JSON output.  Tweets containing this string are scraped for compulsory metadata, i.e. the Vine ID, video page URL, and the tweet text.  Also, since the Twitter Streaming API provides functionality to filter by language, English-only vines can be targeted at the scrape-level to save time when processing the Vines in the next step.

Access to the Twitter Streaming API is obtained through the open-source Java library Twitter4J4. As of now, the ViPar toolkit does not contain a functional scraping tool, because a sufficient database of vine videos was provided by the New Mexico Consortium to begin developing a filtering tool right away.  A scraping tool will be available in a later release of the ViPar toolkit, which will output the scraped content in JSON-format to be read directly into the Vine processing tool described below.


3. PROCESSING VINES

Processing a vine means to append the part-of-speech tags and grammar dependencies to the metadata associated with the vine.  It also includes segmenting and scrubbing out hashtags, at-mentions, emojis, excessive punctuation, or any other linguistic phenomena regularly found in social media microtext.  This must happen before ViPar can identify videos with high probabilities of containing a target category.

3a. Part-of-speech tagging

The vine processing tool first runs the CMU ARK Twitter Part-of-Speech Tagger5 to assign all words in the tweet text with their respective Penn Treebank POS Tags6.  This part-of-speech tagger was chosen explicitly for its ability to label Twitter-specific tokens, ie: hashtags (HT), retweets (RT), at-mentions (@), URLs (U), and emoticons (E).  

3b. Hashtag segmenting and text scrubbing

Once the parts-of-speech tags are assigned, Twitter-specific tokens must be segmented and/or scrubbed.  Two common scenarios where this step applies are below.

(1) It is not uncommon for tweets to contain hashtags in the middle of the text body, eg: “making a #sandwich for lunch”.

(2) Twitterspeak oftentimes relies on lists of hashtags at the end of the text body, eg: “making a sandwich for lunch #turkey  #lunch #sandwich”.

In the first example, “#sandwich” would be labelled as a hashtag (HT), and not a singular noun (NN). Since it exists in the middle of the text, it gets segmented by the ViPar Hashtag Segmenter (see Section 7a).  The resulting segmentation would output “making a sandwich for lunch”.

In the second example, a list of hashtags is found at the end of the tweet text.  These are removed, or scrubbed, by the ViPar Tweet Scrubber (see Section 7b).  Here, scrubber would output “making a sandwich for lunch”.

After segmenting and scrubbing have been performed, the parts-of-speech are re-tagged in order to feed proper English PTB-tags and tokens into the grammar dependency parsing step (next page).

3c. Grammar dependency parsing

Finally, we must assign the grammar dependencies to the tokens, since ViPar analyzes the relationships between parts-of-speech in order to return filtered vines. This is accomplished via the Stanford Dependency Parser7.  To use the example scrubbed tweet from (3b), this step would identify “sandwich-NN” as the direct object of the gerund verb, “making-VBG”.  

3d. Summary

As vines are processed, all of the generated metadata is stored in JSON-format to be fed directly into the ViPar filtering tool described in Section 4. 

The diagram below shows the order of operations performed when processing a vine that has been scraped from the Twitter Streaming API. The ViPar Processing Tool is currently available for use and can be found here:  [GITHUB LINK]

<img src='http://static.dyp.im/L0V9bLKgFp/5de76317296c44442c62c32d8cd94fec.png' alt='Screen Shot 2014-09-26 at 1.22.17 PM' title='Screen Shot 2014-09-26 at 1.22.17 PM' />


4. FILTERING VINES

With a set of processed vines obtained in the previous section, ViPar can begin to filter them based on numerous user-specified criteria.  The real power of the ViPar toolkit is demonstrated through the ViPar Filtering Tool. 

4a. Building the ViPar settings file

The filtering tool uses a settings file that contains all of the user-specified criteria to return datasets of vine videos.  The settings file has 3 unique sections that each have parameters 


CITATIONS

1  DARPA Neovision2 Heli and Tower Datasets [http://ilab.usc.edu/neo2/dataset/]

2 ImageNet Object Recognition Challenge [http://www.image-net.org/]

3 Twitter Streaming APIs [https://dev.twitter.com/docs/streaming-apis/streams/public]

4 Twitter4J [http://twitter4j.org/en/index.html]

5 CMU ARK Twitter Part-of-Speech Tagger [http://www.ark.cs.cmu.edu/TweetNLP/]

6 Penn Treebank POS Tags [https://www.ling.upenn.edu/courses/Fall_2003/ling001/penn_treebank_pos.html]

7 Stanford Parser [http://nlp.stanford.edu/software/stanford-dependencies.shtml]
