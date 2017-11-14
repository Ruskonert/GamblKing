package com.ruskonert.GamblKing.engine.program.component;

import com.ruskonert.GamblKing.engine.program.ProgramManager;
import com.ruskonert.Gamblking.util.ReflectionUtil;

import com.ruskonert.GamblKing.engine.program.AppFramework;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * 서버 프로그램을 구성하고 있는 레이아웃 객체입니다.
 * <b>해당 어셈블리 클래스를 수정하지 마세요. 만약 수정했을 경우, 예기치 않은 결과를 야기할 수 있습니다.</b>
 * 이 클래스는 생성자를 사용할 수 없는 정적 클래스입니다. 서버 프레임워크에 의해 자동으로 호출됩니다.
 *
 * @author Ruskonert(Junwon Kim)
 * @see AppFramework
 * @see ProgramManager#getPreloadComponent
 */
public final class PreloadComponent implements Initializable
{
    @Deprecated
    public PreloadComponent() {}

    @FXML private ProgressBar LoadingProgressBar;       public ProgressBar getLoadingProgressBar() { return LoadingProgressBar; }
    @FXML private Label StatusLabel;                    public Label getStatusLabel()              { return StatusLabel; }

    public void initialize(URL location, ResourceBundle resources)
    {
        try
        {
            ReflectionUtil.Companion.setStaticField(ProgramManager.class, "preloadComponent", this);
        }
        catch (NoSuchFieldException | IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }
}
