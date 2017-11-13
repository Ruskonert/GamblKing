package com.ruskonert.GameClient.program;

import com.ruskonert.GameEngine.util.SystemUtil;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SignupApplication
{
    private static Stage ApplictionStage;
    public static Stage getApplicationStage() { return ApplictionStage; }

    public SignupApplication(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(SystemUtil.Companion.getStyleURL("signup.fxml"));
        Parent parent = loader.load();
        stage.setScene(new Scene(parent, 480, 415));
        stage.setTitle("Register");
        stage.setResizable(true);
        ApplictionStage = stage;
    }

    public static void start()
    {
        ApplictionStage.show();
    }


}
