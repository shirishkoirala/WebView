package app.mjproductions.turboscratch.utils

import android.util.Base64
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.security.GeneralSecurityException
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import java.security.KeyException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.text.Charsets.UTF_8

class EncryptDecryptConstant {

    companion object {
        @Throws(
            UnsupportedEncodingException::class,
            InvalidKeyException::class,
            NoSuchAlgorithmException::class,
            NoSuchPaddingException::class,
            InvalidAlgorithmParameterException::class,
            IllegalBlockSizeException::class,
            BadPaddingException::class
        )
        fun AES_Encrypt(plainText: String, key: String): String? {
            val plainTextbytes = plainText.toByteArray(charset("UTF-8"))
            val keyBytes = getKeyBytes(key)
            return Base64.encodeToString(
                encrypt(
                    plainTextbytes, keyBytes,
                    keyBytes
                ), Base64.NO_WRAP
            )
        }


        fun encryptForPWA(data: String): String {
            // Check for null data
            // Generate a random IV (16 bytes for AES)
            val ivBytes = ByteArray(16)
            SecureRandom().nextBytes(ivBytes)
            val ivSpec = IvParameterSpec(ivBytes)

            // Decode the secret key from Base64 and create a SecretKeySpec.
            val keyBytes = Base64.decode(Constants.SECRET_KEY, Base64.DEFAULT)
            val secretKeySpec = SecretKeySpec(keyBytes, "AES")

            // Initialize the cipher for encryption using AES/CBC/PKCS5Padding.
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec)

            // Encrypt the data (using UTF-8 encoding for the input)
            val encryptedBytes = cipher.doFinal(data.toByteArray(UTF_8))
            // Convert both the IV and encrypted data to Base64 strings.
            val ivBase64 = Base64.encodeToString(ivBytes, Base64.URL_SAFE or Base64.NO_WRAP)
            val encryptedBase64 = Base64.encodeToString(encryptedBytes, Base64.URL_SAFE or Base64.NO_WRAP)

            // Concatenate the Base64 IV and encrypted data, separated by a colon.
            return "$ivBase64:$encryptedBase64"
        }

        @Throws(UnsupportedEncodingException::class)
        private fun getKeyBytes(key: String): ByteArray {
            val keyBytes = ByteArray(16)
            val parameterKeyBytes = key.toByteArray(charset("UTF-8"))
            System.arraycopy(
                parameterKeyBytes, 0, keyBytes, 0,
                Math.min(parameterKeyBytes.size, keyBytes.size)
            )
            return keyBytes
        }

        private var cipherTransformation: String? = "AES/CBC/PKCS5Padding"
        private const val aesEncryptionAlgorithm = "AES"
        private const val characterEncoding = "UTF-8"

        @Throws(
            NoSuchAlgorithmException::class,
            NoSuchPaddingException::class,
            InvalidKeyException::class,
            InvalidAlgorithmParameterException::class,
            IllegalBlockSizeException::class,
            BadPaddingException::class
        )

        fun encrypt(
            plainTextOriginal: ByteArray?,
            key: ByteArray?,
            initialVector: ByteArray?
        ): ByteArray? {
            var plainText = plainTextOriginal
            val cipher =
                Cipher.getInstance(cipherTransformation)
            val secretKeySpec =
                SecretKeySpec(key, aesEncryptionAlgorithm)
            val ivParameterSpec = IvParameterSpec(initialVector)
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec)
            plainText = cipher.doFinal(plainText)
            return plainText
        }

        @Throws(
            KeyException::class,
            GeneralSecurityException::class,
            GeneralSecurityException::class,
            InvalidAlgorithmParameterException::class,
            IllegalBlockSizeException::class,
            BadPaddingException::class,
            IOException::class
        )
        fun decrypt(encryptedText: String?, key: String?): String? {
            val cipheredBytes = Base64.decode(encryptedText, Base64.DEFAULT)
            val keyBytes: ByteArray? = key?.let { getKeyBytes(it) }
            return decrypt(
                cipheredBytes,
                keyBytes,
                keyBytes
            )?.let {
                String(
                    it
                )
            }
        }

        @Throws(
            NoSuchAlgorithmException::class,
            NoSuchPaddingException::class,
            InvalidKeyException::class,
            InvalidAlgorithmParameterException::class,
            IllegalBlockSizeException::class,
            BadPaddingException::class
        )
        fun decrypt(
            cipherTextOriginal: ByteArray?,
            key: ByteArray?,
            initialVector: ByteArray?,
        ): ByteArray? {
            var cipherText = cipherTextOriginal
            val cipher =
                Cipher.getInstance(cipherTransformation)
            val secretKeySpecy =
                SecretKeySpec(key, aesEncryptionAlgorithm)
            val ivParameterSpec = IvParameterSpec(initialVector)
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpecy, ivParameterSpec)
            cipherText = cipher.doFinal(cipherText)
            return cipherText
        }

        fun SHA256(strText: String): String? {
            var strResult: String? = null
            if (strText.isNotEmpty()) {
                try {
                    val messageDigest = MessageDigest.getInstance("SHA-256")
                    messageDigest.update(strText.toByteArray(UTF_8))
                    val byteBuffer = messageDigest.digest()
                    val strHexString = StringBuffer()
                    for (i in byteBuffer.indices) {
                        val hex = Integer.toHexString(0xff and byteBuffer[i].toInt())
                        if (hex.length == 1) {
                            strHexString.append('0')
                        }
                        strHexString.append(hex)
                    }
                    strResult = strHexString.toString()
                } catch (e: NoSuchAlgorithmException) {
                    e.printStackTrace()
                } catch (e: UnsupportedEncodingException) {
                    e.printStackTrace()
                }
            }
            return strResult
        }

        fun String.md5(): String {
            return hashString(this, "MD5")
        }

        fun String.sha256(): String {
            return hashString(this, "SHA-256")
        }

        private fun hashString(input: String, algorithm: String): String {
            return MessageDigest
                .getInstance(algorithm)
                .digest(input.toByteArray())
                .fold("", { str, it -> str + "%02x".format(it) })
        }
    }
}