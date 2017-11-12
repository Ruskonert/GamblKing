package com.ruskonert.GameEngine.program;

import com.ruskonert.GameEngine.GameServer;
import com.ruskonert.GameEngine.MessageType;
import com.ruskonert.GameEngine.framework.GameServerFramework;
import com.ruskonert.GameEngine.server.ConsoleSender;
import com.ruskonert.GameEngine.server.Server;
import com.ruskonert.GameEngine.util.SystemUtil;

import com.ruskonert.GameEngine.program.component.ProgramComponent;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
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

    private void BindServerConnection() throws NoSuchFieldException, IllegalAccessException
    {
        GameServerFramework.generate();
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

        Server server = GameServer.getServer();
        ProgramComponent component = ProgramManager.getProgramComponent();
        ConsoleSender sender = server.getConsoleSender();
        sender.log("Register an object handler...");

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

        component.getMainExitButton().setOnMouseClicked( event -> {
            ApplictionStage.setOnCloseRequest(e -> Platform.exit());
            onDisableInner();
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
        sender.log("The object handler was registered.");
        sender.log("The program was loaded successfully.");
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
