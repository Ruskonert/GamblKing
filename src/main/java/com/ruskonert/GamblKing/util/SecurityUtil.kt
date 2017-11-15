@file:JvmName("SecurityUtil")
package com.ruskonert.GamblKing.util

import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.RandomAccessFile
import java.net.URISyntaxException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import java.util.jar.JarFile

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

        @Throws(Exception::class)
        fun extractFileHashSHA256(filename:String):String
        {
            var SHA = ""
            val buff = 16384
            try
            {
                val file = RandomAccessFile(filename, "r")
                val hashSum = MessageDigest.getInstance("SHA-256")
                val buffer = ByteArray(buff)
                var partialHash : ByteArray? = null
                var read : Long = 0L
                // calculate the hash of the hole file for the test
                val offset = file.length()
                var unitsize : Int
                while (read < offset)
                {
                    unitsize = (if (((offset - read) >= buff)) buff else (offset - read).toInt())
                    file.read(buffer, 0, unitsize)
                    hashSum.update(buffer, 0, unitsize)
                    read += unitsize.toLong()
                }
                file.close()
                partialHash = ByteArray(hashSum.getDigestLength())
                partialHash = hashSum.digest()
                val sb = StringBuffer()
                for (i in partialHash.indices)
                {
                    sb.append(Integer.toString((partialHash[i] and 0xFF.toByte()) + 0x100, 16).substring(1))
                }
                SHA = sb.toString()
            }
            catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
            return SHA
        }

        fun getClassesForPackage(pkg: Package): List<Class<*>>
        {
            val pkgname = pkg.name

            val classes = ArrayList<Class<*>>()

            // Get a File object for the package
            var directory: File? = null
            val fullPath: String
            val relPath = pkgname.replace('.', '/')

            //System.out.println("ClassDiscovery: Package: " + pkgname + " becomes Path:" + relPath);

            val resource = ClassLoader.getSystemClassLoader().getResource(relPath) ?: throw RuntimeException("No resource for " + relPath)

            //System.out.println("ClassDiscovery: Resource = " + resource);
            fullPath = resource.file
            //System.out.println("ClassDiscovery: FullPath = " + resource);

            try {
                directory = File(resource.toURI())
            } catch (e: URISyntaxException) {
                throw RuntimeException("$pkgname ($resource) does not appear to be a valid URL / URI.  Strange, since we got it from the system...", e)
            } catch (e: IllegalArgumentException) {
                directory = null
            }

            //System.out.println("ClassDiscovery: Directory = " + directory);

            if (directory != null && directory.exists()) {

                // Get the list of the files contained in the package
                val files = directory.list()
                for (i in files!!.indices) {

                    // we are only interested in .class files
                    if (files[i].endsWith(".class")) {

                        // removes the .class extension
                        val className = pkgname + '.' + files[i].substring(0, files[i].length - 6)

                        //System.out.println("ClassDiscovery: className = " + className);

                        try {
                            classes.add(Class.forName(className))
                        } catch (e: ClassNotFoundException) {
                            throw RuntimeException("ClassNotFoundException loading " + className)
                        }

                    }
                }
            } else {
                try {
                    val jarPath = fullPath.replaceFirst("[.]jar[!].*".toRegex(), ".jar").replaceFirst("file:".toRegex(), "")
                    val jarFile = JarFile(jarPath)
                    val entries = jarFile.entries()
                    while (entries.hasMoreElements()) {
                        val entry = entries.nextElement()
                        val entryName = entry.name
                        if (entryName.startsWith(relPath) && entryName.length > relPath.length + "/".length) {

                            //System.out.println("ClassDiscovery: JarEntry: " + entryName);
                            val className = entryName.replace('/', '.').replace('\\', '.').replace(".class", "")

                            //System.out.println("ClassDiscovery: className = " + className);
                            try {
                                classes.add(Class.forName(className))
                            } catch (e: ClassNotFoundException) {
                                throw RuntimeException("ClassNotFoundException loading " + className)
                            }

                        }
                    }
                } catch (e: IOException) {
                    throw RuntimeException("$pkgname ($directory) does not appear to be a valid package", e)
                }

            }
            return classes
        }
    }
}