package fhl.kosm.bubblebuster.model;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class AnalyzationResult {

    private List<TweetRelation> relations = new LinkedList<>();

    private List<String> pathOfHashtagWordclouds = new LinkedList<>();

    public void addRelation(Hashtag from, Hashtag to){
        relations.add(new TweetRelation(from, to));
    }

    public void addPathOfHashtagWordcloud(String relative){
        pathOfHashtagWordclouds.add(relative);
    }

    public List<String> getPathOfHashtagWordclouds() {
        return pathOfHashtagWordclouds;
    }

    public List<TweetRelation> getRelations() {
        return relations;
    }
}
