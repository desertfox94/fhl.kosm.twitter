package fhl.kosm.bubblebuster;

import fhl.kosm.bubblebuster.collect.RecursiveTweetCollector;
import fhl.kosm.bubblebuster.collect.ThreadPoolRunner;
import fhl.kosm.bubblebuster.repositories.HashtagRepository;
import fhl.kosm.bubblebuster.repositories.TweetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import twitter4j.*;
import twitter4j.api.TrendsResources;

import java.lang.reflect.Array;
import java.util.*;


@SpringBootApplication
public class App implements CommandLineRunner {

    @Autowired
    HashtagRepository hashtagRepository;

    @Autowired
    TweetRepository tweetRepository;

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        // create wordclouds
//        new ThreadPoolRunner(hashtagRepository.findAll()).execute();

//      load hashtags recursive

//        Thread.sleep(1000 * 60 * 15);

//        Twitter twitter = TwitterFactory.getSingleton();
//        TrendsResources trends = TwitterFactory.getSingleton().trends();
//        List<Location> locations = trends.getClosestTrends(new GeoLocation(53.550556, 9.993333));
//        ArrayList trendingHashtags = new ArrayList();
//        trendingHashtags.add("Filterblase");
//        for(Trend trend: twitter.getPlaceTrends(locations.get(0).getWoeid()).getTrends()) {
//            trendingHashtags.add(trend.getName());
//        }

//        String[] hashtags = {"Filterbubble", "Google", "Facebook", "Jodel", "Bubble", "Filter", "Blase", "Filterblase", "#Tatort", "#RUSCRO", "#nurderHSV", "#FACEITMinor", "#Seebrücke", "Abonnenten", "New York Times", "Kroatien", "Schönen Sonntag", "Jungen", "Lewis", "Kilometer", "Croatia", "Putin", "Hamilton", "Tanja Maljartschuk", "#ESLOne", "#Zverev", "#schwiegertochtergesucht", "#Grindel", "#TSGarmisch", "#Steinmeier", "#SGEagles", "#SWEENG", "#BritishGP", "#Thailand", "#ColognePride", "#dopa", "#Fernsehgarten", "#TDF2018", "#PokemonGOCommunityDay", "#NoPolGNRW", "#ENGSWE", "#IMSA", "#SilverstoneGP", "#PremierTour", "#Presseclub", "#PorscheMobil1Supercup", "#ironman", "#RiftRivals2018", "#hahohe", "#ThreeLions", "#Civilfleet", "#tddl", "#Japan", "#SundayFunday", "#FCAinMals", "#Go4SC2", "#CFCSGD", "#Blankenfelde", "Pizza", "spongebob", "naherosten", "Pferde", "Polizei", "Berlin", "Hamburg", "Kiel"};
//        List<String> hashtags = Arrays.asList("twitter", "Filterbubble", "Bubble", "Filter", "Blase", "Filterblase", "FakeNews", "fakenews");
//        TweetService service = new TweetService(hashtagRepository, tweetRepository);
//        final RecursiveTweetCollector collector  = new RecursiveTweetCollector(service);
//        boolean endless = true;
//        new Thread(() -> {
//            collector.loadHashtags(null, hashtags);
//        }).run();
    }
}
