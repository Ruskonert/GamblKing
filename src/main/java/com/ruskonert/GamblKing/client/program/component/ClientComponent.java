package com.ruskonert.GamblKing.client.program.component;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextField;
import com.ruskonert.GamblKing.client.program.ClientProgramManager;
import com.ruskonert.GamblKing.util.ReflectionUtil;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public final class ClientComponent implements Initializable
{
    public JFXTextField InputID;
    public JFXPasswordField InputPassword;
    public JFXButton LoginButton;
    public JFXButton RegisterButton;
    public JFXProgressBar ProgressBar;
    public Label ProgressLabel;
    
    public ImageView StormImage;
    public JFXButton FishingButton;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        this.InputID.setStyle("-fx-text-inner-color: white;");
        this.InputPassword.setStyle("-fx-text-inner-color: white;");

        try
        {
            ReflectionUtil.Companion.setStaticField(ClientProgramManager.class, "clientComponent", this);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
