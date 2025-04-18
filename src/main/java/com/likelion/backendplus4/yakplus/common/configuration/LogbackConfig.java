package com.likelion.backendplus4.yakplus.common.configuration;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import ch.qos.logback.core.util.FileSize;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class LogbackConfig {
    private static final String LOG_DIRECTORY = "logs";
    private static final String LOG_FILE_NAME = "like-lion.log";
    private static final String LOG_PATTERN = "%d{yyyy-MM-dd HH:mm:ss} %-5level %logger{36} - %msg%n";
    private static final int MAX_HISTORY = 30;
    private static final String TOTAL_SIZE_CAP = "1GB";

    @PostConstruct
    public void configure() {
        LoggerContext context = initializeLoggerContext();
        createLogDirectory();

        ConsoleAppender<ILoggingEvent> consoleAppender = createConsoleAppender(context);
        FileAppender<ILoggingEvent> fileAppender = createFileAppender(context);

        configureRootLogger(context, consoleAppender, fileAppender);
    }

    private LoggerContext initializeLoggerContext() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        context.reset();
        return context;
    }

    private void createLogDirectory() {
        Path logPath = Paths.get(LOG_DIRECTORY);
        try {
            if (!Files.exists(logPath)) {
                Files.createDirectories(logPath);
            }
        } catch (Exception e) {
            throw new RuntimeException("로그 디렉토리 생성 실패", e);
        }
    }

    private ConsoleAppender<ILoggingEvent> createConsoleAppender(LoggerContext context) {
        ConsoleAppender<ILoggingEvent> appender = new ConsoleAppender<>();
        appender.setContext(context);
        appender.setEncoder(createEncoder(context));
        appender.start();
        return appender;
    }

    private FileAppender<ILoggingEvent> createFileAppender(LoggerContext context) {
        FileAppender<ILoggingEvent> appender = new FileAppender<>();
        appender.setContext(context);
        appender.setFile(LOG_DIRECTORY + "/" + LOG_FILE_NAME);
        appender.setAppend(true);
        appender.setEncoder(createEncoder(context));

        TimeBasedRollingPolicy<ILoggingEvent> rollingPolicy = createRollingPolicy(context, appender);
        rollingPolicy.start();

        appender.start();
        return appender;
    }

    private PatternLayoutEncoder createEncoder(LoggerContext context) {
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(context);
        encoder.setPattern(LOG_PATTERN);
        encoder.start();
        return encoder;
    }

    private TimeBasedRollingPolicy<ILoggingEvent> createRollingPolicy(LoggerContext context, FileAppender<ILoggingEvent> parent) {
        TimeBasedRollingPolicy<ILoggingEvent> policy = new TimeBasedRollingPolicy<>();
        policy.setContext(context);
        policy.setParent(parent);
        policy.setFileNamePattern(LOG_DIRECTORY + "/" + LOG_FILE_NAME.replace(".log", ".%d{yyyy-MM-dd}.log"));
        policy.setMaxHistory(MAX_HISTORY);
        policy.setTotalSizeCap(FileSize.valueOf(TOTAL_SIZE_CAP));
        return policy;
    }

    private void configureRootLogger(LoggerContext context, ConsoleAppender<ILoggingEvent> consoleAppender, FileAppender<ILoggingEvent> fileAppender) {
        Logger logger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        if (logger instanceof ch.qos.logback.classic.Logger) {
            ch.qos.logback.classic.Logger rootLogger = (ch.qos.logback.classic.Logger) logger;
            rootLogger.setLevel(Level.INFO);
            rootLogger.addAppender(consoleAppender);
            rootLogger.addAppender(fileAppender);
        }
    }
}