package com.ruskonert.GamblKing.engine;

public enum MessageType
{
    INFO("INFO", "$f"),
    WARNING("WARNING", "$y"),
    ERROR("ERROR", "$r"),
    MESSAGE("MESSAGE", "$w");

    private String value;
    public String getValue() { return this.value; }

    private String colorType;
    public String getColorType() { return this.colorType; }

    MessageType(String value, String colorType)
    {
        this.value = value;
        this.colorType = colorType;
    }
}
