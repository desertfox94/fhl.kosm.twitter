package fhl.kosm.twitter.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class TweetRelation {

	private String hashtag;
	
	private Map<String, Set<Long>> relations = new HashMap<>();
	
	public TweetRelation(String hashtag) {
		this.hashtag = hashtag;
	}

	public String getHashtag() {
		return hashtag;
	}

	public Map<String, Set<Long>> getRelations() {
		return relations;
	}

	public void add(String hashtag, long tweet) {
		Set<Long> tweets = relations.get(hashtag);
		if (tweets == null) {
			tweets = new HashSet<>();
			relations.put(hashtag, tweets);
		}
		tweets.add(tweet);
	}
	
	public Set<Long> allTweets() {
		Set<Long> all = new HashSet<>();
		for (Set<Long> tweets : relations.values()) {
			all.addAll(tweets);
		}
		return all;
	}
	
	public Set<String> relatedHashtag() {
		return relations.keySet();
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (Entry<String, Set<Long>> status : relations.entrySet()) {
			builder.append(status.getKey()).append(": ").append(status.getValue()).append("\n");
		}
		return builder.toString();
	}
	
	public Set<String> similarHashtags(TweetRelation other) {
		Set<String> similarHashtags = new HashSet<>();
		Set<String> otherRelatedHashtags = other.relatedHashtag();
		for (String relatedHashtag : relatedHashtag()) {
			if (otherRelatedHashtags.contains(relatedHashtag)) {
				similarHashtags.add(relatedHashtag);
			}
		}
		return similarHashtags;
	}
	
	public double relation(TweetRelation other) {
		return similarHashtags(other).size() / (double) relations.size();
	}
	
}
