package fhl.kosm.bubblebuster;

import com.mongodb.MongoClient;
import fhl.kosm.bubblebuster.model.Hashtag;
import fhl.kosm.bubblebuster.model.Tweet;
import fhl.kosm.bubblebuster.repositories.TweetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import twitter4j.HashtagEntity;
import twitter4j.Status;
import twitter4j.URLEntity;

import java.util.*;

import static org.springframework.data.mongodb.core.query.Criteria.where;

public class DataCleanUpService {

    Map<String, Tweet> tweets = new HashMap<>();

    Map<String, Hashtag> hashtags = new HashMap<>();

    public Set<Tweet> tweetsOf(String tag) {
//        Hashtag hashtag = mongoOps.findOne(new Query(where("tag").is(tag.trim().toLowerCase())), Hashtag.class);
        Hashtag hashtag = hashtags.get(tag.toLowerCase());
        if (hashtag == null) {
            return Collections.emptySet();
        }
        return hashtag.getTweets();
    }

    public List<Tweet> tweetsOf(String[] tags) {
        List<Tweet> tweets = new LinkedList<>();
        for (String tag : tags) {
            tweets.addAll(tweetsOf(tag));
        }
        return tweets;
    }

    public Hashtag get(String tag) {
        return hashtags.get(tag.toLowerCase());
    }

    private Tweet byId(long id) {
        return tweets.get("" + id);
    }

    public Tweet update(Status status) {
        Tweet tweet = byId(status.getId());
        if (tweet == null) {
            tweet = create(status);
            tweets.put("" + tweet.getId(), tweet);
        }
        return tweet;
    }

    private Tweet create(Status status) {
        Tweet tweet = new Tweet(status.getId());
        tweet.setUrl("https://twitter.com/" + status.getUser().getScreenName() + "/status/" + status.getId());
        List<String> tags = toStringList(status.getHashtagEntities());
        tweet.setHashtags(tags);
        updateHashtags(tweet);
        return tweet;
    }

    private List<String> toStringList(HashtagEntity[] hashtags) {
        if (hashtags.length == 0) {
            return Collections.emptyList();
        }
        List<String> tags = new ArrayList<>(hashtags.length);
        for (HashtagEntity tag : hashtags) {
            tags.add(tag.getText());
        }
        return  tags;
    }

    private void updateHashtags(Tweet tweet) {
        Hashtag tag;
        Hashtag related;
        List<String> tags = tweet.getHashtags();
        for (int i = 0; i < tags.size() - 1; i++) {
            for (int j = i + 1; j < tags.size(); j++) {
                tag = getOrCreate(tags.get(i), tweet);
                related = getOrCreate(tags.get(j), tweet);
                if (tag.equals(related)) {
                    continue;
                }
                // we can do some because the tweets are stored in a hashset.
                // tweets override the hashfunction with the hash of their id.
                tag.addTweet(tweet);
                related.addTweet(tweet);
                tag.addRelation(related);
                related.addRelation(tag);
            }
        }
    }

    private Hashtag getOrCreate(String tag, Tweet tweet) {
        Hashtag hashtag = get(tag);
        if (hashtag == null) {
            hashtag = create(tweet, tag);
        }
        return hashtag;
    }

    private Hashtag create(Tweet tweet, String tag) {
        Hashtag hashtag = new Hashtag(tag);
        hashtag.addTweet(tweet);
        hashtags.put(hashtag.getTag(), hashtag);
        return hashtag;
    }

    public void save() {
        MongoTemplate mongoOps = new MongoTemplate(new MongoClient(), "twitter");
        hashtags.values().forEach(mongoOps::save);
        tweets.values().forEach(mongoOps::save);
    }

}
