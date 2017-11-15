package com.ruskonert.GamblKing.engine.assembly;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TargetReference
{
    String target() default "com.ruskonert.GamblKing.engine.program.component";
    String value() default "";
}
