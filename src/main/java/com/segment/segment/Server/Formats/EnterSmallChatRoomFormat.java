package Server.Formats;

public class EnterSmallChatRoomFormat {
    String id;
    Long smallChatRoomId;

    public EnterSmallChatRoomFormat(String id, Long smallChatRoomId){
        this.id = id;
        this.smallChatRoomId = smallChatRoomId;
    }

    public void setBigChatRoomId(Long bigChatRoomId) {
        this.smallChatRoomId = bigChatRoomId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Long getBigChatRoomId() {
        return smallChatRoomId;
    }
}
