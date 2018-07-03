package fhl.kosm.bubblebuster.repositories;

import fhl.kosm.bubblebuster.model.Tweet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TweetRepository extends MongoRepository<Tweet, Long> {

    Tweet findTweetById(Long id);

    List<Tweet> findTweetsByHashtagsContains(String hashtag);

    @Override
    <S extends Tweet> S save(S s);
}
