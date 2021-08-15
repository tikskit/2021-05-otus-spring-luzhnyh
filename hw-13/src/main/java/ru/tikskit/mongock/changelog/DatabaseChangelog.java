package ru.tikskit.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import org.bson.Document;

import java.util.List;

import static com.mongodb.client.model.Indexes.*;

@ChangeLog(order = "001")
public class DatabaseChangelog {

    @ChangeSet(order = "001", id = "dropDb", author = "tikskit", runAlways = true)
    public void dropDB(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "createIndexes", author = "tikskit")
    public void createIndexes(MongoDatabase db) {
        // books
        MongoCollection<Document> booksCollection = db.getCollection("books");
        booksCollection.createIndex(compoundIndex(ascending("author"), ascending("name")),
                new IndexOptions().
                        name("ix_unique_author_book").
                        unique(true)
                );

        // genres
        MongoCollection<Document> genresCollection = db.getCollection("genres");
        genresCollection.createIndex(ascending("name"),
                new IndexOptions().
                        name("ix_genres_unique_names").
                        unique(true)
        );

        // authors
        MongoCollection<Document> authorsCollection = db.getCollection("authors");
        authorsCollection.createIndex(compoundIndex(ascending("surname"), ascending("name")),
                new IndexOptions().
                        name("ix_authors_unique_author").
                        unique(true)
        );
    }

    @ChangeSet(order = "003", id = "insertGenres", author = "tikskit")
    public void insertGenres(MongoDatabase db) {
        MongoCollection<Document> genresCollection = db.getCollection("genres");
        var sciFiDoc = new Document().append("name", "sci-fi");
        var fantasyDoc = new Document().append("name", "fantasy");
        genresCollection.insertMany(List.of(sciFiDoc, fantasyDoc));
    }

    @ChangeSet(order = "004", id = "insertAuthors", author = "tikskit")
    public void insertAuthors(MongoDatabase db) {
        MongoCollection<Document> authorsCollection = db.getCollection("authors");
        var lukyanenko = new Document().append("surname", "Лукьяненко").append("name", "Сергей");
        var vasilyev = new Document().append("surname", "Васильев").append("name", "Владимир");
        authorsCollection.insertMany(List.of(lukyanenko, vasilyev));
    }
}
