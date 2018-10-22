package one.realme.app

import com.github.ajalt.mordant.TermColors
import one.realme.common.Version

object Banner {
    /**
     * generated by http://www.network-science.de/ascii/
     * font: big
     */
    private val banner = mutableListOf("",
            "_____               __",
            "|  __ \\             | |",
            "| |__) | ___   __ _ | | _ __ ___    ___",
            "|  _  / / _ \\ / _` || || '_ ` _ \\  / _ \\",
            "| | \\ \\|  __/| (_| || || | | | | ||  __/",
            "|_|  \\_\\\\___| \\__,_||_||_| |_| |_| \\___|",
            "========================================")
    private const val slogan = " :: Realme One :: "
    private const val strapLineSize = 40

    fun printBanner() {
        for (line in banner) {
            println(line)
        }
        val version = " (v${Version.CURRENT})"
        val padding = StringBuilder()
        val gap = strapLineSize - (version.length + slogan.length)
        while (padding.length < gap) {
            padding.append(" ")
        }
        with(TermColors()) {
            println((brightBlue)("$slogan$padding$version"))
        }
        println()
    }
}