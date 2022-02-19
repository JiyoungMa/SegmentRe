package Server;

public class EnterBigChatRoomFormat {
    String id;
    Long bigChatRoomId;

    public EnterBigChatRoomFormat(String id, Long bigChatRoomId){
        this.id = id;
        this.bigChatRoomId = bigChatRoomId;
    }

    public void setBigChatRoomId(Long bigChatRoomId) {
        this.bigChatRoomId = bigChatRoomId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Long getBigChatRoomId() {
        return bigChatRoomId;
    }
}
