package com.ruskonert.GameClient.program;

import com.ruskonert.GameClient.program.component.ClientComponent;
import com.ruskonert.GameClient.program.component.SignupComponent;

public class ClientProgramManager
{
    public static SignupComponent signupComponent;

    public static ClientComponent clientComponent;

    public static SignupComponent getSignupComponent() { return signupComponent; }
    public static ClientComponent getClientComponent() { return clientComponent; }
}
