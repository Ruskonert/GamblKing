package com.ruskonert.GameEngine.program.event;

import com.ruskonert.GameEngine.GameServer;
import com.ruskonert.GameEngine.Listener;
import com.ruskonert.GameEngine.MessageType;
import com.ruskonert.GameEngine.program.AppFramework;
import com.ruskonert.GameEngine.program.ProgramManager;
import com.ruskonert.GameEngine.program.component.ProgramComponent;
import com.ruskonert.GameEngine.server.ConsoleSender;
import com.ruskonert.GameEngine.util.ReflectionUtil;

import javafx.application.Platform;
import javafx.scene.input.KeyCode;

public class ConsoleLayoutEvent implements Listener
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
                }
            }
        });

        // clicked exit button
        component.getMainExitButton().setOnMouseClicked( event -> {
            AppFramework.getApplictionStage().setOnCloseRequest(e -> Platform.exit());
            ReflectionUtil.Companion.invokeMethod(framework, "onDisableInner");
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
