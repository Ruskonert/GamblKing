package com.ruskonert.GameClient.event;

import com.ruskonert.GameClient.program.ClientProgramManager;
import com.ruskonert.GameClient.program.SignupApplication;
import com.ruskonert.GameClient.program.component.ClientComponent;
import com.ruskonert.GameEngine.GameServer;
import com.ruskonert.GameEngine.entity.Player;
import com.ruskonert.GameEngine.event.LayoutListener;
import com.ruskonert.GameEngine.event.connect.PlayerLoginAttemptEvent;
import javafx.application.Platform;
import javafx.stage.Stage;

public class ClientLayoutEvent implements LayoutListener
{
    @Override
    public void register(Object handleInstance)
    {
    ClientComponent clientComponent = ClientProgramManager.getClientComponent();
    clientComponent.RegisterButton.setOnMouseClicked(event -> Platform.runLater(() -> {
        SignupApplication application = null;
            new SignupApplication().start(new Stage());
    }));
    clientComponent.LoginButton.setOnMouseClicked(event -> Platform.runLater(() ->
    {
        Player player = GameServer.getPlayer(clientComponent.InputID.getText());
        PlayerLoginAttemptEvent playerEvent = new PlayerLoginAttemptEvent(player);
        playerEvent.start();
    }));
    }
}
