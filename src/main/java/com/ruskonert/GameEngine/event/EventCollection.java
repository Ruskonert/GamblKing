package com.ruskonert.GameEngine.event;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EventCollection
{
    private static Map<Class<?>, List<Method>> EVENT_HANDLER_COLLECTION = new ConcurrentHashMap<>();
    public static void registerMethod(Class<?> type, Method c)
    {
        if(EVENT_HANDLER_COLLECTION.containsKey(type))
        {

        }
        else
        {
            List<Method> methodList = new
        }
    }
}
