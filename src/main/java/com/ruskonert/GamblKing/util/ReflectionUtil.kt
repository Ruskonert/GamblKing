@file:JvmName("ReflectionUtil")
package com.ruskonert.GamblKing.util

import java.lang.reflect.Method

class ReflectionUtil
{
    companion object
    {
        @Throws(NoSuchFieldException::class, IllegalAccessException::class)
        fun setStaticField(clazz: Class<*>, name: String, value: Any)
        {
            val field = clazz.getDeclaredField(name)
            field.isAccessible = true
            field.set(null, value)
        }

        fun invokeMethod(target: Any, methodName : String, vararg args : Any?) : Any?
        {
            var met : Method? = null
            try
            {
                met = target.javaClass.getMethod(methodName)
            }
            catch(e : NoSuchMethodException)
            {
                met = target.javaClass.getDeclaredMethod(methodName)
            }

            met!!.isAccessible = true
            return met.invoke(target, args)
        }
    }
}