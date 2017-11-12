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
    }
}