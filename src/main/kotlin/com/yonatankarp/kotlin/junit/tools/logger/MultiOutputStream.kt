package com.yonatankarp.kotlin.junit.tools.logger

import java.io.Closeable
import java.io.OutputStream
import java.io.PrintStream

/**
 * A custom PrintStream that captures output to two different OutputStreams
 * simultaneously.
 *
 * This class extends the PrintStream class and allows you to capture and
 * duplicate the output that would normally be written to the 'originalOut'
 * OutputStream to both the 'originalOut' and the 'captureOut' OutputStream.
 *
 * @param originalOut The original OutputStream to which the output is originally intended.
 * @param captureOut The OutputStreams to capture and duplicate the output. You can specify multiple
 * OutputStreams as varargs.
 */
class MultiOutputStream(originalOut: OutputStream, vararg captureOut: OutputStream): Closeable {

    private val originalPrintStream = PrintStream(originalOut)
    private val capturedStream = captureOut.map { PrintStream(it) }.toList()

    /**
     * Writes a single byte to both the 'originalOut' and 'captureOut' OutputStreams.
     *
     * @param body The byte to be written.
     */
    fun write(body: Int) {
        originalPrintStream.write(body)
        capturedStream.forEach { it.write(body) }
    }

    /**
     * Writes a portion of an array of bytes to both the 'originalOut' and 'captureOut' OutputStreams.
     *
     * @param body The byte array.
     * @param offset The start offset in the data.
     */
    fun write(body: ByteArray, offset: Int = 0) {
        originalPrintStream.write(body, offset, body.size)
        capturedStream.forEach { it.write(body, offset, body.size) }
    }

    /**
     * Closes the DualPrintStream and all associated OutputStreams.
     */
    override fun close() {
        originalPrintStream.close()
        capturedStream.forEach { it.close() }
    }
}
