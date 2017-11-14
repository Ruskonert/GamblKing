package com.ruskonert.GamblKing.engine.program;

import com.ruskonert.GamblKing.engine.event.EventListener;
import com.ruskonert.GamblKing.engine.event.LayoutListener;

public interface Register
{
    void registerEvent(LayoutListener listener);

    void registerEvent(EventListener listener);
}
