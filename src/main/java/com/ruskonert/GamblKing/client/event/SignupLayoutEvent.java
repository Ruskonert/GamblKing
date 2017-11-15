package com.ruskonert.GamblKing.client.event;

import com.ruskonert.GamblKing.client.connect.ClientConnectionReceiver;
import com.ruskonert.GamblKing.client.connect.packet.RegisterConnectionPacket;
import com.ruskonert.GamblKing.client.program.ClientProgramManager;
import com.ruskonert.GamblKing.client.program.SignupApplication;
import com.ruskonert.GamblKing.client.program.component.SignupComponent;
import com.ruskonert.GamblKing.event.LayoutListener;

import javafx.application.Platform;

public class SignupLayoutEvent implements LayoutListener
{
    @Override
    public void register(Object handleInstance)
    {
        SignupComponent signupComponent = ClientProgramManager.getSignupComponent();

        signupComponent.CancelButton.setOnMouseClicked(event -> SignupApplication.getStage().close()  );

        signupComponent.SignUpButton.setOnMouseClicked(event -> {
            signupComponent.ErrorMessage.setVisible(true);
            if (signupComponent.TextID.getText().isEmpty()) {
                signupComponent.ErrorMessage.setText("오류: 아이디가 비어있습니다.");
                return;
            }
            if (signupComponent.TextNickname.getText().isEmpty()) {
                signupComponent.ErrorMessage.setText("오류: 닉네임이 비어있습니다.");
                return;
            }
            if (signupComponent.TextPassword.getText().isEmpty()) {
                signupComponent.ErrorMessage.setText("오류: 비밀번호가 비어있습니다.");
                return;
            }
            if (signupComponent.TextPasswordCheck.getText().isEmpty()) {
                signupComponent.ErrorMessage.setText("오류: 비밀번호 확인 란이 비어있습니다.");
                return;
            }

            if (signupComponent.TextPasswordCheck.getText().length() < 4) {
                signupComponent.ErrorMessage.setText("오류: 비밀번호를 최소 4자리 이상으로 설정해주십시오!");
                return;
            }

            if (!signupComponent.TextPasswordCheck.getText().equals(signupComponent.TextPassword.getText()))
            {
                signupComponent.ErrorMessage.setText("오류: 비밀번호가 일치하지 않습니다.");
                return;
            }
            signupComponent.ErrorMessage.setText("");
            ClientConnectionReceiver.refreshClientConnection();
            Platform.runLater(() -> {
                RegisterConnectionPacket connection = new RegisterConnectionPacket(signupComponent.TextID.getText(), signupComponent.TextNickname.getText(), signupComponent.TextPassword.getText());
                connection.send();
            });
        });
    }
}
