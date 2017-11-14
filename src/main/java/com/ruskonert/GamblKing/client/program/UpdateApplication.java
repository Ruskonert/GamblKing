package com.ruskonert.GamblKing.client.program;

import com.ruskonert.GamblKing.client.connect.ClientUpdateConnection;
import com.ruskonert.GamblKing.client.event.UpdateLayoutEvent;
import com.ruskonert.GamblKing.engine.program.StageBuilder;
import com.ruskonert.Gamblking.util.SystemUtil;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class UpdateApplication extends StageBuilder
{
    private static UpdateApplication updateApplication = null;
    public static UpdateApplication getUpdateApplication() { return updateApplication; }

    private static Stage stage;
    public static Stage getStage() { return stage; }

    public static void close()
    {
        stage.close();
    }

    @Override
    public void start(Stage stage)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(SystemUtil.Companion.getStylePath("style/update.fxml"));
            Parent parent = loader.load();
            stage.setScene(new Scene(parent, 600, 200));
            stage.setTitle("Update");

            stage.setResizable(false);
            stage.initStyle(StageStyle.UNDECORATED);

            updateApplication = this;
            UpdateApplication.stage = stage;

            registerEvent(new UpdateLayoutEvent());

            ClientUpdateConnection packet = new ClientUpdateConnection();
            packet.send();

            stage.show();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
