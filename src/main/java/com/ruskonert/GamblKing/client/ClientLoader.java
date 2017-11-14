package com.ruskonert.GamblKing.client;

import com.ruskonert.GamblKing.client.connect.ClientUpdateConnection;
import com.ruskonert.GamblKing.client.connect.ClientConnectionReceiver;
import com.ruskonert.GamblKing.client.event.ClientLayoutEvent;
import com.ruskonert.GamblKing.engine.ProgramInitializable;
import com.ruskonert.GamblKing.engine.event.EventController;
import com.ruskonert.GamblKing.engine.event.EventListener;
import com.ruskonert.GamblKing.engine.event.LayoutListener;
import com.ruskonert.GamblKing.engine.program.Register;
import com.ruskonert.Gamblking.util.SystemUtil;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientLoader extends Application implements ProgramInitializable, Register
{
    private static Stage stage;
    public static Stage getStage() { return stage; }

    private static ClientConnectionReceiver backgroundConnection;
    public static ClientConnectionReceiver getBackgroundConnection() { return backgroundConnection; }

    public static void main(String[] args) { launch(args);}

    public static void setBackgroundConnection(ClientConnectionReceiver backgroundConnection)
    {
        ClientLoader.backgroundConnection = backgroundConnection;
    }

    public static void updateDatabase() { ClientUpdateConnection.update(); }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        FXMLLoader loader = new FXMLLoader(SystemUtil.Companion.getStylePath("style/client_login.fxml"));
        primaryStage.setTitle("Ruskonert Card Game Launcher");
        primaryStage.setScene(new Scene(loader.load(), 600,400));
        this.registerEvent(new ClientLayoutEvent());

        stage = primaryStage;

        primaryStage.show();
    }

    @Override
    public boolean initialize(Object handleInstance)
    {
        return true;
    }

    @Override
    public void registerEvent(LayoutListener listener)
    {
        listener.register(this);
    }

    @Override
    public void registerEvent(EventListener listener)
    {
        EventController.signatureListener(listener);
    }
}