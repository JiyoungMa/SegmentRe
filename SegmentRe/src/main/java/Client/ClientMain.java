package Client;

import org.json.JSONObject;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.nio.charset.StandardCharsets;

public class ClientMain {
    private static InputStream input;
    private static OutputStream output;
    private static Scanner scanner;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);

        Socket socket = null;

        try{
            socket = new Socket();
            socket.connect(new InetSocketAddress(8080));

            input = socket.getInputStream();
            output = socket.getOutputStream();

            System.out.println("------------------------------------");
            System.out.println("-----다음 중 원하는 기능을 고르시오-----");
            System.out.println("1. 회원가입 \n 2.로그인 \n 추 후 추가하기!");

            int menu = scanner.nextInt();

            switch (menu){
                case 1:
                    signUp();
                    break;
                case 2:
                    login();
                    break;
                default:
                    break;
            }
//            JSONObject jsondata = new JSONObject();
//            jsondata.put("Method", "Login");
//
//            String data = jsondata.toString();

//            output.write(data.getBytes(StandardCharsets.UTF_8));
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

    public static void signUp(){
        //password 암호화 나중에 추가해보기
        //While True 말고 비동기로 진행할수는 없을까?

        System.out.println("------------------------------------");
        System.out.print("사용하고 싶은 아이디를 입력하세요 : ");
        String id = scanner.nextLine();

        System.out.print("사용하고 싶은 비밀번호를 입력하세요 : ");
        String password = scanner.nextLine();

        System.out.print("사용하고 싶은 닉네임을 입력하세요 : ");
        String username = scanner.nextLine();

        JSONObject loginjson = new JSONObject();
        loginjson.put("Method", "Login");

        JSONObject datajson = new JSONObject();
        datajson.put("Id", id);
        datajson.put("password", password);
        datajson.put("username", username);

        loginjson.put("data", datajson);

        String data = loginjson.toString();

        try {
            output.write(data.getBytes(StandardCharsets.UTF_8));
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    private static void login() {
    }
}
