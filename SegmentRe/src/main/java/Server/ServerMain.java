package Server;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.json.JSONObject;


public class ServerMain {
    private static EntityManagerFactory emf;
    private static EntityManager em;
    private static EntityTransaction tx;

    private static ExecutorService threadPool = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {
        emf = Persistence.createEntityManagerFactory("hello");

        em = emf.createEntityManager();
        tx = em.getTransaction();
        tx.begin();

        try{
            ServerSocket serverSocket = new ServerSocket(8080);
            while (true) {
                Socket socket = serverSocket.accept();
                threadPool.submit(new HandleSocketThread(socket));
            }
        }catch(Exception e){
            System.out.println("ERROR" + e);
            tx.rollback();
        }finally {
            em.close();
        }
        emf.close();
    }



    private static class HandleSocketThread extends Thread {
        private Socket socket;

        HandleSocketThread(Socket socket){
            this.socket = socket;
        }

        public void run(){
            try {
                Reader reader = new InputStreamReader(socket.getInputStream()); //문자 -> Reader
                BufferedReader bufferedReader = new BufferedReader(reader); // Line 단위로 읽기 위함

                OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream());

                while (true){
                    String data = bufferedReader.readLine();
                    if (data.length() == 0){
                        break;
                    }

                    JSONObject jsondata = new JSONObject(data);

                }

                bufferedReader.close();
                reader.close();

                writer.close();

            }catch(IOException e){
                System.err.println(e);
            }finally{
                try{
                    socket.close();
                }catch(IOException e){}
            }
        }
    }

    public static void signUp(String Id,String password, String userName){
        User newUser = new User(Id, password, userName);
        em.persist(newUser);
    }

    public static User login(String id, String password){
        User findUser = em.find(User.class, id);

        try {
            if (!findUser.getUserPassword().equals(password)) {
                Exception exception = new Exception("비밀번호가 일치하지 않음");
                throw exception;
            }
            else{
                findUser.setUserStatus(UserStatus.Online);
                em.persist(findUser);
            }
        }catch(Exception e){
            System.out.println(e);
            findUser = null;
        }finally{
            return findUser;
        }
    }

    public static void logout(String userId){
        User findUser = em.find(User.class, userId);
        findUser.setUserStatus(UserStatus.Offline);
        em.persist(findUser);
    }

    public static void checkOnline(User user){
        if (user.getUserStatus() != UserStatus.Online){
            user.setUserStatus(UserStatus.Online);
        }
    }

    public static List<Chatroom> getBigRoomList(){
        String jpql = "SELECT room FROM Chatroom room WHERE room.chatroomType = 'Big'";
        TypedQuery<Chatroom> query = em.createQuery(jpql, Chatroom.class);

        List<Chatroom> bigRoomList = query.getResultList();

        return bigRoomList;
    }

    public static List<Chatroom> getSmallRoomList(String userId, Long bigRoomId){
        String jpql = "SELECT room FROM Chatroom room WHERE room.bigChatroom.chatroomId = :bigChatRoom";
        TypedQuery<Chatroom> query = em.createQuery(jpql, Chatroom.class);
        query.setParameter("bigChatRoom", bigRoomId);

        List<Chatroom> smallRoomList = query.getResultList();

        return smallRoomList;
    }

    public static Chatroom makeBigChatroom(String userId,String chatroomName){
        Chatroom newChatroom = new Chatroom(chatroomName);
        User findUser = em.find(User.class, userId);
        checkOnline(findUser);
        Chatroom_User newChatUser = new Chatroom_User(newChatroom, findUser);
        em.persist(newChatroom);
        em.persist(newChatUser);
        return newChatroom;
    }

    public static Chatroom enterBigChatroom(String userId, Long chatroomId){
        Chatroom nowChatroom = em.find(Chatroom.class, chatroomId);
        User nowUser = em.find(User.class, userId);
        checkOnline(nowUser);
        Chatroom_User newChatUser = new Chatroom_User(nowChatroom, nowUser);
        em.persist(newChatUser);

        return nowChatroom;
    }

    public static void makeSmallChatroom(String userId, String chatroomName, Long bigChatRoomId){
        Chatroom nowBigChatRoom = em.find(Chatroom.class, bigChatRoomId);
        List<Chatroom_User> bigChatRoomUsers = nowBigChatRoom.getChatroomUsers();
        User findUser = em.find(User.class, userId);
        checkOnline(findUser);

        boolean result = false;
        //findby 사용하면 되지만 아직 사용방법 잘 모르므로 일단 for문 돌림
        for (Chatroom_User chatroomUser : bigChatRoomUsers){
            if (chatroomUser.getUser() == findUser){
                result = true;
                break;
            }
        }

        try {
            if (result == false) {
                Exception exception = new Exception("User가 해당 BigChatRoom에 존재하지 않음");
                throw exception;
            }else{
                Chatroom newSmallChatRoom = new Chatroom(chatroomName, nowBigChatRoom);
                Chatroom_User newChatUser = new Chatroom_User(newSmallChatRoom, findUser);
                em.persist(newSmallChatRoom);
                em.persist(newChatUser);
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public static Chatroom enterSmallChatroom(String userId, Long chatroomId){
        Chatroom nowChatroom = em.find(Chatroom.class, chatroomId);
        User nowUser = em.find(User.class, userId);
        List<Chatroom_User> chatroomUsers = nowChatroom.getBigChatroom().getChatroomUsers();

        boolean result = false;
        for (Chatroom_User cu : chatroomUsers){
            if (cu.getUser() == nowUser){
                result = true;
                break;
            }
        }

        try {
            if (result == false) {
                Exception exception = new Exception("User가 해당 BigChatRoom에 존재하지 않음");
                throw exception;
            }else{
                Chatroom_User newChatUser = new Chatroom_User(nowChatroom, nowUser);
                em.persist(newChatUser);
            }
        }catch (Exception e){
            System.out.println(e);
        }

        return nowChatroom;
    }

    public static void sendMessage(String userId, Long chatroomId, String message){
        User nowUser = em.find(User.class, userId);
        Chatroom nowChatroom = em.find(Chatroom.class, chatroomId);

        List<Chatroom_User> nowChatroomUser = nowChatroom.getChatroomUsers();
        boolean result = false;
        for (Chatroom_User cu : nowChatroomUser){
            if (cu.getUser() == nowUser){
                result = true;
                break;
            }
        }

        try {
            if (result == false) {
                Exception exception = new Exception("User가 해당 BigChatRoom에 존재하지 않음");
                throw exception;
            }else{
                Chat newChat = new Chat(nowUser, nowChatroom, message);
                em.persist(newChat);
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public static void getMessage(String userId, Long chatroomId, LocalDateTime recentMessageTime){
        User nowUser = em.find(User.class, userId);
        Chatroom nowChatroom = em.find(Chatroom.class, chatroomId);

        List<Chatroom_User> nowChatroomUser = nowChatroom.getChatroomUsers();
        boolean result = false;
        for (Chatroom_User cu : nowChatroomUser){
            if (cu.getUser() == nowUser){
                result = true;
                break;
            }
        }

        try {
            if (result == false) {
                Exception exception = new Exception("User가 해당 BigChatRoom에 존재하지 않음");
                throw exception;
            }else{
                String jpql = "SELECT message FROM Chat message WHERE message.chatTime > :recentMessageTime";
                TypedQuery<Chat> query = em.createQuery(jpql, Chat.class);
                query.setParameter("recentMessageTime", recentMessageTime);

                List<Chat> messages = query.getResultList();
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
