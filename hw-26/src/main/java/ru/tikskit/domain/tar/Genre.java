package ru.tikskit.domain.tar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "genres")
public class Genre {
    private String id;

    @Field(name = "oldid")
    private Long oldId;

    @Field(name = "name")
    private String name;
}
