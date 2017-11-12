package com.ruskonert.GameClient.register;

import com.ruskonert.GameEngine.util.SystemUtil;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SignupApplication
{
    public void start(Stage stage) throws Exception
    {
        FXMLLoader loader = new FXMLLoader(SystemUtil.Companion.getStylePath("style/signup.fxml"));
        stage.setScene(new Scene(loader.load(), 480, 480));
        stage.setTitle("회원가입");
        stage.show();
    }
}
