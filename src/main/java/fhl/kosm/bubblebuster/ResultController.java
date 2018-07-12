package fhl.kosm.bubblebuster;

import fhl.kosm.bubblebuster.analyse.HashtagAnalyser;
import fhl.kosm.bubblebuster.model.AnalyzedHashtag;
import fhl.kosm.bubblebuster.repositories.HashtagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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

        System.out.println(selectedHashtags);
        List<AnalyzedHashtag> result = new LinkedList<>();
        if (selectedHashtags != null && !selectedHashtags.trim().isEmpty()) {
            result = analyser.relations(selectedHashtags.replaceAll(" +", ",").split(","));
        }
        model.addAttribute("hashtags", result);
        return "result";
    }

}
