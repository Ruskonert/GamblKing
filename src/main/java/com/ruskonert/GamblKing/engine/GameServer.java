package com.ruskonert.GamblKing.engine;

import com.ruskonert.GamblKing.engine.entity.OfflinePlayer;
import com.ruskonert.GamblKing.engine.server.Channel;
import com.ruskonert.GamblKing.program.ConsoleSender;
import com.ruskonert.GamblKing.engine.server.Server;
import com.ruskonert.GamblKing.engine.program.AppFramework;
import com.ruskonert.GamblKing.entity.Player;

import java.util.Collection;
import java.util.Map;

/**
 * 게임 서버와 관련한 정보를 가져오거나 확인할 수 있습니다. 외부 클래스에서 이 서버의 정보를 얻어오고자 할 때,
 * 해당 클래스에 구현되어 있는 정적 메소드로 값을 호출할 수 있습니다.
 * 이 클래스는 정적 클래스입니다. 생성자를 허용하지 않습니다.
 * @author Ruskonert(Junwon Kim)
 * @version 1.0.0
 */
public final class GameServer
{
    /**
     * 생성자를 허용하지 않습니다.
     * 이 클래스에 대한 정보는 {@link AppFramework}에 의해 자동으로 로드됩니다.
     */
    private GameServer() {}

    /**
     * 서버의 프레임워크입니다. 서버가 활성화될 때, 이 필드에 서버 정보가 입력됩니다.
     * {@link #getServer()}를 이용해 게임 서버와 관련한 정보를 로드할 수 있습니다.
     */
    private static Server server;

    /**
     * 서버 정보를 담은 프레임워크를 가져옵니다.
     * @return 서버 프레임워크
     */
    public static Server getServer() { return server; }

    /**
     * 모든 플레이어의 정보를 가져옵니다.
     * @return 모든 플레이어의 정보
     */
    public static Collection<? extends Player> getPlayers() { return server.getPlayers(); }

    /**
     * 아이디로부터 플레이어의 정보를 가져옵니다.
     * @param id 특정 아이디
     * @return 특정 아이디를 포함하고 있는 플레이어 정보
     */
    public static Player getPlayer(String id) { return server.getPlayer(id); }

    /**
     * 접속하지 않은 모든 플레이어의 정보를 가져옵니다.
     * @return
     */
    public static Collection<? extends OfflinePlayer> getOfflinePlayer() { return server.getOfflinePlayer(); }

    /**
     * 현재 서버에 유효하는 채널의 정보를 가져옵니다.
     * @param channelName
     * @return
     */
    public static Channel getChannel(String channelName) { return  server.getChannel(channelName); }

    /**
     * 서버의 콘솔 전송기를 가져옵니다.
     * @return
     */
    public static ConsoleSender getConsoleSender() { return server.getConsoleSender(); }

    /**
     * 서버의 존재하는 모든 채널을 가져옵니다.
     * @return
     */
    public static Map<String, Channel> getChannels() { return server.getChannels(); }
}