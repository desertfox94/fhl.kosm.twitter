package fhl.kosm.bubblebuster.analyse;

import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.PixelBoundryBackground;
import com.kennycason.kumo.font.scale.LinearFontScalar;
import com.kennycason.kumo.palette.ColorPalette;
import fhl.kosm.bubblebuster.FileUtil;
import fhl.kosm.bubblebuster.TweetService;
import fhl.kosm.bubblebuster.model.*;
import fhl.kosm.bubblebuster.repositories.HashtagRepository;
import fhl.kosm.bubblebuster.repositories.TweetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import twitter4j.OEmbed;
import twitter4j.OEmbedRequest;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class HashtagAnalyser {

    private HashtagRepository repository;

    private WordcloudCreator wordcloud = new WordcloudCreator();

    public HashtagAnalyser(HashtagRepository repository) {
        this.repository = repository;
    }

    private List<String> trim(String[] tags) {
        List<String> trimmed = new ArrayList<>(tags.length);
        for (String tag: tags) {
            trimmed.add(tag.trim());
        }
        return trimmed;
    }

    public List<AnalyzedHashtag> relations(String... tags) {
        Iterable<Hashtag> hashtags = repository.findAllById(trim(tags));
        List<AnalyzedHashtag> analyzedHashtags = new ArrayList<>(tags.length);
        AnalyzedHashtag analyzedHashtag;
        for (Hashtag h1 : hashtags) {
            analyzedHashtag = new AnalyzedHashtag(h1);
            analyzedHashtags.add(analyzedHashtag);
            analyzedHashtag.setWordcloud(wordcloud.asBase64(h1));
            for (Hashtag h2 : hashtags) {
                if (h1 == h2)
                    continue;
                analyzedHashtag.addRelation(h2);
            }
            analyzedHashtag.setEmbeddedTweets(loadTweetsOfMostRelatedHashtag(analyzedHashtag));
        }
        return analyzedHashtags;
    }

    private List<String> loadTweetsOfMostRelatedHashtag(AnalyzedHashtag analyzedHashtag) {
        return loadTweetsOfMostRelatedHashtag(analyzedHashtag, 2);
    }

    private List<String> loadTweetsOfMostRelatedHashtag(AnalyzedHashtag analyzedHashtag, int limit) {
        Set<Tweet> tweets = analyzedHashtag.tweetsOfMostRelatedHashtag();
        List<String> result = new ArrayList<>(limit);
        int i = 0;
        for (Tweet tweet : tweets) {
            if (i++ == limit)
                break;
            try {
                OEmbedRequest oEmbedRequest = new OEmbedRequest(tweet.getId(), tweet.getUrl());
                OEmbed embed = TwitterFactory.getSingleton().getOEmbed(oEmbedRequest);
                result.add(embed.getHtml());
            } catch (TwitterException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

}
