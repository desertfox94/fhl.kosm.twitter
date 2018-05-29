package fhl.kosm.twitter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import com.google.gson.Gson;

import twitter4j.Status;

public class TestData {

	public static List<TweetRelation> load(String name) {
		String json = FileUtil.readFileInCurrentDirectory(name);
		if (json == null) {
			return Collections.emptyList();
		}
		return new Gson().fromJson(json, TweetRelationConatiner.class).relations;
	}
	
	public static List<Status> loadTweets(String query) {
		// new Gson().fromJson(json, classOfT)
		return null;
	}

	public static void saveTweets(String name, List<Status> tweets) {

	}

	public static void saveRealtions(String name, List<TweetRelation> relations) {
		try {
			FileUtil.writeInCurrentDirectory(name, new Gson().toJson(new TweetRelationConatiner(relations)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A wrapper class for TweetRealtion list
	 */
	static class TweetRelationConatiner {
		List<TweetRelation> relations;

		public TweetRelationConatiner(List<TweetRelation> relations) {
			this.relations = relations;
		}
		
	}

}
