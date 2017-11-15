package com.ruskonert.GamblKing.program;

import com.ruskonert.GamblKing.event.EventListener;
import com.ruskonert.GamblKing.event.LayoutListener;

public interface Register
{
    void registerEvent(LayoutListener listener);

    void registerEvent(EventListener listener);
}
