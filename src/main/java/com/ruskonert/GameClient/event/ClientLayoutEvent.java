package com.ruskonert.GameClient.event;

import com.ruskonert.GameClient.program.ClientProgramManager;
import com.ruskonert.GameClient.program.SignupApplication;
import com.ruskonert.GameClient.program.component.ClientComponent;
import com.ruskonert.GameEngine.event.LayoutListener;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientLayoutEvent implements LayoutListener
{
    @Override
    public void register(Object handleInstance)
    {
    ClientComponent clientComponent = ClientProgramManager.getClientComponent();
    clientComponent.RegisterButton.setOnMouseClicked(event -> Platform.runLater(() -> {
        SignupApplication application = null;
            SignupApplication.start();
    }));
}
}
