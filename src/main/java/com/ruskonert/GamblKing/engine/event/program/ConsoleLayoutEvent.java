package com.ruskonert.GamblKing.engine.event.program;

import com.ruskonert.GamblKing.engine.GameServer;
import com.ruskonert.GamblKing.event.LayoutListener;

import com.ruskonert.GamblKing.engine.MessageType;
import com.ruskonert.GamblKing.engine.program.AppFramework;
import com.ruskonert.GamblKing.engine.program.ProgramManager;
import com.ruskonert.GamblKing.engine.program.component.ProgramComponent;
import com.ruskonert.GamblKing.engine.server.ConsoleSender;
import com.ruskonert.GamblKing.util.ReflectionUtil;

import javafx.application.Platform;
import javafx.scene.input.KeyCode;

import java.io.IOException;

public class ConsoleLayoutEvent implements LayoutListener
{
    @Override
    public void register(Object handleInstance)
    {
        AppFramework framework = (AppFramework)handleInstance;
        ProgramComponent component = ProgramManager.getProgramComponent();
        ConsoleSender sender = GameServer.getServer().getConsoleSender();

        // Clicked clear button
        component.getMainClearButton().setOnMouseClicked( event -> sender.clearScreen() );

        // Clicked start button
        component.getMainStartButton().setOnMouseClicked(event -> {
            component.getMainStartButton().setText("Running");
            component.getMainStartButton().setDisable(true);
            try
            {
                GameServer.getServer().getBindConnection().getBackgroundTask().start();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        });

        // Console send button clicked on mouse
        component.getConsoleSendButton().setOnMouseClicked( event -> {
            String fieldText = component.getConsoleMessageField().getText();
            if(fieldText.isEmpty()) {
                sender.sendMessage("Sending message is null or empty.", MessageType.ERROR); }
            else
            {
                sender.clearCommandField();
                if(fieldText.startsWith("/"))
                {
                    sender.dispatch(fieldText);
                }
                else
                {
                    sender.sendMessage(fieldText);
                    sender.sendAll(fieldText);

                }
            }
        });

        // clicked exit button
        component.getMainExitButton().setOnMouseClicked( event -> {
            AppFramework.getApplictionStage().setOnCloseRequest(e -> Platform.exit());
            ReflectionUtil.Companion.invokeMethod(framework, "onDisable");
        });

        // using console send message field and press enter
        component.getConsoleMessageField().setOnKeyPressed( event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String message = component.getConsoleMessageField().getText();
                if (message.isEmpty()) {
                    sender.sendMessage("Sending message is null or empty.", MessageType.ERROR);
                    return;
                }
                sender.clearCommandField();
                if (message.startsWith("/")) {
                    sender.dispatch(message);
                } else {
                    sender.sendMessage(message);
                }
            }
        });
    }
}
