package com.ruskonert.GameServer.program;

import com.ruskonert.GameServer.GameServer;
import com.ruskonert.GameServer.MessageType;
import com.ruskonert.GameServer.framework.GameServerFramework;
import com.ruskonert.GameServer.program.component.ProgramComponent;
import com.ruskonert.GameServer.server.ConsoleSender;
import com.ruskonert.GameServer.server.Server;
import com.ruskonert.GameServer.util.SystemUtil;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class AppFramework
{
    /**
     * 메인 프로그램이 실행되었을 때 실행되는 메소드입니다.
     */
    protected void onEnable()
    {
        Server server = GameServer.getServer();
        server.getConsoleSender().sendMessage("The program was loaded successfully.");
    }

    /**
     * 메인 프로그램이 종료되기 직전에 실행되는 메소드입니다.
     */
    protected void onDisable()
    {
        Server server = GameServer.getServer();
        server.getConsoleSender().sendMessage("The program is shutdowning...");
    }

    @Override protected void finalize() throws Throwable { this.onDisable(); }
    public void start(Stage stage) throws Exception
    {
        this.createLayoutFramework(stage);
        this.onEnableInner();
        this.onEnable();
    }

    private void BindServerConnection() throws NoSuchFieldException, IllegalAccessException
    {
        GameServerFramework.generate();

    }

    protected synchronized void onEnableInner()
    {
        try
        {
            this.BindServerConnection();
        }
        catch (NoSuchFieldException | IllegalAccessException e)
        {
            SystemUtil.Companion.error(e);
            return;
        }

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

        // using console send message field and press enter
        component.getConsoleMessageField().setOnKeyPressed( event -> {
            if(event.getCode() == KeyCode.ENTER)
            {
                String message = component.getConsoleMessageField().getText();
                if(message.isEmpty())
                {
                    sender.sendMessage("Sending message is null or empty.", MessageType.ERROR);
                    return; }
                sender.clearCommandField();
                if(message.startsWith("/"))
                {
                    sender.dispatch(message);
                }
                else
                {
                    sender.sendMessage(message);
                }
            } });
    }

    private void createLayoutFramework(Stage primaryStage) throws Exception
    {
        FXMLLoader root = new FXMLLoader(ProgramManager.getStyleURL("Application.fxml"));
        primaryStage.setTitle(ProgramComponent.PROGRAM_NAME);
        primaryStage.setResizable(false);
        Parent parent = root.load();
        primaryStage.setScene(new Scene(parent, ProgramComponent.PROGRAM_WIDTH, ProgramComponent.PROGRAM_HEIGHT));
        primaryStage.show();
    }
}
