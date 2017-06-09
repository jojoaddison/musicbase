package org.cloudfoundry.samples.music.config.data;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.mongodb.MongoClient;

@Configuration
@Profile("mongodb-local")
public class MongoLocalConfig {

    @Bean
    public MongoDbFactory mongoDbFactory() {
        try {
            return new SimpleMongoDbFactory(new MongoClient(), "music");
        } catch (Exception e) {
            throw new RuntimeException("Error creating MongoDbFactory: " + e);
        }
    }

}
