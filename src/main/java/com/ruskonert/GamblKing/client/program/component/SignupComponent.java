package com.ruskonert.GamblKing.client.program.component;

import com.ruskonert.GamblKing.client.program.ClientProgramManager;
import com.ruskonert.GamblKing.util.ReflectionUtil;
import com.ruskonert.GamblKing.util.SystemUtil;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * 클라이언트를 구성하고 있는 레이아웃 객체입니다.
 * <b>해당 어셈블리 클래스를 수정하지 마세요. 만약 수정했을 경우, 예기치 않은 결과를 야기할 수 있습니다.</b>
 * 이 클래스는 생성자를 사용할 수 없는 정적 클래스입니다. 서버 프레임워크에 의해 자동으로 호출됩니다.
 *
 * @author Ruskonert(Junwon Kim)
 */
public final class SignupComponent implements Initializable
{
    //sign up components
    public JFXButton SignUpButton;
    public JFXButton CancelButton;

    public JFXTextField TextID;
    public JFXPasswordField TextPassword;
    public JFXTextField TextNickname;
    public JFXPasswordField TextPasswordCheck;
    public Label ErrorMessage;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        try
        {
            ReflectionUtil.Companion.setStaticField(ClientProgramManager.class, "signupComponent", this);
        }
        catch (NoSuchFieldException | IllegalAccessException e)
        {
            SystemUtil.Companion.error(e);
        }
    }
}
