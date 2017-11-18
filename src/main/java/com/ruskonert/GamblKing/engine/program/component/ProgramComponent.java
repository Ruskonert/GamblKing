package com.ruskonert.GamblKing.engine.program.component;

import com.ruskonert.GamblKing.engine.program.ProgramManager;
import com.ruskonert.GamblKing.util.ReflectionUtil;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import com.ruskonert.GamblKing.engine.program.AppFramework;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * 서버 프로그램을 구성하고 있는 레이아웃 객체입니다.
 * <b>해당 어셈블리 클래스를 수정하지 마세요. 만약 수정했을 경우, 예기치 않은 결과를 야기할 수 있습니다.</b>
 * 이 클래스는 생성자를 사용할 수 없는 정적 클래스입니다. 서버 프레임워크에 의해 자동으로 호출됩니다.
 *
 * @author Ruskonert(Junwon Kim)
 * @see AppFramework
 * @see ProgramManager#getProgramComponent
 */
public final class ProgramComponent implements Initializable
{
    @Deprecated
    public ProgramComponent() {}

    public static String PROGRAM_NAME = "GameServer Management";
    public static int PROGRAM_HEIGHT  = 535;
    public static int PROGRAM_WIDTH   = 788;

    /*
    * MenuBar FXML Module
     */
     /*Menu bar components */
    @FXML private ImageView ConsoleMenuButton;          public ImageView getConsoleMenuButton()    { return this.ConsoleMenuButton; }
    @FXML private ImageView SettingMenuButton;          public ImageView getSettingMenuButton()    { return this.SettingMenuButton; }
    @FXML private ImageView CloseMenuButton;            public ImageView getCloseMenuButton()      { return this.CloseMenuButton;   }

    /*
    * Console Button FXML Module
    */
     /* Console button components */
    @FXML private JFXButton MainClearButton;             public JFXButton getMainClearButton()     { return this.MainClearButton;   }
    @FXML private JFXButton MainStartButton;             public JFXButton getMainStartButton()     { return this.MainStartButton;   }
    @FXML private JFXButton MainCancelButton;            public JFXButton getMainCancelButton()    { return this.MainCancelButton;  }
    @FXML private JFXButton MainExitButton;              public JFXButton getMainExitButton()      { return this.MainExitButton;    }

    /**
     * Console Button FXML Module
     */
     /* Console screen components */
    @FXML private TextArea  ConsoleScreen;               public TextArea getConsoleScreen()        { return this.ConsoleScreen;       }
    @FXML private TextField ConsoleMessageField;         public TextField getConsoleMessageField() { return this.ConsoleMessageField; }
    @FXML private JFXButton ConsoleSendButton;           public JFXButton getConsoleSendButton()   { return this.ConsoleSendButton;   }

    /**
     * Setting Layout FXML Module
     */
     /* Setting components */
    @FXML private JFXToggleButton IsShowingAddress;
    @FXML private JFXButton SaveButton;
    @FXML private JFXButton CancelButton;
    @FXML private JFXToggleButton IsDebugMode;
    @FXML private AnchorPane MainScreen;

    public void initialize(URL location, ResourceBundle resources)
    {
        try
        {
            ReflectionUtil.Companion.setStaticField(ProgramManager.class, "programComponent", this);
        }
        catch (NoSuchFieldException | IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }
}
