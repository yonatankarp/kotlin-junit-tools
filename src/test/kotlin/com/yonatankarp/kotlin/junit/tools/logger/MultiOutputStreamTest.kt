package com.yonatankarp.kotlin.junit.tools.logger

import java.io.ByteArrayOutputStream
import kotlin.test.Test
import org.junit.jupiter.api.Assertions.assertEquals

internal class MultiOutputStreamTest {
    @Test
    fun `test write single byte`() {
        // Given
        val originalOutputStream = ByteArrayOutputStream()
        val captureOutputStream = ByteArrayOutputStream()
        MultiOutputStream(originalOutputStream, captureOutputStream, System.out).use { dualPrintStream ->
            // When
            val byteToWrite = 'A'.code.toByte()
            dualPrintStream.write(byteToWrite.toInt())

            // Then
            assertEquals(byteToWrite, originalOutputStream.toByteArray().first())
            assertEquals(byteToWrite, captureOutputStream.toByteArray().first())
        }
    }

    @Test
    fun `test write nyte array`() {
        // Given
        val originalOutputStream = ByteArrayOutputStream()
        val captureOutputStream = ByteArrayOutputStream()
        MultiOutputStream(originalOutputStream, captureOutputStream, System.out).use { dualPrintStream ->
            // When
            val byteArrayToWrite = "Hello, World!".toByteArray()
            dualPrintStream.write(byteArrayToWrite)

            // Then
            val originalOutputBytes = originalOutputStream.toByteArray()
            val captureOutputBytes = captureOutputStream.toByteArray()

            assertEquals(byteArrayToWrite.size, originalOutputBytes.size)
            assertEquals(byteArrayToWrite.size, captureOutputBytes.size)

            for (i in byteArrayToWrite.indices) {
                assertEquals(byteArrayToWrite[i], originalOutputBytes[i])
                assertEquals(byteArrayToWrite[i], captureOutputBytes[i])
            }
        }
    }
}
