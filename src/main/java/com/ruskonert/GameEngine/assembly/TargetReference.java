package com.ruskonert.GameEngine.assembly;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TargetReference
{
    String target() default "com.ruskonert.GameEngine.program.component";
    String value() default "";
}
