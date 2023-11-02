package com.yonatankarp.kotlin.junit.tools.logger

import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.AppenderBase
import org.slf4j.LoggerFactory

/**
 * Custom Logback Appender designed to capture and store log events in-memory.
 * This class extends [AppenderBase]<[ILoggingEvent]> and maintains an in-memory
 * log of log events for the specified class or the root logger if no class is
 * provided. It is particularly useful for testing and debugging purposes when
 * you want to capture and analyze log messages generated during the execution
 * of a specific class or component.
 *
 * @param clazz The optional class for which to capture log events. If null, log
 * events will be captured for the root logger.
 */
class InMemoryLoggerAppender(clazz: Class<*>? = null) : AppenderBase<ILoggingEvent>() {
    private val log = mutableListOf<ILoggingEvent>()

    init {
        if (clazz != null) {
            (LoggerFactory.getLogger(clazz) as Logger).addAppender(this)
        } else {
            (LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME) as Logger).addAppender(this)
        }
        start()
    }

    /**
     * Gets the number of log events stored in the in-memory log.
     */
    val logSize: Int
        get() = log.size

    /**
     * Gets the message of the last logged event in the in-memory log.
     */
    val lastMessage: String
        get() = log[log.size - 1].message

    /**
     * Appends a log event to the in-memory log.
     *
     * @param eventObject The log event to append.
     */
    override fun append(eventObject: ILoggingEvent) {
        log.add(eventObject)
    }

    /**
     * Checks if the in-memory log contains a log event with the specified
     * message.
     *
     * @param message The message to search for in the log events.
     * @return `true` if a log event with the specified message is found;
     * otherwise, `false`.
     */
    fun logContains(message: String) =
        log
            .map { it.formattedMessage }
            .any { it == message }

    /**
     * Clears all log events from the in-memory log.
     */
    fun clearAll() = log.clear()
}
