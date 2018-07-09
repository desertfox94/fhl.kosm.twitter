package fhl.kosm.bubblebuster.collect;

import fhl.kosm.bubblebuster.DataCleanUpService;
import fhl.kosm.bubblebuster.TweetService;
import fhl.kosm.bubblebuster.model.Hashtag;
import fhl.kosm.bubblebuster.model.Tweet;
import twitter4j.TwitterException;

import java.util.*;

public class RecursiveTweetCollector extends TweetCollector {

    Set<String> remaining = new HashSet<>();

    public RecursiveTweetCollector(TweetService service) {
        super(service);
    }

    @Override
    protected void loadFromTwitter(List<String> hashtags) {
        System.out.println("load : " + hashtags.toString());
        super.loadFromTwitter(hashtags);
        if (!remaining.isEmpty()) {
            loadFromTwitter(new ArrayList<>(remaining));
        }
    }

    @Override
    protected void loadByHashtag(String tag) throws TwitterException {
        super.loadByHashtag(tag);
        remaining.remove(tag);
        System.out.println("loaded : " + tag);
    }

    private boolean notMarkedOrLoaded(String tag) {
        return tweetService.get(tag) == null && !remaining.contains(tag);
    }

    @Override
    protected TweetProcessor exec() {
        return status -> {
            Tweet tweet = tweetService.update(status);
            tweet.getHashtags().forEach(tag -> {
                if (notMarkedOrLoaded(tag)) {
                    System.out.println("mark: " + tag);
                    remaining.add(tag);
                    if (!tag.toLowerCase().equals(tag)) {
                        remaining.add(tag.toLowerCase());
                    }
                }
            });
            write(tweet);
        };
    }

}
