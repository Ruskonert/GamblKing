package com.ruskonert.GameEngine.program;

import com.ruskonert.GameEngine.GameServer;
import com.ruskonert.GameEngine.event.EventController;
import com.ruskonert.GameEngine.event.EventListener;
import com.ruskonert.GameEngine.event.LayoutListener;
import com.ruskonert.GameEngine.event.program.ConsoleLayoutEvent;
import com.ruskonert.GameEngine.event.program.SettingLayoutEvent;
import com.ruskonert.GameEngine.framework.GameServerFramework;
import com.ruskonert.GameEngine.listener.PlayerConnectListener;
import com.ruskonert.GameEngine.program.component.ProgramComponent;
import com.ruskonert.GameEngine.server.ConsoleSender;
import com.ruskonert.GameEngine.server.Server;
import com.ruskonert.GameEngine.util.SystemUtil;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * 어플리케이션을 자동으로 설계하고 각종 프레임워크를 작성하여 서버가 작동할 수 있도록 합니다.<br>
 * 또한 사용자 이벤트 또는 기본 이벤트를 등록하여 서버 이벤트를 제대로 핸들링할 수 있도록 설계하며
 * 이것은 {@link com.ruskonert.GameEngine.ProgramApplication}에 상속하여 해당 클래스의
 * 인스턴스로부터 작동됩니다. 이 클래스는 생성자를 사용할 수 없습니다.
 *
 * @see com.ruskonert.GameEngine.program.ApplicationLoader#initialize(Object)
 * @author Ruskonert (Junwon Kim)
 */
public abstract class AppFramework
{
    private static Stage ApplictionStage;
    public static Stage getApplictionStage() { return ApplictionStage; }

    /**
     * 레이아웃을 만들고 패킷 서버를 구축하며 각종 시스템 이벤트 핸들러를 등록합니다.
     * 해당 메소드가 작동되지 않으면 프로그램이 활성화될 수 없습니다.
     * @param stage 프로그램의 레이아웃
     * @throws Exception 프로그램 레이아웃 설계를 실패하거나 프레임워크를 제대로 만들지 못할 경우
     */
    public final void start(Stage stage) throws Exception
    {
        this.createLayoutFramework(stage);
        this.onEnableInner();
        this.onEnable();
    }

    private void createLayoutFramework(Stage primaryStage) throws Exception
    {
        FXMLLoader root = new FXMLLoader(ProgramManager.getStyleURL("Application.fxml"));
        primaryStage.setTitle(ProgramComponent.PROGRAM_NAME);
        primaryStage.setResizable(false);
        Parent parent = root.load();
        primaryStage.setScene(new Scene(parent, ProgramComponent.PROGRAM_WIDTH, ProgramComponent.PROGRAM_HEIGHT));

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
        this.registerDefaultEvents();

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
     * 레이아웃 이벤트를 등록합니다.
     */
    protected void registerEvent(LayoutListener listener)
    {
        listener.register(this);
    }

    /**
     * 서버 이벤트를 등록합니다.
     */
    protected final void registerEvent(EventListener listener) { EventController.signatureListener(listener); }

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
    private void registerDefaultEvents()
    {
        this.registerEvent(new PlayerConnectListener());
        // more events...
    }
}
