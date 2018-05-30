package fhl.kosm.twitter.collect;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import fhl.kosm.twitter.model.TweetRelation;
import twitter4j.HashtagEntity;
import twitter4j.Query;
import twitter4j.Query.ResultType;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class HashtagCollector extends QueryExecuter {

	private static final String LANG = "de";

	private static final String HASHTAG = "#";

	private int count = 25;

	private ResultType type = ResultType.popular;

	private boolean forceDataReload;

	public HashtagCollector() {
		this(false);
	}

	public HashtagCollector(boolean forceDataReload) {
		this.forceDataReload = forceDataReload;
	}

	public List<TweetRelation> loadHashtag(String... hashtags) throws TwitterException {
		return loadHashtags(hashtags);
	}

	public List<TweetRelation> loadHashtags(String[] hashtags) throws TwitterException {
		return load(hashtags);
	}

	private List<TweetRelation> load(String[] hashtags) {
		if (forceDataReload) {
			return loadFromTwitter(hashtags);
		}
		String dataName = dataName(hashtags);
		// try to reload data from local json file
		List<TweetRelation> relations = fromTestData(dataName);
		if (relations.isEmpty()) {
			// data not found
			relations = loadFromTwitter(hashtags);
		}
		return relations;
	}

	private String dataName(String[] hashtags) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < hashtags.length; i++) {
			builder.append(i != 0 ? '.' : "").append(hashtags[i]);
		}
		builder.append(".json");
		return builder.toString();
	}

	private List<TweetRelation> fromTestData(String name) {
		return Datastore.load(name);
	}

	private List<TweetRelation> loadFromTwitter(String[] hashtags) {
		List<TweetRelation> relations = new ArrayList<>(hashtags.length);
		for (String tag : hashtags) {
			try {
				relations.add(loadHastag(tag));
			} catch (TwitterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Datastore.saveRealtions(dataName(hashtags), relations);
		return relations;
	}

	public TweetRelation loadHastag(String tag) throws TwitterException {
		if (!tag.startsWith(HASHTAG)) {
			tag = HASHTAG + tag;
		}
		TweetRelation relation = new TweetRelation(tag);
		Query query = buildQuery(tag);
		StringBuilder builder = new StringBuilder();
		execute(query, tweet -> {
			tweet.getURLEntities();
			builder.append("@" + tweet.getUser().getScreenName()).append(":\n");
			builder.append(tweet.getText()).append("\n");
			HashtagEntity[] hashtags = tweet.getHashtagEntities();
			for (int i = 0; i < hashtags.length; i++) {
				if (i != 0) {
					builder.append(", ");
				}
				relation.add(hashtags[i].getText(), tweet.getId());
				builder.append('#').append(hashtags[i].getText());
			}
			builder.append("\n").append("-----------------------------------------------------------------")
					.append("\n");
		});
		System.out.println(builder.toString());
		System.out.println(relation);
		return relation;
	}

	private Query buildQuery(String tag) {
		Query query = new Query(tag).count(count);
		query.setLang(LANG);
		return query;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public ResultType getType() {
		return type;
	}

	public void setType(ResultType type) {
		this.type = type;
	}
}
