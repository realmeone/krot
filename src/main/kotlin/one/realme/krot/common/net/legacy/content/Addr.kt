package one.realme.krot.common.net.legacy.content

/**
 * response to getAddr message
 */
class Addr(
        val count: Int, //4 bytes Number of address entries (max: 1000)
        val addrList: List<NetAddr> // max 1000 * 26 bytes
) {

}