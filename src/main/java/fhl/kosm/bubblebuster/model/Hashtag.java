package fhl.kosm.bubblebuster.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

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
        Long count = related.get(hashtag.getTag());
        if (count == null) {
            count = 0l;
        }
        related.put(hashtag.getTag(), ++count);
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

    public Map<String, Long> relationsInverted() {
        Map<String, Long> inverted = new HashMap<>(related.size());
        Map.Entry<String, Long> mostRelated = mostRelated();
        if (mostRelated == null) {
            return Collections.emptyMap();
        }
        long count = mostRelated().getValue() + 1;
        for (Map.Entry<String, Long> e: related.entrySet()) {
            inverted.put(e.getKey(), count - e.getValue());
        }
        return inverted;
    }

    public Map.Entry<String, Long> mostRelated() {
        long max = 0;
        Map.Entry<String, Long> mostRelated = null;
        for (Map.Entry<String, Long> e : related.entrySet()) {
            if (e.getValue() > max) {
                max = e.getValue();
                mostRelated = e;
            }
        }
        return mostRelated;
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

    public String getDisplayName() {
        return displayName;
    }

    public String getTag() {
        return tag;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Hashtag) {
            return ((Hashtag) obj).getTag().equals(tag);
        }
        return false;
    }
}
