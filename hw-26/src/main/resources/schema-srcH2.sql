CREATE TABLE authors(
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        surname VARCHAR(100) NOT NULL,
                        name VARCHAR(100) NOT NULL,
                        CONSTRAINT uc_author UNIQUE (surname, name)
);

CREATE TABLE genres(
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(100) NOT NULL UNIQUE
);


CREATE TABLE books(
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      name VARCHAR(100) NOT NULL,
                      genre_id BIGINT NOT NULL CONSTRAINT fk_book_genre REFERENCES genres(id) ON DELETE CASCADE ON UPDATE RESTRICT,
                      author_id BIGINT NOT NULL CONSTRAINT fk_book_author REFERENCES authors(id) ON DELETE CASCADE ON UPDATE RESTRICT,
                      CONSTRAINT uc_unique_book_name UNIQUE(author_id, name)
);


CREATE TABLE comments(
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         text VARCHAR(MAX) NOT NULL,
                         book_id BIGINT NOT NULL CONSTRAINT fk_comment_book REFERENCES books(id) ON DELETE CASCADE  ON UPDATE CASCADE
);