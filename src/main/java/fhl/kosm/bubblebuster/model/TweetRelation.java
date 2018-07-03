package fhl.kosm.bubblebuster.model;

import java.util.HashSet;
import java.util.Set;

public class TweetRelation {

	private Hashtag from;

	private Hashtag to;

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
	
	public double calc() {
		return intersection().size() / (double) from.relationsCount();
	}

	public Hashtag from() {
		return from;
	}

	public Hashtag to() {
		return to;
	}
	
}
