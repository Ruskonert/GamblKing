package com.ruskonert.GameEngine.connect;

import java.io.IOException;

public class BindConnection
{
    private ConnectionBackground background = new ConnectionBackground();
    public ConnectionBackground getBackgroundTask() { return this.background; }

    public BindConnection() throws IOException
    {
    }
}