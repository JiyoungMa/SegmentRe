package Server.Formats;

public class GetSmallRoomFormat {
    String id;
    Long bigroomId;

    public GetSmallRoomFormat(String id, Long bigroomId){
        this.id = id;
        this.bigroomId = bigroomId;
    }

    public String getId() {
        return id;
    }

    public Long getBigroomId() {
        return bigroomId;
    }
}
