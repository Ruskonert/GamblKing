package com.ruskonert.GameServer.server;

import com.ruskonert.GameServer.MessageDispatch;
import com.ruskonert.GameServer.MessageType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public interface ConsoleSender extends MessageDispatch
{
    TextField getCommandField();

    TextArea getConsoleScreen();

    void sendRawMessage(String message);

    void sendMessage(String message, MessageType type);

    void clearScreen();

    void clearCommandField();
}
