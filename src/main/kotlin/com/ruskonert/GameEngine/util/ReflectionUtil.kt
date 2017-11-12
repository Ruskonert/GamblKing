@file:JvmName("ReflectionUtil")
package com.ruskonert.GameEngine.util

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
            val met = target.javaClass.getMethod(methodName)
            met.isAccessible = true
            return met.invoke(target, args)
        }
    }
}