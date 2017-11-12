@file:JvmName("SystemUtil")
package com.ruskonert.GameEngine.util

import java.io.File
import java.net.URL
import javax.swing.JOptionPane

class SystemUtil
{
    companion object
    {
        fun error(e : Exception)
        {
            val stes = e.stackTrace
            val builder = StringBuilder()
            builder.append("예외처리가 발생하였습니다. 자세한 내용은 아래 로그를 참고하십시오.\n")
            builder.append("근데 왜 오류가 발생했는지는 모르겠습니다..\n\n")
            builder.append("Caused by: ")
            builder.append(e.toString() + "\n")
            for (ste in stes) builder.append(ste).append("\n")
            JOptionPane.showMessageDialog(null, builder.toString(), "오류", JOptionPane.ERROR_MESSAGE)
            System.exit(-1)
        }

        fun getStylePath(filename : String) : URL = File(filename).toURI().toURL()

        fun getFiles(folder: File): Int
        {
            count = 0;
            getFilesDetailed(folder)
            return count
        }

        private var count : Int = 0

        private fun getFilesDetailed(folder: File)
        {
            for (fileEntry in folder.listFiles()!!)
            {
                if(fileEntry.isDirectory)
                {
                    getFilesDetailed(fileEntry)
                    continue
                }
                else
                {
                    count++
                }
            }
        }
    }
}