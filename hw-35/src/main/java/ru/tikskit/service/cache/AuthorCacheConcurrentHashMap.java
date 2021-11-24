package ru.tikskit.service.cache;

import org.springframework.stereotype.Component;
import ru.tikskit.domain.Author;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AuthorCacheConcurrentHashMap implements AuthorCache {
    private final Map<Long, Author> data = new ConcurrentHashMap<>();

    @Override
    public Author get(long id) {
        return data.get(id);
    }

    @Override
    public Author put(long id, Author author) {
        return data.put(id, author);
    }

    @Override
    public List<Author> getAll() {
        return new ArrayList<>(data.values());
    }
}
