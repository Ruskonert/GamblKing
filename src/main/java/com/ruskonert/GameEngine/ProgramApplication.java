package com.ruskonert.GameEngine;

import com.ruskonert.GameEngine.server.Server;
import com.ruskonert.GameEngine.program.AppFramework;

public class ProgramApplication extends AppFramework
{
    @Override
    protected void onEnable()
    {
        Server server = GameServer.getServer();
        server.getConsoleSender().sendMessage("이제 프로그램을 사용해봅시다.");
    }

    @Override
    protected void onDisable()
    {

    }
}
