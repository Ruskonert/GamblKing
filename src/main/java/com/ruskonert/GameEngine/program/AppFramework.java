package com.ruskonert.GameEngine.program;

import com.ruskonert.GameEngine.GameServer;
import com.ruskonert.GameEngine.Listener;
import com.ruskonert.GameEngine.framework.GameServerFramework;
import com.ruskonert.GameEngine.program.component.ProgramComponent;
import com.ruskonert.GameEngine.program.event.ConsoleLayoutEvent;
import com.ruskonert.GameEngine.program.event.SettingLayoutEvent;
import com.ruskonert.GameEngine.server.ConsoleSender;
import com.ruskonert.GameEngine.server.Server;
import com.ruskonert.GameEngine.util.SystemUtil;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public abstract class AppFramework
{
    private static Stage ApplictionStage;
    public static Stage getApplictionStage() { return ApplictionStage; }

    /**
     * 메인 프로그램이 실행되었을 때 실행되는 메소드입니다.
     */
    protected void onEnable()
    {

    }

    /**
     * 메인 프로그램이 종료되기 직전에 실행되는 메소드입니다.
     */
    protected void onDisable()
    {

    }

    public void start(Stage stage) throws Exception
    {
        this.createLayoutFramework(stage);
        this.onEnableInner();
        this.onEnable();
    }

    protected void registerEvent(Listener listener)
    {
        listener.register(this);
    }


    private void BindServerConnection() throws NoSuchFieldException, IllegalAccessException
    {
        GameServerFramework.generate();
        Server server = GameServer.getServer();
        ConsoleSender sender = server.getConsoleSender();
        sender.log("Register an default object handler...");

        this.registerEvent(new ConsoleLayoutEvent());
        this.registerEvent(new SettingLayoutEvent());

        sender.log("The object handler was registered.");
    }

    private void onEnableInner()
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

        GameServer.getServer().getConsoleSender().log("The program was loaded successfully.");
    }

    private void onDisableInner()
    {
        Platform.runLater(() -> { onDisable(); System.exit(0); });
    }

    private void createLayoutFramework(Stage primaryStage) throws Exception
    {
        FXMLLoader root = new FXMLLoader(ProgramManager.getStyleURL("Application.fxml"));
        primaryStage.setTitle(ProgramComponent.PROGRAM_NAME);
        primaryStage.setResizable(false);
        Parent parent = root.load();
        primaryStage.setScene(new Scene(parent, ProgramComponent.PROGRAM_WIDTH, ProgramComponent.PROGRAM_HEIGHT));
        
        AppFramework.ApplictionStage = primaryStage;
        primaryStage.show();
    }
}
