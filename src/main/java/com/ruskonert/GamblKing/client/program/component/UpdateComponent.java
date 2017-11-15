package com.ruskonert.GamblKing.client.program.component;

import com.ruskonert.GamblKing.client.program.ClientProgramManager;
import com.ruskonert.GamblKing.util.ReflectionUtil;
import com.ruskonert.GamblKing.util.SystemUtil;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.net.URL;
import java.util.ResourceBundle;

public final class UpdateComponent implements Initializable
{
    public ProgressBar UpdateProgressBar;
    public Label UpdateLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        try
        {
            ReflectionUtil.Companion.setStaticField(ClientProgramManager.class, "updateComponent", this);
        }
        catch (NoSuchFieldException | IllegalAccessException e)
        {
            e.printStackTrace();
            SystemUtil.Companion.error(e);
        }
    }
}
