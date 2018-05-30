package fhl.kosm.twitter.collect;

import twitter4j.Status;

public interface TweetProcessor {

	void process(Status tweet);
	
}
