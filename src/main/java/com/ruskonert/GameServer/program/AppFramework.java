package com.ruskonert.GameServer.program;

import com.ruskonert.GameServer.GameServer;
import com.ruskonert.GameServer.MessageType;
import com.ruskonert.GameServer.program.component.ProgramComponent;
import com.ruskonert.GameServer.server.ConsoleSender;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public final class AppFramework
{
    private static Stage AppStage;
    public static Stage getAppStage() { return AppStage; }

    @Override protected void finalize() throws Throwable { this.onDisable(); }

    public void start(Stage stage) throws Exception
    {
        this.createLayoutFramework(stage);
        this.onEnableInner();
        this.onEnable();
    }

    private void BindServerConnection()
    {

    }


    protected synchronized void onEnableInner()
    {
        this.BindServerConnection();
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
            if(component.getConsoleMessageField().getText().isEmpty()) {
                sender.sendMessage("Sending message is null or empty.", MessageType.ERROR); }
        });

        // using console send message field and press enter
        component.getConsoleMessageField().setOnKeyPressed( event -> { if(event.getCode() == KeyCode.ENTER)
        {
            String message = component.getConsoleMessageField().getText();
            if(message.isEmpty())
            {
                sender.sendMessage("Sending message is null or empty.", MessageType.ERROR);
                return; }
            sender.clearCommandField();
            sender.sendMessage(message); }
        });
    }

    protected void onEnable() {}

    protected void onDisable() {}

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
