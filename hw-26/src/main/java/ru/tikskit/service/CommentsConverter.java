package ru.tikskit.service;

import java.util.List;

public interface CommentsConverter {
    List<ru.tikskit.domain.tar.Comment> convert(List<ru.tikskit.domain.src.Comment> src);
}
