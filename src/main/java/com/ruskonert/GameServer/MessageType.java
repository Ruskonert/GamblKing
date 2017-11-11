package com.ruskonert.GameServer;

public enum MessageType
{
    INFO("INFO", "$f"),
    WARNING("WARNING", "$y"),
    ERROR("ERROR", "$r");

    private String value;
    private String colorType;

    MessageType(String value, String colorType)
    {
        this.value = value;
        this.colorType = colorType;
    }
}
