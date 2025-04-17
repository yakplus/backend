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
import java.nio.file.Paths;
@Configuration
public class LogbackConfig {

    @PostConstruct
    public void configure() {
        // LoggerContext 초기화
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        context.reset();  // 기존 설정 초기화

        // 로그 파일 디렉토리 확인 및 생성
        String logDirectory = "logs";
        if (!Files.exists(Paths.get(logDirectory))) {
            try {
                Files.createDirectories(Paths.get(logDirectory));  // 디렉토리 생성
            } catch (Exception e) {
                e.printStackTrace();  // 디렉토리 생성 실패 시 예외 처리
            }
        }

        // 콘솔 Appender 설정
        PatternLayoutEncoder consoleEncoder = createEncoder(context);
        ConsoleAppender<ILoggingEvent> consoleAppender = new ConsoleAppender<>();
        consoleAppender.setContext(context);
        consoleAppender.setEncoder(consoleEncoder);
        consoleAppender.start();

        // 파일 Appender 설정
        PatternLayoutEncoder fileEncoder = createEncoder(context);
        FileAppender<ILoggingEvent> fileAppender = new FileAppender<>();
        fileAppender.setContext(context);
        fileAppender.setFile("logs/like-lion.log");  // 파일 경로 설정
        fileAppender.setAppend(true);  // 기존 로그 이어쓰기
        fileAppender.setEncoder(fileEncoder);
        fileAppender.start();

        // 롤링 파일 Appender 설정 (시간 기반)
        TimeBasedRollingPolicy<ILoggingEvent> rollingPolicy = createRollingPolicy(context);
        rollingPolicy.setParent(fileAppender);
        rollingPolicy.start();  // 롤링 정책 시작

        // 루트 로거 설정: 콘솔 + 파일 Appender 등록
        Logger logger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME); // 정확하게 Logger 객체 가져오기
        if (logger instanceof ch.qos.logback.classic.Logger) {
            ch.qos.logback.classic.Logger rootLogger = (ch.qos.logback.classic.Logger) logger;
            rootLogger.setLevel(Level.INFO);  // INFO 레벨로 설정
            rootLogger.addAppender(consoleAppender);  // 콘솔 Appender 추가
            rootLogger.addAppender(fileAppender);  // 파일 Appender 추가
        }
    }

    private PatternLayoutEncoder createEncoder(LoggerContext context) {
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(context);
        encoder.setPattern("%d{yyyy-MM-dd HH:mm:ss} %-5level %logger{36} - %msg%n");
        encoder.start();
        return encoder;
    }

    private TimeBasedRollingPolicy<ILoggingEvent> createRollingPolicy(LoggerContext context) {
        TimeBasedRollingPolicy<ILoggingEvent> rollingPolicy = new TimeBasedRollingPolicy<>();
        rollingPolicy.setContext(context);
        rollingPolicy.setFileNamePattern("logs/like-lion.%d{yyyy-MM-dd}.log"); // 로그 파일이 날짜별로 롤링됨
        rollingPolicy.setMaxHistory(30);  // 최대 보관 기간 30일
        rollingPolicy.setTotalSizeCap(FileSize.valueOf("1GB"));  // 총 로그 크기 제한 1GB
        return rollingPolicy;
    }
}