package ru.tikskit.service;

import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthorOldIdCleanUpServiceImpl implements AuthorOldIdCleanUpService {

    private final MongoOperations template;

    @Override
    public void cleanUp() {
        template.updateMulti(new Query(), new Update().unset("oldid"), ru.tikskit.domain.tar.Author.class);
    }
}
