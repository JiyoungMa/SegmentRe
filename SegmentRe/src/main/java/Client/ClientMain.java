package Client;

import org.json.JSONObject;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class ClientMain {
    public static void main(String[] args) {

        Socket socket = null;

        try{
            socket = new Socket();
            socket.connect(new InetSocketAddress(8080));

            InputStream input = socket.getInputStream();
            OutputStream output = socket.getOutputStream();

            JSONObject jsondata = new JSONObject();
            jsondata.put("Method", "Login");

            String data = jsondata.toString();

            output.write(data.getBytes(StandardCharsets.UTF_8));
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            if (socket != null){
                try{
                    socket.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }

    }

    public static void signUp(String Id,String password, String userName){
    }
}
