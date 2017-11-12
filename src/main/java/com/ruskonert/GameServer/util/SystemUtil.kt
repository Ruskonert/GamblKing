package com.ruskonert.GameServer.util

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
            builder.append("프로그램을 실행하는 중 오류가 발생하였습니다. 원인은 아래를 참고하시기 바랍니다.\n")
            builder.append("근데 이 메세지가 왜 뜨는지 나도 모르겠습니다 허허..\n\n")
            builder.append("Caused by: ")
            builder.append(e.toString() + "\n")
            for (ste in stes) builder.append(ste).append("\n")
            JOptionPane.showMessageDialog(null, builder.toString(), "예외 오류 발생", JOptionPane.ERROR_MESSAGE)
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