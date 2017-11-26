package com.ruskonert.GamblKing.engine.framework.server;

import com.ruskonert.GamblKing.MessageType;
import com.ruskonert.GamblKing.engine.assembly.TargetReference;
import com.ruskonert.GamblKing.engine.assembly.TargetBuilder;
import com.ruskonert.GamblKing.engine.connect.ConnectionBackground;
import com.ruskonert.GamblKing.program.ConsoleSender;
import com.ruskonert.GamblKing.engine.server.Server;
import com.ruskonert.GamblKing.entity.Player;

import javafx.beans.property.StringProperty;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import org.jetbrains.annotations.NotNull;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ConsoleSenderFramework extends TargetBuilder<ConsoleSenderFramework> implements ConsoleSender
{
    @TargetReference(target="ProgramComponent", value="ConsoleMessageField")
    private TextField commandField;
    @Override public TextField getCommandField() { return this.commandField; }

    @TargetReference(target="ProgramComponent", value="ConsoleScreen")
    private TextArea consoleScreen;
    @Override public TextArea getConsoleScreen() { return this.consoleScreen; }

    @TargetReference(target="ProgramComponent", value="ConsoleScreen#textProperty")
    private StringProperty messageProperty;
    @Override public StringProperty getMessageProperty() { return this.messageProperty; }

    private Server server;
    public Server getServer() { return this.server; }

    protected ConsoleSenderFramework(Server server)
    {
        this.server = server;
    }

    @Override public final void sendMessage(@NotNull String message){ this.sendMessage(message, MessageType.MESSAGE); }

    @Override public final void sendRawMessage(String message) { this.messageProperty.setValue(this.messageProperty.getValue() + message + "\n"); }

    @Override public synchronized void sendMessage(String message, MessageType type)
    {
        SimpleDateFormat sdf = this.getServer().getDateFormat();
        String builder = "[" +
                sdf.format(new Date()) +
                " " +
                type.getValue() +
                "] " +
                message;

        this.messageProperty.setValue(this.messageProperty.getValue() + builder + "\n");
    }

    @Override public void log(String message)
    {
        this.sendMessage(message, MessageType.INFO);
    }

    @Override public final void clearScreen() { this.consoleScreen.setText(""); }

    @Override public final void clearCommandField() { this.commandField.setText("");}

    /**
     * 콘솔에서 명령어를 실행합니다.
     * @param command 명령어 라벨
     * @param args 명령어 인자값
     */
    private void dispatch(String command, List<String> args)
    {
        //TODO("UP TO DATE");
    }

    /**
     * 이 메소드는 받은 메세지 형식이 명령어 단위인지 메세지인지 판단하여 보냅니다.
     * {@link #dispatch(String, java.util.List)}는 명령어를 실행하는 메소드입니다.
     * @param message 메세지
     */
    public void dispatch(String message)
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

    /**
     * 게임에 접속중인 모든 플레이어에게 메세지를 보냅니다.
     * 이것이 실행되려면
     * @param message 메세지
     */
    @Override
    public void sendAll(String message)
    {
        for(Player p : ConnectionBackground.getGameClientMap().keySet())
        {
            // 지금 게임에 접속중인 플레이어가 게임중일 때는 메인 화면에 메세지를 보내지 않습니다.
            // 왜냐하면 게임 중에는 홈에 있는 채팅 창을 볼 수 없습니다.
            if(p.isEnteredRoom()) continue;
            p.sendMessage(message);
        }
    }

    @Override
    public Object onInit(Object handleInstance)
    {
        super.onInit(this);
        return this;
    }
}
