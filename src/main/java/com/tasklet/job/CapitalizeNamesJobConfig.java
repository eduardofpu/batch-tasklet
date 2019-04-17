package com.tasklet.job;

import com.tasklet.model.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.builder.MultiResourceItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;

@Slf4j
@Configuration
public class CapitalizeNamesJobConfig {

    @Bean
    public Job capitalizeNamesJob(JobBuilderFactory jobBuilders,
                                  StepBuilderFactory stepBuilders) {
        return jobBuilders.get("capitalizeNamesJob")
                .start(capitalizeNamesStep(stepBuilders))
                .next(deleteFilesStep(stepBuilders)).build();
    }

    @Bean
    public Step capitalizeNamesStep(StepBuilderFactory stepBuilders) {
        //Function<? super Person, ? extends Person> processor = (Function<? super Person, ? extends Person>) processor();
        return stepBuilders.get("capitalizeNamesStep")
                .<Person, Person> chunk(10).reader(multiItemReader())
                .processor(processor()).writer(itemWriter()).build();
    }

    @Bean
    public Step deleteFilesStep(StepBuilderFactory stepBuilders) {
        return stepBuilders.get("deleteFilesStep")
                .tasklet(fileDeletingTasklet()).build();
    }

    @Bean
    public MultiResourceItemReader<Person> multiItemReader() {
        ResourcePatternResolver patternResolver =
                new PathMatchingResourcePatternResolver();
        Resource[] resources = null;
        try {
            resources = patternResolver
                    .getResources("file:target/test-inputs/*.csv");
        } catch (IOException e) {
            log.error("error reading files", e);
        }

        return new MultiResourceItemReaderBuilder<Person>()
                .name("multiPersonItemReader").delegate(itemReader())
                .resources(resources).setStrict(true).build();
    }

    @Bean
    public FlatFileItemReader<Person> itemReader() {
        return new FlatFileItemReaderBuilder<Person>()
                .name("personItemReader").delimited()
                .names(new String[] {"firstName", "lastName"})
                .targetType(Person.class).build();
    }

    @Bean
    public PersonItemProcessor processor() {
        return new PersonItemProcessor();
    }

    @Bean
    public FlatFileItemWriter<Person> itemWriter() {
        return new FlatFileItemWriterBuilder<Person>()
                .name("personItemWriter")
                .resource(new FileSystemResource(
                        "target/test-outputs/persons.txt"))
                .delimited().delimiter(", ")
                .names(new String[] {"firstName", "lastName"}).build();
    }

    @Bean
    public FileDeletingTasklet fileDeletingTasklet() {
        return new FileDeletingTasklet(
                new FileSystemResource("target/test-inputs"));
    }
}