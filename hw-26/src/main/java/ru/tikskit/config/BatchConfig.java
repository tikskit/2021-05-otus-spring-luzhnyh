package ru.tikskit.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.database.orm.JpaNativeQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.lang.NonNull;
import ru.tikskit.domain.tar.Author;

import javax.persistence.EntityManagerFactory;
import java.util.List;

@EnableBatchProcessing
@Configuration
@Slf4j
public class BatchConfig {

    private static final int CHUNK_SIZE = 5;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Bean
    public Job transferDataJob(Step transferAuthorsStep) {
        return jobBuilderFactory
                .get("transfer data")
                .start(transferAuthorsStep)
                .build();
    }

    @Bean
    public Step transferAuthorsStep(ItemReader<ru.tikskit.domain.src.Author> reader,
                                    ItemProcessor<ru.tikskit.domain.src.Author, ru.tikskit.domain.tar.Author> processor,
                                    MongoItemWriter<ru.tikskit.domain.tar.Author> writer) {
        return stepBuilderFactory
                .get("transferAuthorsStep")
                .<ru.tikskit.domain.src.Author, ru.tikskit.domain.tar.Author>chunk(CHUNK_SIZE)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .listener(new ItemReadListener<ru.tikskit.domain.src.Author>() {
                    public void beforeRead() {
                        log.info("Начало чтения");
                    }

                    public void afterRead(@NonNull ru.tikskit.domain.src.Author o) {
                        log.info("Конец чтения: " + o);
                    }

                    public void onReadError(@NonNull Exception e) {
                        log.info("Ошибка чтения");
                    }
                })
                .listener(new ItemWriteListener<>() {
                    public void beforeWrite(@NonNull List list) {
                        log.info("Начало записи");
                    }

                    public void afterWrite(@NonNull List list) {
                        log.info("Конец записи");
                    }

                    public void onWriteError(@NonNull Exception e, @NonNull List list) {
                        log.info("Ошибка записи");
                    }
                })
                .listener(new ItemProcessListener<ru.tikskit.domain.src.Author, ru.tikskit.domain.tar.Author>() {
                    public void beforeProcess(ru.tikskit.domain.src.Author o) {
                        log.info("Начало обработки: " + o);
                    }

                    public void afterProcess(@NonNull ru.tikskit.domain.src.Author o,  ru.tikskit.domain.tar.Author o2) {
                        log.info(String.format("Конец обработки %s -> %s", o, o2));
                    }

                    public void onProcessError(@NonNull ru.tikskit.domain.src.Author o, @NonNull Exception e) {
                        log.info("Ошибка обработки");
                    }
                })
                .listener(new ChunkListener() {
                    public void beforeChunk(@NonNull ChunkContext chunkContext) {
                        log.info("Начало пачки");
                    }

                    public void afterChunk(@NonNull ChunkContext chunkContext) {
                        log.info("Конец пачки");
                    }

                    public void afterChunkError(@NonNull ChunkContext chunkContext) {
                        log.info("Ошибка пачки");
                    }
                })
                .build();
    }

    @StepScope
    @Bean
    public JpaPagingItemReader<ru.tikskit.domain.src.Author> authorReader() {
        JpaNativeQueryProvider<ru.tikskit.domain.src.Author> queryProvider = new JpaNativeQueryProvider<>();
        queryProvider.setEntityClass(ru.tikskit.domain.src.Author.class);
        queryProvider.setSqlQuery("SELECT id, surname, name FROM authors ORDER BY id");
        return new JpaPagingItemReaderBuilder<ru.tikskit.domain.src.Author>()
                .name("readAuthor")
                .entityManagerFactory(entityManagerFactory)
                .queryProvider(queryProvider)
                .build();
    }

    @StepScope
    @Bean
    public MongoItemWriter<ru.tikskit.domain.tar.Author> authorWriter(MongoOperations template) {
        return new MongoItemWriterBuilder<ru.tikskit.domain.tar.Author>()
                .template(template)
                .collection("authors")
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<ru.tikskit.domain.src.Author, ru.tikskit.domain.tar.Author> itemProcessor() {
        return item -> Author.builder().name(item.getName()).surname(item.getSurname()).build();
    }


}
