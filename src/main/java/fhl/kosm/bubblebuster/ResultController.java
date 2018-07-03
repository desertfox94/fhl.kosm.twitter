package fhl.kosm.bubblebuster;

import fhl.kosm.bubblebuster.analyse.HashtagAnalyser;
import fhl.kosm.bubblebuster.model.AnalyzationResult;
import fhl.kosm.bubblebuster.model.TweetRelation;
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

    @GetMapping("/analyze")
    public String analyseHashtags(@RequestParam String selectedHashtags, Model model) {
        if (selectedHashtags == null || selectedHashtags.trim().isEmpty()) {
            return null;
        }
        System.out.println(selectedHashtags);
        HashtagAnalyser analyser = new HashtagAnalyser();
        List<String> relations = new LinkedList<>();
        AnalyzationResult result = analyser.relations(selectedHashtags.split(","));
        if (result != null) {
//            result.getPathOfHashtagWordclouds().forEach(r -> relations.add(String.format("%.4f %s -> %s : %s", r.calc(), r.from().toString(), r.to().toString(), r.intersection().toString())));
            model.addAttribute("result", result.getPathOfHashtagWordclouds());
        }
        return "result";
    }

}
