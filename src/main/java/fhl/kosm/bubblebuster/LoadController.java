package fhl.kosm.bubblebuster;
import fhl.kosm.bubblebuster.collect.RecursiveTweetCollector;
import fhl.kosm.bubblebuster.repositories.HashtagRepository;
import fhl.kosm.bubblebuster.repositories.TweetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoadController {

    @Autowired
    HashtagRepository hashtagRepository;

    @Autowired
    TweetRepository tweetRepository;

    @GetMapping("/load")
    public String loadHashtags(@RequestParam String selectedHashtags) {
        if (true) {
            return null;
        }

        if (selectedHashtags == null || selectedHashtags.trim().isEmpty()) {
            return null;
        }

//        new Thread(() -> {
//            new TweetCollector().loadHashtags(selectedHashtags.split(","));
//        }).run();

        new Thread(() -> {
            new RecursiveTweetCollector(new TweetService(hashtagRepository, tweetRepository)).loadHashtags(null, selectedHashtags.split(","));
        }).run();
        return "result";
    }
}
