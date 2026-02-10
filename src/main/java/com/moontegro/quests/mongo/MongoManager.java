package com.moontegro.quests.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.moontegro.quests.Quests;
import lombok.Getter;
import org.bson.Document;

@Getter
public class MongoManager {

    private final Quests plugin = Quests.getInstance();
    private final MongoCollection<Document> profiles;

    public MongoManager() {
        MongoClient mongoClient = MongoClients.create(new ConnectionString(plugin.getConfiguration()
                .getConfiguration().getString("mongo-uri")));
        MongoDatabase mongoDatabase = mongoClient.getDatabase("quests");

        this.profiles = mongoDatabase.getCollection("profiles");
    }
}
