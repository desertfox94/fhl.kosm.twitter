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

    final RecursiveTweetCollector collector  = new RecursiveTweetCollector();

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        // create wordclouds
//        new ThreadPoolRunner(hashtagRepository.findAll()).execute();

//      load hashtags recursive

//        Thread.sleep(1000 * 60 * 15);


//        String[] selectedHashtags = {"Pizza", "spongebob", "naherosten", "Pferde", "Polizei", "Berlin", "Hamburg", "Kiel"};
//        TweetService service = new TweetService(hashtagRepository, tweetRepository);
//        boolean endless = true;
//        collector.loadHashtags(null, selectedHashtags);
//        new Thread(() -> {
//            collector.start(Arrays.asList(selectedHashtags), service, endless);
//        }).run();
    }
}
