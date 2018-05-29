package fhl.kosm.twitter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import twitter4j.TwitterException;

public class HashtagAnalyser {

	private HashtagCollector collector =new HashtagCollector();
	
	public void relations(String... hashtags) throws TwitterException {
		List<TweetRelation> relations = collector.loadHashtags(hashtags);
		List<String> results = new ArrayList<>(relations.size() * relations.size());
		for (TweetRelation r1 : relations) {
			for (TweetRelation r2 : relations) {
				if (r1 == r2)
					continue;
				Set<String> similarHashtags = r1.similarHashtags(r2);
				double rel = r1.relation(r2);
				results.add(String.format("%.4f", rel) + " " + r1.getHashtag() + " - " + r2.getHashtag() + ": "+ similarHashtags);
			}
		}
		Collections.sort(results);
		Collections.reverse(results); // ascending
		results.forEach(System.out::println);
	}
	
}
