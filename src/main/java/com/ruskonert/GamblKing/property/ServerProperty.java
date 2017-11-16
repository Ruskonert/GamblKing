package com.ruskonert.GamblKing.property;

import com.ruskonert.GamblKing.connect.Packet;
import com.ruskonert.GamblKing.engine.connect.ServerConnectionReceiver;

/**
 * 서버에서 사용되는 시스템 값을 정의한 클래스입니다
 * 또한 서버와 클라이언트가 특정 패킷을 이용할 때, 그 패킷의 규격을 번호로 나타냅니다.
 * 이것을 통해 패킷의 규격은 {@link Packet#status}으로
 * 결정할 수 있습니다.
 *
 * 이 클래스는 시스템 값을 구성하는 어셈블리 클래스이며, 임의로 수정할 경우 심각한 오류가 발생할 수 있습니다.
 *
 * @author Ruskonert(Junwon Kim)
 * @see ServerConnectionReceiver
 */
public final class ServerProperty
{
    public static final int REGISTER_SUCCESSED_ACCOUNT = 504;
    public static final int REGISTER_FAILED_ACCOUNT    = 505;

    public static final String SERVER_ADDRESS          = "localhost";
    public static final int    SERVER_PORT             = 7743;


    public static final int CHECK_REGISTER_CONNECTION  = 440;
    public static final int CLIENT_SERVER_REQUEST      = 614;

    public static final int REQUEST_LOGIN              = 100;
    public static final int RECEVIED_LOGIN_SUCCESS     = 101;
    public static final int RECEIVED_LOGIN_FAILED      = 102;

    public static final int CHECK_UPDATE_REQUEST       = 600;
    public static final int CHECK_UPDATE_PROCESSING    = 601;
    public static final int CHECK_UPDATE_FINISHED      = 602;

    public static final int SEND_UPDATE_REQURST        = 700;
    public static final int SEND_UPDATE_REQURST_RECEIVED = 701;

    public static final int SEND_UPDATE_FILE_REQUEST   = 702;

    public static final int SIGNAL_FILE_UPLOADED       = 800;
}
