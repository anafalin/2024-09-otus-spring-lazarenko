package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ShellComponent
@RequiredArgsConstructor
public class AppCommand {
    private static final String MIGRATION_LIBRARY_JOB_NAME = "migrationJpaToMongoJob";

    private final JobLauncher jobLauncher;

    private final JobExplorer jobExplorer;

    private final Job migrationLibraryJpaToMongo;

    @SuppressWarnings("unused")
    @ShellMethod(value = "startMigrationJobWithJobLauncher", key = "sm")
    public void startMigrationJobWithJobLauncher() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("time", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .toJobParameters();

        JobExecution execution = jobLauncher.run(migrationLibraryJpaToMongo, jobParameters);
    }

    @SuppressWarnings("unused")
    @ShellMethod(value = "showInfo", key = "i")
    public void showInfo() {
        System.out.println(jobExplorer.getJobNames());
        System.out.println(jobExplorer.getLastJobInstance(MIGRATION_LIBRARY_JOB_NAME));
    }
}