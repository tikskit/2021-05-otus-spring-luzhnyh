package ru.tikskit.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.MethodInvokingTaskletAdapter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.database.orm.JpaNativeQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import ru.tikskit.domain.tar.Author;
import ru.tikskit.domain.tar.Book;
import ru.tikskit.domain.tar.Comment;
import ru.tikskit.domain.tar.Genre;
import ru.tikskit.service.AuthorOldIdCleanUpService;
import ru.tikskit.service.CommentsConverter;
import ru.tikskit.service.GenreOldIdCleanUpService;

import javax.persistence.EntityManagerFactory;

@EnableBatchProcessing
@Configuration
@Slf4j
public class BatchConfig {
    public static final String JOB_NAME = "transfer data to mongo db";

    private static final int CHUNK_SIZE = 5;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private EntityManagerFactory entityManagerFactory;
    @Autowired
    private AuthorOldIdCleanUpService authorOldIdCleanUpService;
    @Autowired
    private GenreOldIdCleanUpService genreOldIdCleanUpService;
    @Autowired
    private CommentsConverter commentsConverter;

    @Bean
    public Job transferDataJob(Step transferAuthorsStep, Step transferGenresStep, Step transferBooksStep,
                               Step authorsCleanUpStep, Step genresCleanUpStep) {
        return jobBuilderFactory
                .get(JOB_NAME)
                .start(transferAuthorsStep)
                .next(transferGenresStep)
                .next(transferBooksStep)
                .next(authorsCleanUpStep)
                .next(genresCleanUpStep)
                .build();
    }

//         AUTHORS

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
    public ItemProcessor<ru.tikskit.domain.src.Author, ru.tikskit.domain.tar.Author> authorItemProcessor() {
        return item -> Author.builder().name(item.getName()).surname(item.getSurname()).oldId(item.getId()).build();
    }

    @Bean
    public Step transferGenresStep(ItemReader<ru.tikskit.domain.src.Genre> reader,
                                   ItemProcessor<ru.tikskit.domain.src.Genre, ru.tikskit.domain.tar.Genre> processor,
                                   ItemWriter<ru.tikskit.domain.tar.Genre> writer) {
        return stepBuilderFactory
                .get("transferGenresStep")
                .<ru.tikskit.domain.src.Genre, ru.tikskit.domain.tar.Genre>chunk(CHUNK_SIZE)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

//             GENRES

    @StepScope
    @Bean
    public JpaPagingItemReader<ru.tikskit.domain.src.Genre> genreReader() {
        JpaNativeQueryProvider<ru.tikskit.domain.src.Genre> queryProvider = new JpaNativeQueryProvider<>();
        queryProvider.setEntityClass(ru.tikskit.domain.src.Genre.class);
        queryProvider.setSqlQuery("SELECT id, name FROM genres ORDER BY id");
        return new JpaPagingItemReaderBuilder<ru.tikskit.domain.src.Genre>()
                .name("readGenre")
                .entityManagerFactory(entityManagerFactory)
                .queryProvider(queryProvider)
                .build();
    }

    @StepScope
    @Bean
    public MongoItemWriter<ru.tikskit.domain.tar.Genre> genreWriter(MongoOperations template) {
        return new MongoItemWriterBuilder<ru.tikskit.domain.tar.Genre>()
                .template(template)
                .collection("genres")
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<ru.tikskit.domain.src.Genre, ru.tikskit.domain.tar.Genre> genreItemProcessor() {
        return item -> Genre.builder().name(item.getName()).oldId(item.getId()).build();
    }


//             BOOKS
    @Bean
    public Step transferBooksStep(ItemReader<ru.tikskit.domain.src.Book> reader,
                                   ItemProcessor<ru.tikskit.domain.src.Book, ru.tikskit.domain.tar.Book> processor,
                                   ItemWriter<ru.tikskit.domain.tar.Book> writer) {
        return stepBuilderFactory
                .get("transferBooksStep")
                .<ru.tikskit.domain.src.Book, ru.tikskit.domain.tar.Book>chunk(CHUNK_SIZE)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @StepScope
    @Bean
    public JpaPagingItemReader<ru.tikskit.domain.src.Book> bookReader() {

        JpaNativeQueryProvider<ru.tikskit.domain.src.Book> queryProvider = new JpaNativeQueryProvider<>();
        queryProvider.setEntityClass(ru.tikskit.domain.src.Book.class);
        queryProvider.setSqlQuery("SELECT id, name, genre_id, author_id FROM books ORDER BY id");

        return new JpaPagingItemReaderBuilder<ru.tikskit.domain.src.Book>()
                .name("readBook")
                .entityManagerFactory(entityManagerFactory)
                .queryProvider(queryProvider)
                .build();
    }
    @StepScope
    @Bean
    public MongoItemWriter<ru.tikskit.domain.tar.Book> bookWriter(MongoOperations template) {
        return new MongoItemWriterBuilder<ru.tikskit.domain.tar.Book>()
                .template(template)
                .collection("books")
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<ru.tikskit.domain.src.Book, ru.tikskit.domain.tar.Book> bookItemProcessor(MongoOperations template) {
        return item -> {
            Author author = template.findOne(Query.query(Criteria.where("oldid").is(item.getAuthor().getId())), Author.class);
            Genre genre = template.findOne(Query.query(Criteria.where("oldid").is(item.getGenre().getId())), Genre.class);

            author.setOldId(null);
            genre.setOldId(null);

            return Book.builder()
                    .name(item.getName())
                    .author(author)
                    .genre(genre)
                    .comments(commentsConverter.convert(item.getComments()))
                    .build();
        };
    }

    @StepScope
    @Bean
    public ItemProcessor<ru.tikskit.domain.src.Book, ru.tikskit.domain.tar.Comment> commentItemProcessor(MongoOperations template) {
        return item -> new Comment();
    }


//              CLEAN UPS
    @Bean
    public Step authorsCleanUpStep() {
        return this.stepBuilderFactory.get("authorsCleanUpStep")
                .tasklet(authorsCleanUpTasklet())
                .build();
    }

    @Bean
    public MethodInvokingTaskletAdapter authorsCleanUpTasklet() {
        MethodInvokingTaskletAdapter adapter = new MethodInvokingTaskletAdapter();

        adapter.setTargetObject(authorOldIdCleanUpService);
        adapter.setTargetMethod("cleanUp");

        return adapter;
    }

    @Bean
    public Step genresCleanUpStep() {
        return this.stepBuilderFactory.get("genresCleanUpStep")
                .tasklet(genresCleanUpTasklet())
                .build();
    }

    @Bean
    public MethodInvokingTaskletAdapter genresCleanUpTasklet() {
        MethodInvokingTaskletAdapter adapter = new MethodInvokingTaskletAdapter();

        adapter.setTargetObject(genreOldIdCleanUpService);
        adapter.setTargetMethod("cleanUp");

        return adapter;
    }

}
