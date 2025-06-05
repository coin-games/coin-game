package com.cgs.backend.websocket.util;

public class WebSocketEndpoint {
    public static final String APP_PREFIX = "/app";
    public static final String QUEUE_PREFIX = "/queue/";
    public static final String TOPIC_PREFIX = "/topic/";
    public static final String ONLINE_USERS = TOPIC_PREFIX + "online-users";

    public static final String GAME_INVITE = APP_PREFIX + "/game/invite";
    public static final String GAME_INVITE_RESPONSE = APP_PREFIX + "/game/invite/response";


    public static String userInvite(String userId) {
        return QUEUE_PREFIX + userId + "/invite";
    }

    public static String userInviteResponse(String userId) {
        return QUEUE_PREFIX + userId + "/invite-response";
    }

    public static String userInviteFail(String userId) {
        return QUEUE_PREFIX + userId + "/invite-fail";
    }
}
