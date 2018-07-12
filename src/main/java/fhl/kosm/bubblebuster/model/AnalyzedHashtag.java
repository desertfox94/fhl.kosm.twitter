package fhl.kosm.bubblebuster.model;

import fhl.kosm.bubblebuster.repositories.MapUtil;

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
        for (Map.Entry<String, Long> entry :hashtag.relationsInverted().entrySet()) {
            int rate = (int) (entry.getValue() / (double) c * 100) ;
            if (rate > 0) {
                withValue.put("#" + entry.getKey(), String.valueOf(rate));
            }
        }
        List<Map.Entry<String, String>> topTen = new ArrayList<>(5);
        for (Map.Entry<String, String> entry : MapUtil.sortByValueDescending(withValue).entrySet()) {
            if (topTen.size() >= 5) {
                break;
            }
            if (topTen.stream().allMatch(e->!e.getValue().equals(entry.getValue()))) {
                topTen.add(entry);
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

    public Set<Tweet> tweetsOfLessRelatedHashtag() {
        Hashtag mostRelated = lessRelatedHashtag();
        return mostRelated == null ? Collections.emptySet() : lessRelatedHashtag().getTweets();
    }

    public Hashtag lessRelatedHashtag() {
        TweetRelation lessRelated = null;
        for (TweetRelation relation : relations) {
            if (lessRelated == null) {
                lessRelated = relation;
            } else if (relation.intersectionPercentage() < lessRelated.intersectionPercentage()) {
                lessRelated = relation;
            }
        }
        if (lessRelated == null) {
            return null;
        }
        List<TweetRelation> all = new LinkedList<>();
        for (TweetRelation relation : relations) {
            if (lessRelated.intersection().size() == relation.intersection().size()) {
                all.add(relation);
            }
        }
        return all.get((int) (Math.random() * all.size())).to();
    }

}
