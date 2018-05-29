package fhl.kosm.twitter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;

import twitter4j.HashtagEntity;
import twitter4j.Query;
import twitter4j.Query.ResultType;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class App {

	public static void main(String[] args) throws Exception {
		Twitter twitter = new TwitterFactory().getInstance();
		String tag = "elonmusk";
		try {
			Query query = new Query(tag).count(50);
			QueryResult result;
			StringBuilder builder = new StringBuilder();
			List<Status> allTweets = new LinkedList<>();
			do {
				result = twitter.search(query);
				List<Status> tweets = result.getTweets();
				allTweets.addAll(tweets);
				for (Status tweet : tweets) {
					builder.append("@" + tweet.getUser().getScreenName()).append(":\n");
					builder.append(tweet.getText()).append("\n");
					HashtagEntity[] hashtags = tweet.getHashtagEntities();
					for (int i = 0; i < hashtags.length; i++) {
						if (i != 0) {
							builder.append(", ");
						}
						builder.append('#').append(hashtags[i].getText());
					}
					builder.append("\n").append("-----------------------------------------------------------------")
							.append("\n");
				}
			} while ((query = result.nextQuery()) != null);
			System.out.println(builder.toString());
			writeSearchToFile(tag, allTweets);
		} catch (TwitterException te) {
			te.printStackTrace();
			System.out.println("Failed to search tweets: " + te.getMessage());
			System.exit(-1);
		}
	}

	private static void writeSearchToFile(String tag, List<Status> allTweets) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(getFileForDataBackup(tag)));
		String json = new Gson().toJson(allTweets);
		writer.write(json);
		writer.flush();
		writer.close();
	}

	private static File getFileForDataBackup(String query) {
		try {
			String path = new File(".").getCanonicalPath();
			File file = new File(path + File.separator + query + ".search.json");
			int i = 1;
			while (file.exists()) {
				file = new File(path + File.separator + query + "(" + i++ + ").search.json");
			}
			file.createNewFile();
			return file;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
