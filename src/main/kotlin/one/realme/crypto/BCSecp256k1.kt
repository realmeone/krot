package one.realme.crypto

import one.realme.crypto.encoding.Hex
import org.bouncycastle.jce.ECNamedCurveTable
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.jce.spec.ECPrivateKeySpec
import org.bouncycastle.jce.spec.ECPublicKeySpec
import java.math.BigInteger
import java.security.*
import java.security.interfaces.ECPrivateKey
import java.security.interfaces.ECPublicKey


/**
 * BC Provider
 *
 * secp256k1 faster than SUN Provider and my Provider
 */
object BCSecp256k1 {
    // the data already SHA256 hashed. so there is no need for SHA256withECDSA
    private const val ALG_SIGN = "NONEwithECDSA"
    private const val ALG_PROVIDER = "BC"
    private const val ALG = "ECDSA"
    private const val RADIX = 16
    private const val CURVE = "secp256k1"
    private val ecSpec = ECNamedCurveTable.getParameterSpec(CURVE)

    init {
        Security.addProvider(BouncyCastleProvider())
    }

    fun newKeyPair(): Pair<String, String> {
        val g = KeyPairGenerator.getInstance(ALG, ALG_PROVIDER)
        g.initialize(ecSpec, SecureRandom())
        val keyPair = g.generateKeyPair()
        val privateKey = keyPair.private as ECPrivateKey
        val publicKey = keyPair.public as ECPublicKey

        // gX, gY, s need fix to 64 size
        val gX = adjustTo64(publicKey.w.affineX)
        val gY = adjustTo64(publicKey.w.affineY)
        val s = adjustTo64(privateKey.s)
        return Pair(s, "04$gX$gY")
    }

    fun computePublicKey(sec: String): String {
        val point = ecSpec.g.multiply(BigInteger(sec, RADIX))
        return Hex.encode(point.getEncoded(false))
    }


    fun sign(hash: String, sec: String): String {
        require(hash.length == 64) { "Not a valid data, have you passed sha256 hash data?" }
        val priKeySpec = ECPrivateKeySpec(BigInteger(sec, RADIX), ecSpec)
        val priKey = KeyFactory.getInstance(ALG, ALG_PROVIDER).generatePrivate(priKeySpec)
        val signature = Signature.getInstance(ALG_SIGN)
        signature.initSign(priKey)
        signature.update(Hex.decode(hash))
        return Hex.encode(signature.sign())
    }

    /**
     * @param data Hex data
     * @param sig Hex sig
     * @param pub public key with "04" prefix
     */
    fun verify(data: String, sig: String, pub: String): Boolean {
        // verify site
        require(data.length == 64) { "Not a valid data, have you passed sha256 hash data?" }
        require(sig.length <= 520) { "Signed data is too big." }
        require(pub.length <= 520) { "Public key is too big." }

        // decode data from hex
        val dataBytes = Hex.decode(data)
        val sigBytes = Hex.decode(sig)

        // gen  public key
        val pubKeySpec = ECPublicKeySpec(
                ecSpec.curve.decodePoint(Hex.decode(pub)), // Q
                ecSpec)
        val keyFactory = KeyFactory.getInstance(ALG, ALG_PROVIDER)
        val pubKey = keyFactory.generatePublic(pubKeySpec)

        // verify
        val signature = Signature.getInstance(ALG_SIGN)
        signature.initVerify(pubKey)
        signature.update(dataBytes)
        return signature.verify(sigBytes)
    }

    private fun adjustTo64(s: BigInteger): String = String.format("%064x", s)
}