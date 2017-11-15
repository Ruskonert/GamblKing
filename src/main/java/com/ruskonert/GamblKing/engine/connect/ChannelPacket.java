package com.ruskonert.GamblKing.engine.connect;

import com.ruskonert.GamblKing.engine.server.Channel;

public abstract class ChannelPacket extends ServerPacket
{
    private Channel channel;
    public Channel getChannel() { return channel; }

    public ChannelPacket()
    {

    }
}
