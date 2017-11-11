package com.ruskonert.GameServer;

public @interface TargetReference
{
    String target() default "com.ruskonert.GameServer.program.component";
    String value() default "";
}
