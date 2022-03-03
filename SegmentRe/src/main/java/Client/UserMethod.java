package Client;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class UserMethod {
    private static BufferedReader input;
    private static BufferedWriter output;
    private static Scanner scanner;
    private static Socket socket;
    private static String my_id = null;
    private static int bigroom_id = -1;
    private static int smallroom_id = -1;

    public UserMethod(Socket socket){
        scanner = new Scanner(System.in);

        this.socket = socket;
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public ServerResult signUp() {
        //password 암호화 나중에 추가해보기
        //While True 말고 비동기로 진행할수는 없을까?

        System.out.println("------------------------------------");
        System.out.print("사용하고 싶은 아이디를 입력하세요 : ");
        String id = scanner.nextLine();

        System.out.print("사용하고 싶은 비밀번호를 입력하세요 : ");
        String password = scanner.nextLine();

        System.out.print("사용하고 싶은 닉네임을 입력하세요 : ");
        String username = scanner.nextLine();

        JSONObject signUpjson = new JSONObject();
        signUpjson.put("Method", "SignUp");

        JSONObject datajson = new JSONObject();
        datajson.put("Id", id);
        datajson.put("password", password);
        datajson.put("username", username);

        signUpjson.put("data", datajson);

        return getServerResult(signUpjson);

    }

    public ServerResult login() {
        System.out.println("------------------------------------");
        System.out.print("아이디를 입력하세요 : ");
        String id = scanner.nextLine();

        System.out.print("비밀번호를 입력하세요 : ");
        String password = scanner.nextLine();

        JSONObject loginJson = new JSONObject();
        loginJson.put("Method", "Login");

        JSONObject datajson = new JSONObject();
        datajson.put("Id", id);
        datajson.put("password",password);

        loginJson.put("data",datajson);

        ServerResult serverResult= getServerResult(loginJson);
        if (serverResult.getResult() == true) {
            my_id = serverResult.getId();
        }
        return serverResult;
    }

    private ServerResult getServerResult(JSONObject loginJson) {
        ServerResult serverResult = new ServerResult();
        try{
            sendMessageToServer(loginJson);
            JSONObject results = getMessageFromServer();
            String result = results.get("Result").toString();

            if (result.equals("Success")){
                serverResult.setResult(true);
                serverResult.setId(results.get("Id").toString());

                if (results.has("BigChatRoomId")){
                    serverResult.setBigRoomId(Integer.parseInt(results.get("BigChatRoomId").toString()));
                }

                if (results.has("SmallChatRoomId")){
                    serverResult.setSmallRoomId(Integer.parseInt(results.get("SmallChatRoomId").toString()));
                }

            }else{
                serverResult = new ServerResult(false,results.get("Error").toString());
            }
        }catch(IOException e){
            e.printStackTrace();
        }finally {
            return serverResult;
        }
    }

    private JSONObject getMessageFromServer() throws IOException {
        String resultData = input.readLine();
        JSONObject results = new JSONObject(resultData);
        return results;
    }

    private void sendMessageToServer(JSONObject loginJson) throws IOException {
        output.write(loginJson.toString());
        output.newLine();
        output.flush();
    }

    public ServerResult logout() {
        System.out.println("------------------------------------");
        System.out.println("로그아웃을 진행하겠습니다.");
        System.out.print("아이디를 입력하세요 : ");
        String id = scanner.nextLine();

        JSONObject logoutJson = new JSONObject();
        logoutJson.put("Method", "Logout");

        JSONObject datajson = new JSONObject();
        datajson.put("Id", id);

        logoutJson.put("data",datajson);

        return getServerResult(logoutJson);
    }

    public Map<Integer,String> getChatRoom(boolean room){//true가 big
        JSONObject getRoomsJson = new JSONObject();
        Map<Integer,String> bigrooms = new HashMap<>();
        try {
            if(room){
                getRoomsJson.put("Method","GetBigRooms");
            }else{
                getRoomsJson.put("Method","GetSmallRooms");
                JSONObject data = new JSONObject();
                data.put("Id", my_id);
                data.put("BigRoomId",bigroom_id);
                getRoomsJson.put("data",data);
            }
            sendMessageToServer(getRoomsJson);
            JSONObject bigRooms = getMessageFromServer();
            if (bigRooms.get("Result").toString().equals("Success")) {
                System.out.println("------------------------------------");
                if(room){
                    System.out.println("<큰 채팅방 목록>");
                }else{
                    System.out.println("<작은 채팅방 목록>");
                }
                JSONObject roomList = bigRooms.getJSONObject("data");
                List roomIds = roomList.getJSONArray("RoomId").toList();
                for (int i = 0; i < roomIds.size(); i++) {
                    String now_room = roomIds.get(i).toString();
                    String roomName = roomList.get(now_room).toString();
                    bigrooms.put(Integer.getInteger(now_room), roomName);
                    System.out.format("번호 : %s  이름 : %s\n", now_room, roomName);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            return bigrooms;
        }
    }

    public ServerResult enterChatRoom(boolean room) {
        ServerResult serverResult = new ServerResult();
        try{
            Map<Integer, String> rooms = getChatRoom(room);
            if (!rooms.isEmpty()){
                int roomId = -1;
                while(true) {
                    System.out.println("------------------------------------");
                    System.out.print("입장할 채팅방의 번호를 입력하세요 : ");
                    roomId = scanner.nextInt();
                    scanner.nextLine();
                    if (rooms.get(roomId) != null){
                        break;
                    }else{
                        System.out.println("존재하는 채팅방의 번호를 입력하세요");
                    }
                }

                JSONObject roomJson = new JSONObject();
                if(room) {
                    roomJson.put("Method", "Enter BigChatRoom");

                    JSONObject data = new JSONObject();
                    data.put("Id", my_id);
                    data.put("BigChatRoomId", roomId);

                    roomJson.put("data", data);
                }
                else{
                    roomJson.put("Method", "Enter SmallChatRoom");

                    JSONObject data = new JSONObject();
                    data.put("Id", my_id);
                    data.put("SmallChatRoomId", roomId);

                    roomJson.put("data", data);
                }
                serverResult = getServerResult(roomJson);
                if (serverResult.getResult() == true) {
                    if(room) {
                        bigroom_id = serverResult.getBigRoomId();
                    }else{
                        smallroom_id = serverResult.getSmallRoomId();
                    }
                }
            }
        }catch(Exception e) {
            e.printStackTrace();
            return null;
        }finally {
            return serverResult;
        }
    }
}
