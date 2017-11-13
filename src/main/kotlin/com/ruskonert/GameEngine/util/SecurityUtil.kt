package com.ruskonert.GameEngine.util

import java.security.NoSuchAlgorithmException
import java.security.MessageDigest
import kotlin.experimental.and

class SecurityUtil
{
    companion object
    {
        fun sha256(str: String): String?
        {
            var SHA: String?
            try {
                val sh = MessageDigest.getInstance("SHA-256")
                sh.update(str.toByteArray())
                val byteData = sh.digest()
                val sb = StringBuffer()
                for (i in byteData.indices) {
                    sb.append(Integer.toString((byteData[i] and 0xff.toByte()) + 0x100, 16).substring(1))
                }
                SHA = sb.toString()
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
                SHA = null
            }
            return SHA
        }
    }
}