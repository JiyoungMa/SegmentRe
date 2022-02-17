package Client;

public class ServerResult {
    boolean result;
    String id;
    String error;

    public ServerResult(){

    }

    public ServerResult(boolean result, String info){
        if (result == true){
            this.id = info;
        }
        else{
            this.error = info;
        }
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
}
