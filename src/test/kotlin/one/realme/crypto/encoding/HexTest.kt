package one.realme.crypto.encoding

import com.google.common.base.Stopwatch
import one.realme.crypto.digest.Ripemd160
import one.realme.crypto.digest.inOneGo
import org.bouncycastle.crypto.digests.RIPEMD160Digest
import org.junit.Assert.*
import org.junit.Test
import java.lang.Exception
import java.util.concurrent.TimeUnit
import java.util.stream.IntStream

/**
 * test cases from https://tools.ietf.org/html/rfc4648#page-10
 * see Page 12. Test Vectors Base16
 *
 * why not use guava BaseEncoding.base16 to do Hex thing?
 * guava base16 cannot pass the test cases "testDecoding",
 * it require UpperCase or LowerCase, so we implements one
 */
class HexTest {
    @Test
    fun whoIsFasterDecode() {
        val raw = "666f6f"

        val round = 500000
        println("Hex decode round : $round")
        val watch = Stopwatch.createStarted()
        IntStream.range(0, round).parallel().forEach {
            Hex.decode(raw)
        }
        watch.stop()
        println("my use time : ${watch.elapsed(TimeUnit.MILLISECONDS) / 1000.0} seconds")

        watch.reset()
        watch.start()
        IntStream.range(0, round).parallel().forEach {
            org.bouncycastle.util.encoders.Hex.decode(raw)
        }
        watch.stop()
        println("BC use time : ${watch.elapsed(TimeUnit.MILLISECONDS) / 1000.0} seconds")
    }

    @Test
    fun whoIsFaster() {
        val raw = "foo".toByteArray()

        val round = 500000
        println("Hex round : $round")
        val watch = Stopwatch.createStarted()
        IntStream.range(0, round).parallel().forEach {
            Hex.encode(raw)
        }
        watch.stop()
        println("my use time : ${watch.elapsed(TimeUnit.MILLISECONDS) / 1000.0} seconds")

        watch.reset()
        watch.start()
        IntStream.range(0, round).parallel().forEach {
            org.bouncycastle.util.encoders.Hex.encode(raw)
        }
        watch.stop()
        println("BC use time : ${watch.elapsed(TimeUnit.MILLISECONDS) / 1000.0} seconds")
    }

    private val cases = listOf(
            listOf("".toByteArray(), ""),
            listOf("1".toByteArray(), "31"),
            listOf("A".toByteArray(), "41"),
            listOf("f".toByteArray(), "66"),
            listOf("fo".toByteArray(), "666f"),
            listOf("foo".toByteArray(), "666f6f"),
            listOf("foob".toByteArray(), "666f6f62"),
            listOf("fooba".toByteArray(), "666f6f6261"),
            listOf("foobar".toByteArray(), "666f6f626172"),
            listOf("Hello World".toByteArray(), "48656C6C6F20576F726C64"),
            listOf(ByteArray(1) { 10 }, "0a"),
            listOf(ByteArray(1) { 255.toByte() }, "ff"),
            listOf(ByteArray(1), "00"),
            listOf(ByteArray(2), "0000"),
            listOf(ByteArray(3), "000000"),
            listOf(ByteArray(4), "00000000"),
            listOf(ByteArray(5), "0000000000"),
            listOf(ByteArray(6), "000000000000"),
            listOf(ByteArray(36), "000000000000000000000000000000000000000000000000000000000000000000000000")
    )


    @Test
    fun testEncoding() {
        for (it in cases)
            assertEquals(Hex.encode(it[0] as ByteArray).toUpperCase(), (it[1] as String).toUpperCase())
    }

    @Test
    fun testDecoding() {
        for (it in cases)
            try {
                assertArrayEquals(it[0] as ByteArray, Hex.decode(it[1] as String))
            } catch (e: Exception) {
                fail("${it[1]} decoded to ${it[0]} is failed")
                e.printStackTrace()
            }
    }
}