package com.ruskonert.GamblKing.client.event;

import com.ruskonert.GamblKing.client.connect.ClientConnectionReceiver;
import com.ruskonert.GamblKing.client.connect.packet.LoginConnectionPacket;
import com.ruskonert.GamblKing.client.program.ClientProgramManager;
import com.ruskonert.GamblKing.client.program.SignupApplication;
import com.ruskonert.GamblKing.client.program.component.ClientComponent;
import com.ruskonert.GamblKing.event.LayoutListener;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class ClientLayoutEvent implements LayoutListener
{
    private static Thread stormThread;
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

    clientComponent.FishingButton.setOnMouseClicked(event -> Platform.runLater(() ->
        {
            ImageView view = ClientProgramManager.getClientComponent().StormImage;
            Task<Void> shadowShowing = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    double value = 0.0D;
                    while(value <= 0.3D)
                    {
                        view.setOpacity(value);
                        Thread.sleep(30L);
                        value += 0.01D;
                    }

                    Thread.sleep(2000L);

                    while(value >= 0.0d)
                    {
                        view.setOpacity(value);
                        Thread.sleep(30L);
                        value -= 0.01D;
                    }
                    safetyInterrupt();
                    return null;
                }
            };
            if(stormThread == null) {
                Thread t = new Thread(shadowShowing);
                stormThread = t;
                t.start();
            }
    }));

    }

    private void safetyInterrupt()
    {
        stormThread.interrupt();
        stormThread = null;
    }

}
