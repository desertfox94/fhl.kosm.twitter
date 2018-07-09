package fhl.kosm.bubblebuster;

import com.mongodb.MongoClient;
import fhl.kosm.bubblebuster.model.Hashtag;
import fhl.kosm.bubblebuster.model.Tweet;
import fhl.kosm.bubblebuster.repositories.HashtagRepository;
import fhl.kosm.bubblebuster.repositories.TweetRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import twitter4j.HashtagEntity;
import twitter4j.Status;

import java.util.*;
import java.util.function.Consumer;

public class TweetService {

    private HashtagRepository hashtagRepository;

    private TweetRepository tweetRepository;

    private Optional<Consumer<Hashtag>> updatedHashtag = Optional.empty();

    public TweetService(HashtagRepository hashtagRepository, TweetRepository tweetRepository) {
        this.hashtagRepository =hashtagRepository;
        this.tweetRepository = tweetRepository;
    }

    public Set<Tweet> tweetsOf(String tag) {
        Hashtag hashtag =null;
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
        Optional<Hashtag> opt = hashtagRepository.findById(tag.toLowerCase());
        return opt.isPresent() ? opt.get() : null;
    }

    private Tweet byId(long id) {
        Optional<Tweet> tweet = tweetRepository.findById(id);
        return tweet.isPresent() ? tweet.get() : null;
    }

    public Tweet update(Status status) {
        Tweet tweet = byId(status.getId());
        if (tweet == null) {
            tweet = create(status);
            tweetRepository.save(tweet);
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
            tag = getOrCreate(tags.get(i), tweet);
            for (int j = i + 1; j < tags.size(); j++) {
                related = getOrCreate(tags.get(j), tweet);
                if (tag.equals(related)) {
                    continue;
                }
                updatedHashtag(tweet, tag, related);
            }
        }
    }

    private void updatedHashtag(Tweet tweet, Hashtag tag, Hashtag related) {
        tag.addTweet(tweet);
        related.addTweet(tweet);
        tag.addRelation(related);
        related.addRelation(tag);
        hashtagRepository.save(tag);
        hashtagRepository.save(related);
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
        hashtagRepository.save(hashtag);
        return hashtag;
    }

    public void setUpdatedHashtag(Consumer<Hashtag> consumer) {
        updatedHashtag = Optional.of(consumer);
    }

}
