package Client;

import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class UserMethod {
    private static BufferedReader input;
    private static BufferedWriter output;
    private static Scanner scanner;
    private static Socket socket;

    public UserMethod(Socket socket){
        this.socket = socket;
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public ServerResult signUp(){
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
        ServerResult signupResult = null;

        try {
            output.write(signUpjson.toString());
            output.newLine();
            output.flush();
            String resultData = input.readLine();
            JSONObject results = new JSONObject(resultData);
            String result = results.getString("Result");

            if (result.equals("Success")){
                signupResult = new ServerResult(true, results.getString("id"));
            }else{
                signupResult = new ServerResult(false,results.getString("Error"));
            }

        }catch(IOException e){
            e.printStackTrace();
        }finally {
            return signupResult;
        }

    }

    public void login() {
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

        try {
            output.write(loginJson.toString());
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void logout() {
    }

}
