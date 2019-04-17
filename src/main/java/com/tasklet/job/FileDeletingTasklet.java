package com.tasklet.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Slf4j
public class FileDeletingTasklet implements Tasklet {

    private Resource directory;

    public FileDeletingTasklet(Resource directory) {
        this.directory = directory;
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        try (Stream<Path> walk =
                     Files.walk(Paths.get(directory.getFile().getPath()))){
            walk.filter(Files::isRegularFile).map(Path::toFile)
                    .forEach(File::delete);


        } catch (IOException e){

            log.info("error deleting files", e);
            throw new UnexpectedJobExecutionException("unable to delete files", e);
        }
        return RepeatStatus.FINISHED;
    }
}
