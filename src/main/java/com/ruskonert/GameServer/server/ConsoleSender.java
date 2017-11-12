package com.ruskonert.GameServer.server;

import com.ruskonert.GameServer.entity.MessageDispatcher;
import com.ruskonert.GameServer.MessageType;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public interface ConsoleSender extends MessageDispatcher
{
    TextField getCommandField();

    TextArea getConsoleScreen();

    StringProperty getMessageProperty();

    void sendRawMessage(String message);

    void sendMessage(String message, MessageType type);

    void clearScreen();

    void clearCommandField();

    void dispatch(String command);
}
