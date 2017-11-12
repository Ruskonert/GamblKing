package com.ruskonert.GameClient;

import com.ruskonert.GameClient.register.SignupApplication;

import com.ruskonert.GameEngine.util.SystemUtil;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientLoader extends Application
{
    public static void main(String[] args) { launch(args);}
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        FXMLLoader loader = new FXMLLoader(SystemUtil.Companion.getStylePath("style/client_login.fxml"));
        primaryStage.setTitle("Ruskonert's Game");
        primaryStage.setScene(new Scene(loader.load(), 720,480));
        primaryStage.show();

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception
            {
                Platform.runLater(() -> {
                    SignupApplication signup = new SignupApplication();
                    try
                    {
                        signup.start(new Stage());
                    }
                    catch (Exception e)
                    {
                        SystemUtil.Companion.error(e);
                    }
                });
                return null;
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }
}
