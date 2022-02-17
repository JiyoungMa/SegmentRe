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

        try{
            socket = new Socket();
            socket.connect(new InetSocketAddress(8080));
            userMethod = new UserMethod(socket);

            while (true) {
                if (status == Status.NOTHING) {
                    System.out.println("------------------------------------");
                    System.out.println("-----다음 중 원하는 기능을 고르시오-----");
                    System.out.println("1. 회원가입 \n2.로그인\n3.로그아웃 및 종료 ");

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
                                System.out.println(signupResult.getError());
                            }
                            break;
                        case 2:
                            userMethod.login();
                            status = Status.LOGIN;
                            break;
                        case 3:
                            userMethod.logout();
                            status = Status.LOGOUT;
                            break;
                        default:
                            System.out.println("올바른 번호를 입력해주세요.");
                            break;
                    }
                } else if (status == Status.SIGN_UP){
                    System.out.println("------------------------------------");
                    System.out.print("-----다음 중 원하는 기능을 고르시오-----");
                    String answer = scanner.nextLine();

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
}
