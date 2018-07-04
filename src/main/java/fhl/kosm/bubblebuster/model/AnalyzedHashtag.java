package fhl.kosm.bubblebuster.model;

import com.kennycason.kumo.nlp.FrequencyAnalyzer;

import javax.management.relation.Relation;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
