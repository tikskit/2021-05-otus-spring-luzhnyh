package ru.tikskit.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentsConverterImpl implements CommentsConverter {
    @Override
    public List<ru.tikskit.domain.tar.Comment> convert(List<ru.tikskit.domain.src.Comment> src) {
        if (src == null) {
            return null;
        } else {
            return src.stream()
                    .map(comment -> new ru.tikskit.domain.tar.Comment(null, comment.getText()))
                    .collect(Collectors.toList());
        }
    }
}
