package com.ruskonert.GamblKing.client.connect.packet;

import com.google.gson.Gson;
import com.ruskonert.GamblKing.util.SystemUtil;

import java.io.File;
import java.util.HashMap;

public class ClientRequestedPacket extends ClientPacket
{
    private String data;

    public HashMap<String, String> getData() {
        return new Gson().fromJson(SystemUtil.Companion.fixHashMap(data), HashMap.class);
    }

    public ClientRequestedPacket(int statusNumber)
    {
        super(statusNumber);
    }
}
