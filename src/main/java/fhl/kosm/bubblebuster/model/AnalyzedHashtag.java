package fhl.kosm.bubblebuster.model;

import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import fhl.kosm.bubblebuster.repositories.MapUtil;

import javax.management.MalformedObjectNameException;
import javax.management.relation.Relation;
import javax.swing.text.html.parser.Entity;
import java.util.*;

public class AnalyzedHashtag {

    private final Hashtag hashtag;

    private List<TweetRelation> relations = new LinkedList<>();

    private List<String> embeddedTweets = new LinkedList<>();

    private String wordcloud;

    public AnalyzedHashtag(Hashtag hashtag) {
        this.hashtag = hashtag;
    }

    public List<TweetRelation> getRelations() {
        return relations;
    }

    public List<Map.Entry<String, String>> getUsages() {
        Map<String, String> withValue = new HashMap<>();
        long c = hashtag.relationsCount();
        for (Map.Entry<String, Long> entry :hashtag.relations().entrySet()) {
            int rate = (int) (entry.getValue() / (double) c * 100) ;
            if (rate > 0) {
                withValue.put("#" + entry.getKey(), String.valueOf(rate));
            }
        }
        List<Map.Entry<String, String>> topTen = new ArrayList<>(5);
        int i = 0;
        for (Map.Entry<String, String> entry : MapUtil.sortByValueDescending(withValue).entrySet()) {
            topTen.add(entry);
            if (i++ > 5) {
                break;
            }
        }
        return topTen;
    }

    public Hashtag getHashtag() {
        return hashtag;
    }

    public void addRelation(Hashtag other) {
        relations.add(new TweetRelation(hashtag, other));
    }

    public String getWordcloud() {
        return wordcloud;
    }

    public void setWordcloud(String wordcloud) {
        this.wordcloud = wordcloud;
    }

    public List<String> getEmbeddedTweets() {
        return embeddedTweets;
    }

    public void setEmbeddedTweets(List<String> embeddedTweets) {
        this.embeddedTweets = embeddedTweets;
    }

    public Set<Tweet> tweetsOfMostRelatedHashtag() {
        Hashtag mostRelated = mostRelatedHashtag();
        return mostRelated == null ? Collections.emptySet() : mostRelatedHashtag().getTweets();
    }

    public Hashtag mostRelatedHashtag() {
        TweetRelation mostRelated = null;
        for (TweetRelation relation : relations) {
            if (mostRelated == null) {
                mostRelated = relation;
            } else if (relation.intersectionPercentage() > mostRelated.intersectionPercentage()) {
                mostRelated = relation;
            }
        }
        if (mostRelated == null) {
            return null;
        }
        return mostRelated.to();
    }

}
