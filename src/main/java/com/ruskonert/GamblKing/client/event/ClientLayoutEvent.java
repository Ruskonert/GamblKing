package com.ruskonert.GamblKing.client.event;

import com.ruskonert.GamblKing.client.connect.ClientConnectionReceiver;
import com.ruskonert.GamblKing.client.connect.packet.LoginConnectionPacket;
import com.ruskonert.GamblKing.client.program.ClientProgramManager;
import com.ruskonert.GamblKing.client.program.SignupApplication;
import com.ruskonert.GamblKing.client.program.component.ClientComponent;
import com.ruskonert.GamblKing.engine.event.LayoutListener;

import javafx.application.Platform;
import javafx.stage.Stage;

public class ClientLayoutEvent implements LayoutListener
{
    @Override
    public void register(Object handleInstance)
    {
    ClientComponent clientComponent = ClientProgramManager.getClientComponent();
    clientComponent.RegisterButton.setOnMouseClicked(event -> Platform.runLater(() -> new SignupApplication().start(new Stage())));

    clientComponent.LoginButton.setOnMouseClicked(event -> Platform.runLater(() ->
    {
        ClientConnectionReceiver.refreshClientConnection();
        Platform.runLater(() -> { LoginConnectionPacket connection = new LoginConnectionPacket(clientComponent.InputID.getText(),
                clientComponent.InputPassword.getText());connection.send();}); }));
    }
}
