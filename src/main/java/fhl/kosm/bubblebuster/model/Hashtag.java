package fhl.kosm.bubblebuster.model;

import org.springframework.data.annotation.Id;

import java.util.*;

public class Hashtag {

    @Id
    private String tag;

    private String displayName;

    private Map<String, Long> related = new HashMap<>();

    private Set<Tweet> tweets = new HashSet<>();

    public Hashtag(String tag) {
        this.tag = tag.toLowerCase();
        displayName = tag;
    }

    public void addRelation(Hashtag hashtag) {
        Long count = related.get(hashtag.toString());
        if (count == null) {
            count = 0l;
        }
        related.put(hashtag.toString(), ++count);
        Relation<Hashtag> relation = new Relation(this, hashtag);
        related.get(relation);
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public String toString() {
        return tag;
    }

    public Set<Tweet> getTweets() {
        return tweets;
    }

    public Set<String> relatedHashtags() {
        return related.keySet();
    }

    public Map<String, Long> relations() {
        return related;
    }

    public long relationsCount() {
        long sum = 0;
        for (Long l : related.values()) {
            sum += l;
        }
        return sum;
    }

    public boolean addTweet(Tweet tweet) {
        return tweets.add(tweet);
    }

}
