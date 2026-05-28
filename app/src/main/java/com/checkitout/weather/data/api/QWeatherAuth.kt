package com.checkitout.weather.data.api

import android.util.Base64
import com.checkitout.weather.data.AuthConstants
import net.i2p.crypto.eddsa.EdDSAEngine
import net.i2p.crypto.eddsa.EdDSAPrivateKey
import java.security.spec.PKCS8EncodedKeySpec

object QWeatherAuth {

    private var cachedToken: String? = null
    private var tokenExpiry: Long = 0

    private val privateKey: EdDSAPrivateKey by lazy {
        val derBytes = Base64.decode(AuthConstants.PRIVATE_KEY_PEM, Base64.DEFAULT)
        val spec = PKCS8EncodedKeySpec(derBytes)
        EdDSAPrivateKey(spec)
    }

    @Synchronized
    fun getToken(): String {
        val now = System.currentTimeMillis() / 1000
        if (cachedToken != null && now < tokenExpiry - 30) {
            return cachedToken!!
        }
        return generateToken(now)
    }

    private fun generateToken(now: Long): String {
        val iat = now - 30
        val exp = now + 900

        val header = """{"alg":"EdDSA","kid":"${AuthConstants.CREDENTIAL_ID}"}"""
        val payload = """{"sub":"${AuthConstants.PROJECT_ID}","iat":$iat,"exp":$exp}"""

        val encodedHeader = base64Url(header.toByteArray(Charsets.UTF_8))
        val encodedPayload = base64Url(payload.toByteArray(Charsets.UTF_8))
        val signingInput = "$encodedHeader.$encodedPayload"
        val inputBytes = signingInput.toByteArray(Charsets.UTF_8)

        val engine = EdDSAEngine()
        engine.initSign(privateKey)
        engine.update(inputBytes)
        val signature = engine.sign()

        val token = "$signingInput.${base64Url(signature)}"
        cachedToken = token
        tokenExpiry = exp
        return token
    }

    private fun base64Url(data: ByteArray): String {
        return Base64.encodeToString(data, Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP)
    }
}
