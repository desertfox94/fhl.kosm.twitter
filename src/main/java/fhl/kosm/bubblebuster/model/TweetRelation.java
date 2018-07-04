package fhl.kosm.bubblebuster.model;

import java.util.HashSet;
import java.util.Set;

public class TweetRelation {

    private Hashtag from;

    private Hashtag to;

    private String wordcloud;

    private Set<String> intersection;

    public TweetRelation(Hashtag from, Hashtag to) {
        this.from = from;
        this.to = to;
    }

    public Set<String> intersection() {
        if (intersection != null) {
            return intersection;
        }
        intersection = new HashSet<>();
        for (String tag : from.relatedHashtags()) {
            if (to.relatedHashtags().contains(tag)) {
                intersection.add(tag);
            }
        }
        return intersection;
    }

    public double intersectionPercentage() {
        return intersection().size() / (double) from.relationsCount();
    }

    public double intersectionPercentageInverted() {
        return 1 - intersectionPercentage();
    }

    public Hashtag from() {
        return from;
    }

    public Hashtag to() {
        return to;
    }

    public String getWordcloud() {
        return wordcloud;
    }

    public void setWordcloud(String wordcloud) {
        this.wordcloud = wordcloud;
    }

    @Override
    public String toString() {
        return String.format("%.4f %s -> %s : %s", intersectionPercentage(), from().toString(), to().toString(), intersection().toString());
    }

}
