package com.ruskonert.GameServer.program;

import com.ruskonert.GameServer.program.component.PreloadComponent;
import com.ruskonert.GameServer.program.component.ProgramComponent;
import com.ruskonert.GameServer.util.SystemUtil;

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
