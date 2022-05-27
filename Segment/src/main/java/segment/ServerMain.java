package segment;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Server.Formats.EnterBigChatRoomFormat;
import Server.Formats.EnterSmallChatRoomFormat;
import Server.Formats.GetSmallRoomFormat;
import org.json.JSONObject;
import segment.Entity.*;


public class ServerMain {

    private static ExecutorService threadPool = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {

        try{
            initBigRoom();
            ServerSocket serverSocket = new ServerSocket(8080);
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    System.out.println("클라이언트 1 입장");
                    threadPool.execute(new HandleSocketThread(socket));

                } catch (Exception e) {
                    throw e;
                }
            }
        }catch(Exception e){
            System.out.println("ERROR" + e);
        }
    }

    public static void initBigRoom(){
        EntityManagerFactory emf = null;
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            emf = Persistence.createEntityManagerFactory("hello");
            em = emf.createEntityManager();
            tx = em.getTransaction();
            tx.begin();

            Chatroom big1 = new Chatroom("Java");
            em.persist(big1);

            Chatroom big2 = new Chatroom("Python");
            em.persist(big2);

            Chatroom big3 = new Chatroom("C");
            em.persist(big3);

            tx.commit();
        }catch(Exception e){
            e.printStackTrace();
            if (tx != null) {
                tx.rollback();
            }
        }finally{
            if (em != null){
                em.close();
            }
            if (emf != null){
                emf.close();
            }
        }
    }



    private static class HandleSocketThread implements Runnable {
        private Socket socket;
        private static EntityManagerFactory emf;
        private static EntityManager em;
        private static EntityTransaction tx;
        private BufferedWriter writer;

        HandleSocketThread(Socket socket){
            this.socket = socket;
        }

        @Override
        public void run(){
            System.out.println("In");
            getSmallRoomList("1",Long.valueOf(2));
            try {
                emf = Persistence.createEntityManagerFactory("hello");

                em = emf.createEntityManager();
                tx = em.getTransaction();

                Reader reader = new InputStreamReader(socket.getInputStream()); //문자 -> Reader
                BufferedReader bufferedReader = new BufferedReader(reader); // Line 단위로 읽기 위함

                writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                while (true){
                    if(!tx.isActive()) {
                        tx.begin();
                    }
                    String data = bufferedReader.readLine();
                    if (data.length() == 0){
                        break;
                    }
                    System.out.println("DATA :" +  data);

                    JSONObject jsondata = new JSONObject(data);
                    String method = jsondata.get("Method").toString();

                    JSONObject returndata = new JSONObject();

                    try {
                        switch (method) {
                            case "SignUp":
                                String signUpId = signUpJson(jsondata);
                                returndata.put("Result", "Success");
                                returndata.put("Id",signUpId);
                                break;
                            case "Login":
                                String loginId = loginJson(jsondata);
                                returndata.put("Result", "Success");
                                returndata.put("Id",loginId);
                                break;
                            case "Logout":
                                String logoutId = logoutJson(jsondata);
                                returndata.put("Result","Success");
                                returndata.put("Id", logoutId);
                                break;
                            case "GetBigRooms":
                                List bigRooms = getBigRoomList();
                                sendRoomList(bigRooms,true);
                                break;
                            case "Enter BigChatRoom":
                                EnterBigChatRoomFormat bigChatRoomData = enterBigChatRoomJson(jsondata);
                                returndata.put("Result","Success");
                                returndata.put("Id",bigChatRoomData.getId());
                                returndata.put("BigChatRoomId",bigChatRoomData.getBigChatRoomId());
                                break;
                            case "GetSmallRooms":
                                GetSmallRoomFormat smallRoomData = getSmallChatroomJson(jsondata);
                                List smallRooms = getSmallRoomList(smallRoomData.getId(), smallRoomData.getBigroomId());
                                sendRoomList(smallRooms,false);
                                break;
                            case "Enter SmallChatRoom":
                                EnterSmallChatRoomFormat smallChatRoomFormat = enterSmallChatroomJson(jsondata);
                        }
                        tx.commit();
                    }catch (Exception e){
                        returndata.put("Result", "Fail");
                        returndata.put("Error", e.getLocalizedMessage());
                    }finally {
                        writer.write(returndata.toString());
                        writer.newLine();
                        writer.flush();
                    }
                }
                bufferedReader.close();
                reader.close();
                writer.close();
            }catch(Exception e){
                e.printStackTrace();
                if (tx != null) {
                    tx.rollback();
                }
            }finally{

                if (em != null){
                    em.close();
                }
                if (emf != null){
                    emf.close();
                }
                if(socket != null){
                    try{
                        socket.close();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }

        private EnterSmallChatRoomFormat enterSmallChatroomJson(JSONObject jsondata) {
            JSONObject data = jsondata.getJSONObject("data");
            return new EnterSmallChatRoomFormat(data.getString("Id"), Long.valueOf(data.getInt("SmallRoomId")));
        }

        private GetSmallRoomFormat getSmallChatroomJson(JSONObject jsondata) {
            JSONObject data = jsondata.getJSONObject("data");
            return new GetSmallRoomFormat(data.getString("Id"), Long.valueOf(data.getInt("BigRoomId")));
        }

        private EnterBigChatRoomFormat enterBigChatRoomJson(JSONObject jsondata) {
            JSONObject data = jsondata.getJSONObject("data");
            return enterBigChatroom(data.get("Id").toString(),Long.valueOf(data.getInt("BigChatRoomId")));
        }

        private void sendRoomList(List<Chatroom> rooms,boolean roomType) throws Exception{ //roomType true == big
            JSONObject roomsJson = new JSONObject();
            JSONObject roomList = new JSONObject();
            for (int i = 0; i<rooms.size();i++){
                Chatroom nowRoom = rooms.get(i);
                roomList.append("RoomId",Long.toString(nowRoom.getChatroomId()));
                roomList.put(Long.toString(nowRoom.getChatroomId()),nowRoom.getChatroomName());
            }
            roomsJson.put("data",roomList);
            roomsJson.put("Result","Success");

            writer.write(roomsJson.toString());
            writer.newLine();
            writer.flush();
        }

        private String logoutJson(JSONObject jsondata) {
            JSONObject data = jsondata.getJSONObject("data");
            return logout(data.getString("Id"));
        }

        private String loginJson(JSONObject jsondata) {
            JSONObject data = jsondata.getJSONObject("data");
            return login(data.getString("Id"), data.getString("password"));
        }

        private String signUpJson(JSONObject jsondata) {
            JSONObject data = jsondata.getJSONObject("data");
            return signUp(data.getString("Id"),data.getString("password"), data.getString("username"));
        }

        public static String signUp(String Id,String password, String userName){
            User newUser = new User(Id, password, userName);
            em.persist(newUser);
            return Id;
        }

        public static String login(String id, String password){
            User findUser = em.find(User.class, id);

            try {
                if (findUser == null){
                    Exception exception = new Exception("아이디에 해당하는 유저가 존재하지 않음");
                    throw exception;
                }
                else if (!findUser.getUserPassword().equals(password)) {
                    Exception exception = new Exception("비밀번호가 일치하지 않음");
                    throw exception;
                }
                else{
                    findUser.setUserStatus(UserStatus.Online);
                    em.persist(findUser);
                }
                return findUser.getUserRealId();
            }catch(Exception e){
                System.out.println(e);
                findUser = null;
            }finally{
                return findUser.getUserRealId();
            }
        }

        public static String logout(String userId){
            User findUser = em.find(User.class, userId);
            try {
                if(findUser == null){
                    Exception exception = new Exception("아이디에 해당하는 유저가 존재하지 않음");
                    throw exception;
                }
                findUser.setUserStatus(UserStatus.Offline);
                em.persist(findUser);
            }catch (Exception e){
                System.out.println(e);
            }finally {
                return findUser.getUserRealId();
            }
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
            String jpql = "SELECT room.chatroomUsers From Chatroom room WHERE room.bigChatroom.chatroomId = :bigChatRoom";
            TypedQuery<User> userInRoom = em.createQuery(jpql, User.class);
            jpql = "SELECT room FROM Chatroom room WHERE room.bigChatroom.chatroomId = :bigChatRoom";
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

        public static EnterBigChatRoomFormat enterBigChatroom(String userId, Long chatroomId){
            Chatroom nowChatroom = em.find(Chatroom.class, chatroomId);
            User nowUser = em.find(User.class, userId);
            checkOnline(nowUser);
            Chatroom_User newChatUser = new Chatroom_User(nowChatroom, nowUser);
            em.persist(newChatUser);
            return new EnterBigChatRoomFormat(nowUser.getUserRealId(),nowChatroom.getChatroomId());
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

}
