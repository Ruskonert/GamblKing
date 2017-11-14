package com.ruskonert.GamblKing.engine.event;

import com.ruskonert.GamblKing.engine.execption.EventExecption;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class EventController
{
    private static Map<Class<Event>, List<EventEntry<EventListener, Method>>> EVENT_HANDLER_COLLECTION = new ConcurrentHashMap<>();
    public static void signatureListener(EventListener listener)
    {
        EventEntry<EventListener, Method> listenerEntry = new EventEntry<>();
        listenerEntry.setKey(listener);
        for(Method m : listener.getClass().getDeclaredMethods())
        {
            if(m.getDeclaredAnnotation(Handle.class) != null)
            {
                Class<?> parameterType = m.getParameterTypes()[0];
                Class<?> eventType = Event.class;
                if(eventType.isAssignableFrom(parameterType))
                {
                    listenerEntry.setValue(m);
                    registerEvent((Class<Event>) m.getParameterTypes()[0], listenerEntry);
                }
            }
        }
    }

    private static void registerEvent(Class<Event> event, EventEntry<EventListener, Method> entry)
    {
        if(!EVENT_HANDLER_COLLECTION.containsKey(event)) {
            List<EventEntry<EventListener, Method>> entryList = new ArrayList<>();
            entryList.add(entry);
            EVENT_HANDLER_COLLECTION.put(event, entryList);
        }
        else {
            EVENT_HANDLER_COLLECTION.get(event).add(entry);
        }
    }

    public static void invokeEvent(Event eventTarget)
    {
        if(EVENT_HANDLER_COLLECTION.containsKey(eventTarget.getClass()))
        {
            for(EventEntry<EventListener, Method> entry :EVENT_HANDLER_COLLECTION.get(eventTarget.getClass())) {
                try {
                    entry.getValue().invoke(entry.getKey(), eventTarget);
                }
                catch (IllegalAccessException | InvocationTargetException e) {
                    throw new EventExecption(e);
                }
            }
        }
    }
}
class EventEntry<K,V> implements Map.Entry<K, V>
{
    private K key;
    private V value;

    public void setKey(K key) { this.key = key; }

    @Override public K getKey() { return this.key; }

    @Override public V getValue() { return this.value; }

    @Override public V setValue(V value) { this.value = value; return value; }
}
