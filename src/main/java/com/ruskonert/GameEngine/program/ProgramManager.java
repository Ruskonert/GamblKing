package com.ruskonert.GameEngine.program;

import com.ruskonert.GameEngine.util.SystemUtil;

import com.ruskonert.GameEngine.program.component.PreloadComponent;
import com.ruskonert.GameEngine.program.component.ProgramComponent;

import java.net.URL;

public class ProgramManager
{
    public static URL getStyleURL(String filename) { return SystemUtil.Companion.getStylePath("style/" + filename); }

    public static URL getLayout(Class<?> clazz) { return getStyleURL(clazz.getSimpleName() + ".fxml"); }

    private static PreloadComponent preloadComponent;
    public static PreloadComponent getPreloadComponent() { return preloadComponent; }

    private static ProgramComponent programComponent;
    public static ProgramComponent getProgramComponent() { return programComponent; }
}
