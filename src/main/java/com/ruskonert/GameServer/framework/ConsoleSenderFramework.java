package com.ruskonert.GameServer.framework;

import com.ruskonert.GameServer.TargetReference;
import com.ruskonert.GameServer.GameServer;
import com.ruskonert.GameServer.server.ConsoleSender;
import com.ruskonert.GameServer.MessageType;

import javafx.beans.property.StringProperty;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public final class ConsoleSenderFramework extends TargetBuilder<ConsoleSenderFramework> implements ConsoleSender
{
    @TargetReference(value="ConsoleMessageField")
    private TextField commandField;
    @Override public TextField getCommandField() { return this.commandField; }

    @TargetReference(value="ConsoleScreen")
    private TextArea consoleScreen;
    @Override public TextArea getConsoleScreen() { return this.consoleScreen; }

    @TargetReference(target="", value="property")
    private StringProperty messageProperty;
    public StringProperty getMessageProperty() { return this.messageProperty; }

    @Override public final void sendMessage(@NotNull String message){ this.sendMessage(message, MessageType.INFO); }

    @Override public final void sendRawMessage(String message) { this.messageProperty.setValue(this.messageProperty.getValue() + message + "\n"); }

    @Override public void sendMessage(String message, MessageType type)
    {
        SimpleDateFormat sdf = GameServer.getServer().getDateFormat();
        StringBuilder builder = new StringBuilder(sdf.format(new Date()));

        //new SimpleDateFormat("[yyyy-MM-dd kk:mm:ss").format(new Date()) +
    }

    @Override public final void clearScreen() { this.consoleScreen.setText(""); }

    @Override public final void clearCommandField() { this.commandField.setText("");}

    public void dispatch(String command, List<String> args)
    {
        //TODO("UP TO DATE");
    }

    public void messageDispatch(String message)
    {
        String command = this.commandField.getText();
        if(command.startsWith("/"))
        {
            command = command.substring(1, command.length() - 1);
            String[] split = command.split(" ");
            List<String> args = Arrays.asList(split);
            args.remove(0);
            this.dispatch(split[0], args);
        }
        else
        {
            this.sendMessage(message, MessageType.INFO);
        }
    }
}