package fhl.kosm.bubblebuster;

import com.mongodb.MongoClient;
import fhl.kosm.bubblebuster.repositories.TweetRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoClientFactoryBean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import javax.persistence.EntityManagerFactory;

@Configuration
public class AppConfig {

    /*
     * Use the standard Mongo driver API to create a com.mongodb.MongoClient instance.
     */
    public @Bean
    MongoClient mongoClient() {
        return new MongoClient("localhost");
    }

}
