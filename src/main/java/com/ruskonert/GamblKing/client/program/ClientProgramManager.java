package com.ruskonert.GamblKing.client.program;

import com.ruskonert.GamblKing.client.program.component.ClientComponent;
import com.ruskonert.GamblKing.client.program.component.SignupComponent;
import com.ruskonert.GamblKing.client.program.component.UpdateComponent;

public class ClientProgramManager
{
    public static SignupComponent signupComponent;

    public static ClientComponent clientComponent;

    public static UpdateComponent updateComponent;

    public static SignupComponent getSignupComponent() { return signupComponent; }
    public static ClientComponent getClientComponent() { return clientComponent; }
}
