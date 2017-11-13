package com.ruskonert.GameClient.program.component;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextField;
import com.ruskonert.GameClient.program.ClientProgramManager;
import com.ruskonert.GameEngine.util.ReflectionUtil;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientComponent implements Initializable
{
    public JFXTextField InputID;
    public JFXPasswordField InputPassword;
    public JFXButton LoginButton;
    public JFXButton RegisterButton;
    public JFXProgressBar ProgressBar;
    public Label ProgressLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        try {
            ReflectionUtil.Companion.setStaticField(ClientProgramManager.class, "clientComponent", this);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
