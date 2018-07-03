package fhl.kosm.bubblebuster.collect;

import fhl.kosm.bubblebuster.TweetService;
import fhl.kosm.bubblebuster.model.Tweet;
import twitter4j.Query;
import twitter4j.Query.ResultType;
import twitter4j.TwitterException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.*;

public class TweetCollector extends QueryExecuter {

    private static final String LANG = "de";

    private static final String HASHTAG = "#";

    private int count = 50;

    TweetService tweetService = new TweetService();

    private BufferedWriter writer;

    private ResultType type = ResultType.popular;

    public List<Tweet> loadHashtags(String[] hashtags) {
        return loadHashtags(System.out, hashtags);
    }

    public List<Tweet> loadHashtags(OutputStream stream, String[] hashtags) {
        if (stream != null) {
            writer = new BufferedWriter(new OutputStreamWriter(stream));
        }
        loadFromTwitter(Arrays.asList(hashtags));
        return tweetService.tweetsOf(hashtags);
    }

    protected void loadFromTwitter(List<String> hashtags) {
        for (String tag : hashtags) {
            try {
                loadByHashtag(tag);
            } catch (TwitterException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    protected void loadByHashtag(String tag) throws TwitterException {
        if (!tag.startsWith(HASHTAG)) {
            tag = HASHTAG + tag;
        }
        Query query = buildQuery(tag);
        execute(query, exec());
    }

    protected TweetProcessor exec() {
        return status -> {
            Tweet tweet = tweetService.update(status);
            write(tweet);
        };
    }

    protected void write(Tweet tweet) {
        if (writer == null)
            return;
        try {
            writer.write(tweet.getUrl());
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
