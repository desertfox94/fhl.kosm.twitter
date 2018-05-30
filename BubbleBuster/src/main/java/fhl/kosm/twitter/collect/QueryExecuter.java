package fhl.kosm.twitter.collect;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class QueryExecuter {

	private static final int FIFTEEN_MINUTES = 1000 * 60 * 15;

	private Twitter twitter = TwitterFactory.getSingleton();

	public void execute(Query query, TweetProcessor processor) {
		QueryResult result;
		do {
			result = execute(query);
			for (Status tweet : result.getTweets()) {
				processor.process(tweet);
			}
		} while ((query = result.nextQuery()) != null);
	}
	
	private QueryResult execute(Query q) {
		QueryResult result = null;
		do {
			try {
				result = twitter.search(q);
			} catch (TwitterException e) {
				if (e.exceededRateLimitation()) {
					handleRateLimitation();
				} else {
					throw new RuntimeException(e);
				}
			}
		} while (result == null);
		return result;
	}

	private void handleRateLimitation() {
		System.err.println(String.format("Anfrage limit erreicht! Anfrage wird in 15 Mintuten erneut gesendet."));
		wait(FIFTEEN_MINUTES);
	}

	private void wait(int time) {
		try {
			Thread.sleep(FIFTEEN_MINUTES);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
