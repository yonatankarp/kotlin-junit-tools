package com.yonatankarp.kotlin.junit.tools.logger

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import ch.qos.logback.classic.Logger
import org.junit.jupiter.api.AfterEach
import org.slf4j.event.Level.INFO
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class InMemoryLoggerAppenderTest {

    private val logger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME)
        .apply { atLevel(INFO) }
    private val appender = InMemoryLoggerAppender()

    @BeforeEach
    fun setup() {
        appender.start()
    }

    @AfterEach
    fun tearDown() {
        appender.stop()
        appender.clearAll()
    }

    @Test
    fun `test logContains`() {
        logger.info("This is a test message")
        assertTrue { appender.logContains("This is a test message") }
        assertFalse { appender.logContains("This is not in the log") }
    }

    @Test
    fun `test clearAll`() {
        logger.info("This is a test message")
        appender.clearAll()
        assertEquals(0, appender.logSize)
    }

    @Test
    fun `test logSize`() {
        logger.info("Message 1")
        logger.info("Message 2")
        logger.info("Message 3")
        assertEquals(3, appender.logSize)
    }

    @Test
    fun `test log with different log levels`() {
        logger.debug("Debug message")
        logger.info("Info message")
        logger.warn("Warn message")
        logger.error("Error message")
        assertEquals(4, appender.logSize)
    }

    @Test
    fun `test lastMessage`() {
        logger.info("Message 1")
        logger.warn("Message 2")
        logger.error("Message 3")
        assertEquals("Message 3", appender.lastMessage)
    }

    @Test
    fun `test init appender with class object catch only classes logs`() {
        val instance = object {
            val logger = LoggerFactory.getLogger(this::class.java)
                .apply { atLevel(INFO) }
        }

        val classAppender = InMemoryLoggerAppender(instance::class.java)

        instance.logger.info("Message 1")
        logger.info("Message 2")

        assertEquals(1, classAppender.logSize)
        assertEquals("Message 1", classAppender.lastMessage)
    }

    @Test
    fun `test multi threaded logging`() {
        val executor = Executors.newFixedThreadPool(5)
        val logCount = AtomicInteger(0)

        for (i in 1..5) {
            executor.execute {
                logger.info("Thread $i log message")
                logCount.incrementAndGet()
            }
        }

        executor.shutdown()
        executor.awaitTermination(5, TimeUnit.SECONDS)

        assertEquals(logCount.get(), appender.logSize)
    }
}
