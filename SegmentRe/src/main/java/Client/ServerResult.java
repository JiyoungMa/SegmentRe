package Client;

public class ServerResult {
    boolean result;
    String id;
    String error;
    int bigRoomId;

    public ServerResult(){

    }

    public ServerResult(boolean result, String info){
        this.result = result;
        if (result == true){
            this.id = info;
        }
        else{
            this.error = info;
        }
        bigRoomId = -1;
    }

    public ServerResult(boolean result, String info, int bigRoomId){
        this.result = result;
        if (result == true){
            this.id = info;
        }
        else{
            this.error = info;
        }
        bigRoomId = bigRoomId;
    }


    public String getId() {
        return id;
    }

    public boolean getResult(){
        return result;
    }

    public String getError(){
        return error;
    }

    public int getBigRoomId() {
        return bigRoomId;
    }

    public void setId(String id){
        this.id = id;
    }

    public void setResult(boolean result){
        this.result = result;
    }

    public void setError(String error){
        this.error = error;
    }

    public void setBigRoomId(int bigRoomId) {
        this.bigRoomId = bigRoomId;
    }
}
