package fhl.kosm.bubblebuster.analyse;

import fhl.kosm.bubblebuster.model.*;
import fhl.kosm.bubblebuster.repositories.HashtagRepository;
import twitter4j.OEmbed;
import twitter4j.OEmbedRequest;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import java.util.*;
import java.util.List;

public class HashtagAnalyser {

    private HashtagRepository repository;

    private WordcloudCreator wordcloud = new WordcloudCreator();

    public HashtagAnalyser(HashtagRepository repository) {
        this.repository = repository;
    }

    private List<String> trimToLower(String[] tags) {
        List<String> trimmed = new ArrayList<>(tags.length);
        for (String tag: tags) {
            trimmed.add(tag.trim().toLowerCase());
        }
        return trimmed;
    }

    public List<AnalyzedHashtag> relations(String... tags) {
        Iterable<Hashtag> hashtags = repository.findAllById(trimToLower(tags));
        List<AnalyzedHashtag> analyzedHashtags = new ArrayList<>(tags.length);
        AnalyzedHashtag analyzedHashtag;
        for (Hashtag h1 : hashtags) {
            analyzedHashtag = new AnalyzedHashtag(h1);
            analyzedHashtags.add(analyzedHashtag);
            analyzedHashtag.setWordcloud(wordcloud.asBase64(h1));
            for (Hashtag h2 : hashtags) {
                if (h1 == h2)
                    continue;
                analyzedHashtag.addRelation(h2);
            }
            analyzedHashtag.setEmbeddedTweets(loadTweetsOfMostRelatedHashtag(analyzedHashtag));
        }
        return analyzedHashtags;
    }

    private List<String> loadTweetsOfMostRelatedHashtag(AnalyzedHashtag analyzedHashtag) {
        return loadTweetsOfMostRelatedHashtag(analyzedHashtag, 2);
    }

    private List<String> loadTweetsOfMostRelatedHashtag(AnalyzedHashtag analyzedHashtag, int limit) {
        Set<Tweet> tweets = analyzedHashtag.tweetsOfMostRelatedHashtag();
        List<String> result = new ArrayList<>(limit);
        int i = 0;
        for (Tweet tweet : tweets) {
            if (i++ == limit)
                break;
            try {
                OEmbedRequest oEmbedRequest = new OEmbedRequest(tweet.getId(), tweet.getUrl());
                OEmbed embed = TwitterFactory.getSingleton().getOEmbed(oEmbedRequest);
                result.add(embed.getHtml());
            } catch (TwitterException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

}
