package fhl.kosm.bubblebuster;

import fhl.kosm.bubblebuster.analyse.HashtagAnalyser;
import fhl.kosm.bubblebuster.model.AnalyzationResult;
import fhl.kosm.bubblebuster.model.AnalyzedHashtag;
import fhl.kosm.bubblebuster.model.TweetRelation;
import fhl.kosm.bubblebuster.repositories.HashtagRepository;
import fhl.kosm.bubblebuster.repositories.TweetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedList;
import java.util.List;

@Controller
public class ResultController {

    private HashtagAnalyser analyser;

    @Autowired
    private HashtagRepository repository;

    @GetMapping("/analyze")
    public String analyseHashtags(@RequestParam String selectedHashtags, Model model) {
        if (analyser == null) {
            analyser = new HashtagAnalyser(repository);
        }
        if (selectedHashtags == null || selectedHashtags.trim().isEmpty()) {
            return null;
        }
        System.out.println(selectedHashtags);
        List<String> relations = new LinkedList<>();
        List<AnalyzedHashtag> result = analyser.relations(selectedHashtags.split(","));
        model.addAttribute("hashtags", result);
        return "result";
    }

}
