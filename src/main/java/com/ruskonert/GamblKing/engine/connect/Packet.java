package com.ruskonert.GamblKing.engine.connect;

import com.google.gson.Gson;
import com.ruskonert.GamblKing.client.connect.ClientConnectionReceiver;
import com.ruskonert.Gamblking.util.SystemUtil;
import javafx.scene.control.Alert;

public abstract class Packet
{
    private int status;
    public int getStatus() { return this.status; }

    public Packet(int status)
    {
        this.status = status;
    }

    public void send(ClientConnectionReceiver clientSocket, Object handleInstance)
    {
        Gson gson = new Gson();
        try
        {
            clientSocket.send(gson.toJson(handleInstance));
        }
        catch(NullPointerException e)
        {
            SystemUtil.Companion.alert("이벤트 콜링 실패", "연결 끊김",
                    "서버가 닫혀있거나 연결되지 연결되지 않았습니다.", Alert.AlertType.ERROR);
        }
    }

}
