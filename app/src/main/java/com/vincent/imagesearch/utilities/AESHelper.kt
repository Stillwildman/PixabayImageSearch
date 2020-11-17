package com.vincent.imagesearch.utilities

import android.util.Base64
import android.util.Log
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * Created by Vincent on 2020/11/17.
 */
object AESHelper {

    private const val secretKey = "stillwildman"

    fun encrypt(strToEncrypt: String): String? {
        try {
            Log.i("AES", "Encrypt original params:\n$strToEncrypt")

            val iv = byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
            val ivSpec = IvParameterSpec(iv)

            val digest = MessageDigest.getInstance("SHA-256")
            digest.update(secretKey.toByteArray(StandardCharsets.UTF_8))

            val keyBytes = ByteArray(32)
            System.arraycopy(digest.digest(), 0, keyBytes, 0, keyBytes.size)

            val sKey = SecretKeySpec(keyBytes, "AES")

            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, sKey, ivSpec)

            val encrypted = cipher.doFinal(strToEncrypt.toByteArray(StandardCharsets.UTF_8))
            return String(Base64.encode(encrypted, Base64.DEFAULT), StandardCharsets.UTF_8)
        }
        catch (ex: Exception) {
            Log.e("AESHelper", "Error while encrypting: ${ex.message ?: ""}")
        }
        return ""
    }

    fun decrypt(strToDecrypt: String?): String? {
        try {
            Log.i("AES", "Decrypt original params:\n$strToDecrypt")

            val iv = byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
            val ivSpec = IvParameterSpec(iv)

            val digest = MessageDigest.getInstance("SHA-256")
            digest.update(secretKey.toByteArray(StandardCharsets.UTF_8))

            val keyBytes = ByteArray(32)
            System.arraycopy(digest.digest(), 0, keyBytes, 0, keyBytes.size)

            val sKey = SecretKeySpec(keyBytes, "AES")

            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.DECRYPT_MODE, sKey, ivSpec)

            val bytes = Base64.decode(strToDecrypt, Base64.DEFAULT)
            val decrypted = cipher.doFinal(bytes)

            return String(decrypted, StandardCharsets.UTF_8)
        }
        catch (ex: java.lang.Exception) {
            Log.e("AESHelper", "Error while decrypting: ${ex.message ?: ""}")
        }
        return ""
    }

}