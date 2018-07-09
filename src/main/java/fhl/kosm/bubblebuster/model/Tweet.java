package fhl.kosm.bubblebuster.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

public class Tweet {

    @Id
    private long id;

    public Tweet(long id) {
        this.id = id;
    }

    private List<String> hashtags = new ArrayList<>();

    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setHashtags(List<String> hashtags) {
        this.hashtags = hashtags;
    }

    public List<String> getHashtags() {
        return hashtags;
    }

    public long getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return String.valueOf(id).hashCode();
    }

    @Override
    public String toString() {
        return id + " - " + url;
    }
}
