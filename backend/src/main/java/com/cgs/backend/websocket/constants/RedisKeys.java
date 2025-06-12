package com.cgs.backend.websocket.constants;

public final class RedisKeys {
    public static final String GAME_READY_PREFIX = "game_ready:";
    public static final String ONLINE_USER_PREFIX = "online_user:";
    public static final String PENDING_INVITE_PREFIX = "pending_invite:";
    public static final String GAME_ROOM_PREFIX = "game_room:";

    private RedisKeys() {}  //인스턴스화 방지
}
