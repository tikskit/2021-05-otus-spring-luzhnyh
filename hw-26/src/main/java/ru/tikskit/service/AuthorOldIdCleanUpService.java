package ru.tikskit.service;

import org.springframework.data.mongodb.core.MongoOperations;

public interface AuthorOldIdCleanUpService {
    void cleanUp();
}
