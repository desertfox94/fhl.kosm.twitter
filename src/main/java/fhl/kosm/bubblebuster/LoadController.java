package fhl.kosm.bubblebuster;
import fhl.kosm.bubblebuster.analyse.HashtagAnalyser;
import fhl.kosm.bubblebuster.collect.RecursiveTweetCollector;
import fhl.kosm.bubblebuster.collect.TweetCollector;
import fhl.kosm.bubblebuster.model.AnalyzationResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import twitter4j.TwitterException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

@Controller
public class LoadController {

    @GetMapping("/load")
    public String loadHashtags(@RequestParam String selectedHashtags) {
        if (selectedHashtags == null || selectedHashtags.trim().isEmpty()) {
            return null;
        }

        new Thread(() -> {
            new RecursiveTweetCollector().loadHashtags(null, selectedHashtags.split(","));
        }).run();
        return "result";
    }
}
