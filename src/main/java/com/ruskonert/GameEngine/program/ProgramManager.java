package com.ruskonert.GameEngine.program;

import com.ruskonert.GameEngine.program.component.PreloadComponent;
import com.ruskonert.GameEngine.program.component.ProgramComponent;

public class ProgramManager
{
    private static PreloadComponent preloadComponent;
    public static PreloadComponent getPreloadComponent() { return preloadComponent; }

    private static ProgramComponent programComponent;
    public static ProgramComponent getProgramComponent() { return programComponent; }
}
