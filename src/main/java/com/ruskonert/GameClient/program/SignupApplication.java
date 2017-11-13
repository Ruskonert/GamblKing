package com.ruskonert.GameClient.program;

import com.ruskonert.GameClient.event.SignupLayoutEvent;
import com.ruskonert.GameEngine.event.LayoutListener;
import com.ruskonert.GameEngine.util.SystemUtil;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SignupApplication
{
    private static SignupApplication signupApplication = null;
    public static SignupApplication getSignupApplication() { return signupApplication; }

    private static  Stage stage;
    public static Stage getStage() { return stage; }

    public void start(Stage stage)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(SystemUtil.Companion.getStylePath("style/signup.fxml"));
            Parent parent = loader.load();
            stage.setScene(new Scene(parent, 480, 480));
            stage.setTitle("회원가입");
            stage.setResizable(false);
            stage.initStyle(StageStyle.UNDECORATED);
            SignupApplication.stage = stage;
            signupApplication = this;
            this.registerEvent(new SignupLayoutEvent());

            stage.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void close()
    {
        stage.close();
    }

    public void registerEvent(LayoutListener listener)
    {
        listener.register(this);
    }
}
