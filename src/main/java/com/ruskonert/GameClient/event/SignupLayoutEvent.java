package com.ruskonert.GameClient.event;

import com.ruskonert.GameClient.program.ClientProgramManager;
import com.ruskonert.GameClient.program.component.SignupComponent;
import com.ruskonert.GameEngine.event.LayoutListener;
import com.ruskonert.GameEngine.util.SystemUtil;
import javafx.application.Platform;

public class SignupLayoutEvent implements LayoutListener
{
    @Override
    public void register(Object handleInstance)
    {
        SignupComponent signupComponent = ClientProgramManager.getSignupComponent();
        signupComponent.CancelButton.setOnMouseClicked(event -> Platform.exit());

        signupComponent.SignUpButton.setOnMouseClicked(event -> {
            if(signupComponent.TextID.getText().isEmpty())
            {
                signupComponent.ErrorMessage.setText("오류: 아이디가 비어있습니다."); return;
            }
            if(signupComponent.TextNickname.getText().isEmpty())
            {
                signupComponent.ErrorMessage.setText("오류: 닉네임이 비어있습니다."); return;
            }
            if(signupComponent.TextPassword.getText().isEmpty())
            {
                signupComponent.ErrorMessage.setText("오류: 비밀번호가 비어있습니다."); return;
            }
            if(signupComponent.TextPasswordCheck.getText().isEmpty())
            {
                signupComponent.ErrorMessage.setText("오류: 비밀번호 확인 란이 비어있습니다."); return;
            }

            if(signupComponent.TextPasswordCheck.getText().equalsIgnoreCase(
                    signupComponent.TextPassword.getText()))
            {
                SystemUtil.Companion.alert("오류", "Incorrect password", "비밀번호가 일치하지 않습니다. 다시 입력하세요.");return;
            }
        });
    }
}
