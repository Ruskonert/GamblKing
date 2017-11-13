package com.ruskonert.GameEngine.program;

import com.ruskonert.GameEngine.event.EventListener;
import com.ruskonert.GameEngine.event.LayoutListener;

public interface Register
{
    void registerEvent(LayoutListener listener);

    void registerEvent(EventListener listener);
}
