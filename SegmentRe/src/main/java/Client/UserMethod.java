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
    private static String myid = null;
    private static int bigroom = -1;

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
            myid = serverResult.getId();
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
                if (results.has("BigChatRoomId")){
                    serverResult = new ServerResult(true,results.get("Id").toString(), Integer.parseInt(results.get("BigChatRoomId").toString()));
                }else {
                    serverResult = new ServerResult(true, results.get("Id").toString());
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

    public ServerResult enterBigChatRoom() {
        ServerResult serverResult = new ServerResult();
        JSONObject getRoomsJson = new JSONObject();
        Map<Integer,String> bigrooms = new HashMap<>();
        getRoomsJson.put("Method", "GetBigRooms");
        try{
            sendMessageToServer(getRoomsJson);
            JSONObject bigRooms = getMessageFromServer();
            if (bigRooms.get("Result").toString().equals("Success")){
                System.out.println("------------------------------------");
                System.out.println("<큰 채팅방 목록>");
                JSONObject roomList = bigRooms.getJSONObject("data");
                List roomIds = roomList.getJSONArray("RoomId").toList();
                for(int i = 0; i<roomIds.size();i++){
                    String now_room = roomIds.get(i).toString();
                    String roomName = roomList.get(now_room).toString();
                    bigrooms.put(Integer.getInteger(now_room),roomName);
                    System.out.format("번호 : %s  이름 : %s\n",now_room,roomName);
                }
                System.out.println("------------------------------------");
                System.out.print("입장할 채팅방의 번호를 입력하세요 : ");
                int roomId = scanner.nextInt();
                scanner.nextLine();

                JSONObject bigRoomJson = new JSONObject();
                bigRoomJson.put("Method","Enter BigChatRoom");

                JSONObject data = new JSONObject();
                data.put("Id", myid);
                data.put("BigChatRoomId", roomId);

                bigRoomJson.put("data",data);

                serverResult = getServerResult(bigRoomJson);
                if (serverResult.getResult() == true) {
                    bigroom = serverResult.getBigRoomId();
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
