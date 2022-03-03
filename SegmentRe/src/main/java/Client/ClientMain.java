package Client;

import org.json.JSONObject;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.nio.charset.StandardCharsets;

public class ClientMain {
    private static Scanner scanner;
    private static UserMethod userMethod;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);

        Socket socket = null;
        Status status = Status.NOTHING;
        String myid = null;
        int bigroom = -1;
        int smallromm = -1;

        try{
            socket = new Socket();
            socket.connect(new InetSocketAddress(8080));
            userMethod = new UserMethod(socket);

            while (true) {
                if (status == Status.NOTHING) {
                    status = statusNothing(status);
                } else if (status == Status.SIGN_UP){
                    status = statusSignUp(status);
                } else if (status == Status.LOGIN){
                    System.out.println("------------------------------------");
                    System.out.println("-----다음 중 원하는 기능을 고르시오-----");
                    System.out.println("1.로그아웃\n2.큰 채팅방 입장");
                    int menu = scanner.nextInt();
                    scanner.nextLine(); //개행문자 제거

                    switch (menu) {
                        case 1:
                            userMethod.logout();
                            status = Status.LOGOUT;
                            break;
                        case 2:
                            userMethod.enterChatRoom(true);
                            status = Status.ENTER_BIG_ROOM;
                            break;
                        default:
                            System.out.println("올바른 번호를 입력해주세요.");
                            break;
                    }
                }
            }
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

    private static Status statusSignUp(Status status) {
        String myid;
        System.out.println("------------------------------------");
        System.out.print("-----로그인을 진행합니다.-----");
        ServerResult loginResult = userMethod.login();
        if (loginResult.getResult() == true) {
            status = Status.LOGIN;
            myid = loginResult.getId();
            System.out.println("성공적으로 로그인이 완료되었습니다.");
        }else{
            System.out.print("문제가 발생했습니다 : ");
            System.out.println(loginResult.getError());
        }
        return status;
    }

    private static Status statusNothing(Status status) {
        String myid;
        System.out.println("------------------------------------");
        System.out.println("-----다음 중 원하는 기능을 고르시오-----");
        System.out.println("1. 회원가입 \n2.로그인");

        int menu = scanner.nextInt();
        scanner.nextLine(); //개행문자 제거

        switch (menu) {
            case 1:
                ServerResult signupResult = userMethod.signUp();
                if (signupResult.getResult() == true) {
                    status = Status.SIGN_UP;
                    myid = signupResult.getId();
                    System.out.println("성공적으로 회원가입이 완료되었습니다");
                }else{
                    System.out.print("문제가 발생했습니다 : ");
                    System.out.println(signupResult.getError());
                }
                break;
            case 2:
                ServerResult loginResult = userMethod.login();
                if (loginResult.getResult() == true) {
                    status = Status.LOGIN;
                    myid = loginResult.getId();
                    System.out.println("성공적으로 로그인이 완료되었습니다.");
                }else{
                    System.out.print("문제가 발생했습니다 : ");
                    System.out.println(loginResult.getError());
                }
                break;
            default:
                System.out.println("올바른 번호를 입력해주세요.");
                break;
        }
        return status;
    }
}
