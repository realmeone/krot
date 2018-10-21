package one.realme.chain

import com.typesafe.config.ConfigFactory
import one.realme.common.UnixTime
import one.realme.common.Version
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class BlockTest {

    @Test
    fun testBlock() {
        val b1 = Block(Version.CURRENT, 1, Hash.empty())
        val b2 = Block(Version.CURRENT, 2, b1.hash())
        assertEquals(b1.hash(), b2.header().prevBlockHash)
        println(b1.hash())
        println(b2.hash())
    }

    @Test
    fun testGenesis() {
        val env = ConfigFactory.load("testnet.conf")
        val version = env.getInt("genesis.version")
        val height = env.getLong("genesis.height")
        val prevBlockHash = env.getString("genesis.prevBlockHash")
        val timestamp = env.getInt("genesis.timestamp")

        val genesis = Block(version, height, Hash.fromString(prevBlockHash), UnixTime.fromSeconds(timestamp))
        println(genesis)
        println(genesis.hash())
    }
}