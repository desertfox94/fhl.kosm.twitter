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

public class TweetService {

    @Autowired
    private TweetRepository repository;

    MongoOperations mongoOps = new MongoTemplate(new MongoClient(), "twitter");

    public List<Hashtag> all() {
        return mongoOps.findAll(Hashtag.class);
    }

    public Set<Tweet> tweetsOf(String tag) {
        Hashtag hashtag = mongoOps.findOne(new Query(where("tag").is(tag.trim().toLowerCase())), Hashtag.class);
        if (tag == null) {
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
        System.out.println("repo: " + repository);
        return mongoOps.findOne(new Query(where("_id").is(tag.toLowerCase())), Hashtag.class);
    }

    private Tweet byId(long id) {
        return mongoOps.findOne(new Query(where("id").is(id)), Tweet.class);
    }

    public Tweet update(Status status) {
        Tweet tweet = byId(status.getId());
        if (tweet == null) {
            tweet = create(status);
            mongoOps.save(tweet);
            mongoOps.save(status);
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
        for (int i = 0; i < tags.size(); i++) {
            for (int j = i + 1; i < tags.size() - 1; i++) {
                tag = getOrCreate(tags.get(i), tweet);
                related = getOrCreate(tags.get(j), tweet);
                tag.addTweet(tweet);
                related.addTweet(tweet);
                tag.addRelation(related);
                related.addRelation(tag);
                mongoOps.save(tag);
                mongoOps.save(related);
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
        mongoOps.save(hashtag);
        return hashtag;
    }

}
