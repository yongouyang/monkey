// File: src/main/resources/logback.groovy


import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.rolling.RollingFileAppender
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy
import ch.qos.logback.core.status.OnConsoleStatusListener

import static ch.qos.logback.classic.Level.DEBUG
import static ch.qos.logback.classic.Level.INFO

displayStatusOnConsole()
scan('5 minutes')  // Scan for changes every 5 minutes.
setupAppenders()
setupLoggers()

def displayStatusOnConsole() {
    statusListener OnConsoleStatusListener
}

def setupAppenders() {
    def logfileDate = timestamp('yyyy-MM-dd') // Formatted current date.
    // hostname is a binding variable injected by Logback.
    appender('logfile', RollingFileAppender) {
        file = "logs/monkey_${logfileDate}.log"
        encoder(PatternLayoutEncoder) {
            pattern = "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
        }
        rollingPolicy(TimeBasedRollingPolicy) {
            FileNamePattern = "logs/monkey_%d{yyyy-MM}.zip"
        }
    }

    appender('console', ConsoleAppender) {
        encoder(PatternLayoutEncoder) {
            pattern = "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
        }
    }
}

def setupLoggers() {
    logger 'org.monkey', getLogLevel(), ['logfile']
    root DEBUG, ['console']
}

def getLogLevel() {
    (isDevelopmentEnv() ? DEBUG : INFO)
}

def isDevelopmentEnv() {
    def env = System.properties['MONKEY_ENV'] ?: 'TESTING'
    env == 'TESTING'
}

