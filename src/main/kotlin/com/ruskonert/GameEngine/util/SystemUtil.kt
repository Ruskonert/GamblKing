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
            builder.append("����ó���� �߻��Ͽ����ϴ�. �ڼ��� ������ �Ʒ� �α׸� �����Ͻʽÿ�.\n")
            builder.append("�ٵ� �� ������ �߻��ߴ����� �𸣰ڽ��ϴ�..\n\n")
            builder.append("Caused by: ")
            builder.append(e.toString() + "\n")
            for (ste in stes) builder.append(ste).append("\n")
            JOptionPane.showMessageDialog(null, builder.toString(), "����", JOptionPane.ERROR_MESSAGE)
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