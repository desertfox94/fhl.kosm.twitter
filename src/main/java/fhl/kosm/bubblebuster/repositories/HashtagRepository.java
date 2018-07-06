package fhl.kosm.bubblebuster.repositories;

import fhl.kosm.bubblebuster.model.Hashtag;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HashtagRepository extends MongoRepository<Hashtag, String> {
    @Override
    <S extends Hashtag> S save(S s);
}