package com.ruskonert.GamblKing.engine.program;

import com.ruskonert.GamblKing.engine.event.EventController;
import com.ruskonert.GamblKing.engine.event.EventListener;
import com.ruskonert.GamblKing.engine.event.LayoutListener;
import javafx.stage.Stage;

public abstract class StageBuilder
{
    public abstract void start(Stage stage);

    /**
     * 레이아웃 이벤트를 등록합니다.
     */
    protected void registerEvent(LayoutListener listener)
    {
        listener.register(this);
    }

    /**
     * 서버 이벤트를 등록합니다.
     */
    protected void registerEvent(EventListener listener) { EventController.signatureListener(listener); }
}
