package SplitStats;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import net.dv8tion.jda.api.entities.Guild;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MongoManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(MongoManager.class);
    private static MongoManager instance;
    private final MongoDatabase db;

    private final List<Document> guildSettings;

    public static MongoManager getInstance() {
        if (instance == null) {
            instance = new MongoManager();
        }

        return instance;
    }

    private MongoManager() {
        MongoClient mongo = new MongoClient("127.0.0.1", 27017);
        db = mongo.getDatabase("SplitStats");
        LOGGER.info("Initialized MongoDB");

        guildSettings = new ArrayList<>();
    }

    //region Guild Settings
    public void deleteGuildSettings(Guild guild) {
        MongoCollection<Document> settingsCollection = db.getCollection("Settings");
        settingsCollection.deleteOne(Filters.eq("guild_id", guild.getId()));
    }

    public Document writeDefaultGuildSettings(Guild guild) {
        MongoCollection<Document> settingsCollection = db.getCollection("Settings");
        Document guildDoc = getDefaultGuildSettings(guild);
        settingsCollection.insertOne(guildDoc);

        removeGuildSettingsFromList(guild);
        guildSettings.add(guildDoc);

        return guildDoc;
    }

    public Document findOrCreateGuildSettings(Guild guild) {
        // If settings are already loaded in the list, return those
        for (Document doc : new ArrayList<>(guildSettings)) {
            if (doc.get("guild_id").equals(guild.getId())) {
                return doc;
            }
        }

        // If not in the list, try loading the settings from MongoDB
        MongoCollection<Document> settingsCollection = db.getCollection("Settings");
        Document settingsDoc = settingsCollection.find(Filters.eq("guild_id", guild.getId())).first();
        if (settingsDoc != null) {
            guildSettings.add(settingsDoc);
            return settingsDoc;
        }

        // If settings are not in MongoDB, write default settings
        return writeDefaultGuildSettings(guild);
    }

    public void updateGuildSettings(Document updateDoc, Guild guild) {
        MongoCollection<Document> settingsCollection = db.getCollection("Settings");
        removeGuildSettingsFromList(guild);

        // Update document if it exists, if it doesn't then write default settings and add it to the list
        settingsCollection.findOneAndUpdate(Filters.eq("guild_id", guild.getId()), new Document("$set", updateDoc));
    }

    private void removeGuildSettingsFromList(Guild guild) {
        guildSettings.removeIf(doc -> doc.get("guild_id").equals(guild.getId()));
    }

    private Document getDefaultGuildSettings(Guild guild) {
        return new Document("guild_id", guild.getId()).append("prefix", "!");
    }
    //endregion
}
