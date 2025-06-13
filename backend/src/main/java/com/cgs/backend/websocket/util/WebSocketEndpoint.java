package com.cgs.backend.websocket.util;

public class WebSocketEndpoint {
    public static final String QUEUE_PREFIX = "/queue/";
    public static final String TOPIC_PREFIX = "/topic/";

    //온라인 유저 목록
    public static String onlineUsers() {
        return TOPIC_PREFIX + "online-users";
    }

    //초대 관련
    public static String userInvite(String userId) {
        return QUEUE_PREFIX + userId + "/invite";
    }
    public static String userInviteResponse(String userId) {
        return QUEUE_PREFIX + userId + "/invite/response";
    }

    //게임 관련
    public static String gameStart(String roomId) {
        return TOPIC_PREFIX + "game/" + roomId + "/start";
    }
    public static String gameInit(String userId) {  //상대방 정보
        return QUEUE_PREFIX + userId + "/game/init";
    }
    public static String gameUpdate(String userId) {  //상대방 정보
        return QUEUE_PREFIX + userId + "/game/update";
    }
    public static String gameScore(String userId) {  //상대방 정보
        return QUEUE_PREFIX + userId + "/game/score";
    }
    public static String gameEnd(String roomId) {
        return TOPIC_PREFIX + "game/" + roomId + "/end";
    }
}
