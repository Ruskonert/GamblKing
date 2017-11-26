package com.ruskonert.GamblKing.engine.framework.entity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ruskonert.GamblKing.engine.GameServer;
import com.ruskonert.GamblKing.engine.connect.packet.PlayerMessagePacket;
import com.ruskonert.GamblKing.entity.Player;
import com.ruskonert.GamblKing.framework.PlayerEntityFramework;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class PlayerFramework extends PlayerEntityFramework implements Player
{
    /**
     * 플레이어에게 메세지를 보냅니다.
     *  클라이언트에 채팅 메세지 박스에 접근해야 하므로 패킷을 보냅니다.
     * @param message
     */
    @Override
    public void sendMessage(String message)
    {
        PlayerMessagePacket packet = new PlayerMessagePacket(this, message);
        packet.send();
    }

    private PlayerFramework() { }

    public static Player register(String id, String nickname, String password)
    {
        // 새로운 프레임워크를 작성합니다. 즉, 플레이어를 만듭니다.
        PlayerFramework framework = new PlayerFramework();
        framework.setId(id);
        framework.setNickname(nickname);
        framework.setPassword(password);
        framework.setVictory(0);
        framework.setDefeat(0);
        framework.setLastConnected("정보없음");

        Gson gson = new GsonBuilder().serializeNulls().create();
        String jsonMessage = gson.toJson(framework);
        File dataFolder = GameServer.getServer().getDataFolder();
        File playerFile = new File(dataFolder, "/player/" + framework.getId() + ".json");
        try
        {
            File playerFolder = new File(dataFolder, "/player");
            if(! playerFolder.exists()) playerFolder.mkdir();
            if(playerFile.createNewFile())
            {
                GameServer.getConsoleSender().log("The user id: " + framework.getId() + " was created.");
            }
            else
            {
                GameServer.getConsoleSender().log(framework.getNickname() + " was failed creating. Please check your directory.");
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(playerFile));
            writer.append(jsonMessage);
            writer.flush();
            writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return framework;
    }
}
