package com.ruskonert.GamblKing.engine.program;

import com.ruskonert.GamblKing.engine.GameServer;
import com.ruskonert.GamblKing.engine.ProgramApplication;
import com.ruskonert.GamblKing.engine.event.program.ConsoleLayoutEvent;
import com.ruskonert.GamblKing.engine.event.program.SettingLayoutEvent;
import com.ruskonert.GamblKing.engine.framework.server.GameServerFramework;
import com.ruskonert.GamblKing.engine.program.component.ProgramComponent;
import com.ruskonert.GamblKing.engine.server.Server;
import com.ruskonert.GamblKing.program.ConsoleSender;
import com.ruskonert.GamblKing.program.StageBuilder;
import com.ruskonert.GamblKing.util.SystemUtil;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * 어플리케이션을 자동으로 설계하고 각종 프레임워크를 작성하여 서버가 작동할 수 있도록 합니다.<br>
 * 또한 사용자 이벤트 또는 기본 이벤트를 등록하여 서버 이벤트를 제대로 핸들링할 수 있도록 설계하며
 * 이것은 {@link ProgramApplication}에 상속하여 해당 클래스의
 * 인스턴스로부터 작동됩니다. 이 클래스는 생성자를 사용할 수 없습니다.
 *
 * @see ApplicationLoader#initialize(Object)
 * @author Ruskonert (Junwon Kim)
 */
public class AppFramework extends StageBuilder
{
    private static Stage ApplictionStage;
    public static Stage getApplictionStage() { return ApplictionStage; }

    /**
     * 레이아웃을 만들고 패킷 서버를 구축하며 각종 시스템 이벤트 핸들러를 등록합니다.
     * 해당 메소드가 작동되지 않으면 프로그램이 활성화될 수 없습니다.
     * @param stage 프로그램의 레이아웃
     * @throws Exception 프로그램 레이아웃 설계를 실패하거나 프레임워크를 제대로 만들지 못할 경우
     */
    @Override
    public final void start(Stage stage)
    {
        try
        {
            this.createLayoutFramework(stage);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        this.onEnableInner();
        this.onEnable();
    }

    private void createLayoutFramework(Stage primaryStage) throws Exception
    {
        FXMLLoader root = new FXMLLoader(SystemUtil.Companion.getStyleURL("Application.fxml"));
        primaryStage.setTitle(ProgramComponent.PROGRAM_NAME);
        primaryStage.setResizable(false);
        Parent parent = root.load();
        primaryStage.setScene(new Scene(parent, ProgramComponent.PROGRAM_WIDTH, ProgramComponent.PROGRAM_HEIGHT));

        // 이벤트 등록
        // this.registerEvent(new PlayerConnectListener());

        AppFramework.ApplictionStage = primaryStage;
        primaryStage.show();
    }

    private void BindServerConnection() throws NoSuchFieldException, IllegalAccessException
    {
        GameServerFramework.generate();
        Server server = GameServer.getServer();
        ConsoleSender sender = server.getConsoleSender();

        sender.log("Register an default object handlers...");
        this.registerDefaultLayoutEvents();

        sender.log("Register an event listeners...");
        sender.log("The object handler was registered.");
    }


    /**
     * 서버가 모두 준비를 완료했을 때 실행되는 메소드입니다. 해당 메소드를 오버라이딩하여 사용 가능합니다.
     */
    protected void onEnable()
    {

    }

    /**
     * 프로그램을 처음 활성화 했을 때, 가장 먼저 실행되는 메소드입니다.
     */
    private void onEnableInner()
    {
        try
        {
            this.BindServerConnection();
        }
        catch (NoSuchFieldException | IllegalAccessException e)
        {
            SystemUtil.Companion.error(e);
            return;
        }

        GameServer.getServer().getConsoleSender().log("The program was loaded successfully.");
    }

    /**
     * 메인 프로그램이 종료되기 직전에 실행되는 메소드입니다. 해당 메소드를 오버라이딩하여 사용 가능합니다.
     */
    protected void onDisable()
    {

    }

    /**
     * 프로그램을 종료할 때, 가장 먼저 실행되는 메소드입니다.
     */
    private void onDisableInner()
    {
        Platform.runLater(() -> { onDisable(); System.exit(0); });
    }


    /**
     * 미리 만들어 둔 레이아웃 이벤트를 등록합니다.
     */
    private void registerDefaultLayoutEvents()
    {
        this.registerEvent(new ConsoleLayoutEvent());
        this.registerEvent(new SettingLayoutEvent());
        // more events...
    }

    /**
     * 미리 만들어 둔 서버 이벤트를 등록합니다.
     */
    private void registerDefaultEvents() {
        // this.registerEvent(new PlayerConnectListener());
        // more events...
    }
}
