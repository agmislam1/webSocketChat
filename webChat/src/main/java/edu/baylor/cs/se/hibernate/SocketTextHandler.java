package edu.baylor.cs.se.hibernate;

import edu.baylor.cs.se.hibernate.model.WebSocketData;
import edu.baylor.cs.se.hibernate.repository.socketRepo;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;

@Component
public class SocketTextHandler extends TextWebSocketHandler {



    private socketRepo repo;


    private final List<WebSocketSession> socketData = new ArrayList();

    //private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();


    @Autowired
    public SocketTextHandler(socketRepo repo){
        this.repo = repo;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {



        socketData.add(session);

        // Store in db

        WebSocketData newSocket = new WebSocketData();

        newSocket.setStatus("ON");
        newSocket.setSession(session.getId());
        repo.save(newSocket);



    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {


        WebSocketData newSocket = new WebSocketData();
        newSocket = repo.findBySession(session.getId());
        if(newSocket!=null)
            removeuser(session,newSocket);

    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message)  throws Exception {

        String action = "";
        String username = "";
        String msg = "";
        String status = "";

        String payload = message.getPayload().toString();
        System.out.println( payload);

        JSONObject jsonObject = new JSONObject(payload);



        try{
            username = jsonObject.get("ADD").toString();
            System.out.println( jsonObject.get("ADD"));
            action = "ADD";
        }
        catch (Exception e){
            ;
        }

        try{
            String removedata = jsonObject.get("REMOVE").toString();
            System.out.println( jsonObject.get("REMOVE"));
            action = "REMOVE";
        }
        catch (Exception e){
            ;
        }

        try{
            msg = jsonObject.get("SEND").toString();
            System.out.println( jsonObject.get("SEND"));
            action = "SEND";
        }
        catch (Exception e){
            ;
        }

        try{
            status = jsonObject.get("STATUS").toString();
            System.out.println( jsonObject.get("STATUS"));
            action = "STATUS";
        }
        catch (Exception e){
            ;
        }



        WebSocketData newSocket = new WebSocketData();

        // Register User
        if(action == "ADD"){

            newSocket = repo.findBySession(session.getId());
            if(newSocket!=null){
                newSocket.setUsername(username);
                repo.save(newSocket);

                // Notify other users for new registration
                String txt = "{\"action\":\"add\", \"msg\":\""+username+"\"}";
                sendmsg(session,newSocket,txt);
                //session.sendMessage(new TextMessage(txt));


                return;

            }
        }



        // Remove User
        if(action == "REMOVE"){
            newSocket = repo.findBySession(session.getId());

            if(newSocket!=null){

                // Notify other users
                removeuser(session,newSocket);


            }

        }

        // Change Status

        if(action == "STATUS"){
            newSocket = repo.findBySession(session.getId());
            if(newSocket!=null){

                // change status and notify others
                changeStatus(session,newSocket,status);


            }

        }

        if(action == "SEND") {
            newSocket = repo.findBySession(session.getId());
            if(newSocket!=null){

                // Broadcast Msg

                broadcastmsg(session,newSocket,msg);
                //session.sendMessage(new TextMessage(txt));




            }


        }


    }

    public void changeStatus(WebSocketSession session, WebSocketData aSocket, String status)  throws Exception{


        aSocket.setStatus(status);
        repo.save(aSocket);

        String username = aSocket.getUsername();

        String msg = "";
        String info = "Status Changed";

        System.out.println(username+" changed status to "+ status);

        for(WebSocketSession aSession : socketData){
            if(aSocket.getSession()!=aSession.getId()){
                if(aSession.isOpen()){

                    msg = "{\"action\":\"status\", \"msg\":\""+status+"\", \"user\":\""+username+"\"}";
                    System.out.println(msg);

                    aSession.sendMessage(new TextMessage(msg));
                }
                else{
                    removeuser(aSession,aSocket);
                    //repo.delete(aSocket);
                    //socketData.remove(aSession);
                }


            }

        }

    }

    public void removeuser(WebSocketSession session, WebSocketData aSocket)  throws Exception{

        String username = aSocket.getUsername();
        repo.delete(aSocket);
        socketData.remove(session);
        String msg = "";
        String info = "Logged out";

        for(WebSocketSession aSession : socketData){



            if(aSocket.getSession()!=aSession.getId()){
                if(aSession.isOpen()){

                    msg = "{\"action\":\"remove\", \"msg\":\""+info+"\", \"user\":\""+username+"\"}";
                    System.out.println(msg);

                    aSession.sendMessage(new TextMessage(msg));
                }
                else{
                    removeuser(aSession,aSocket);
                    //repo.delete(aSocket);
                    //socketData.remove(aSession);
                }


            }

        }

    }

    public void sendmsg(WebSocketSession session, WebSocketData aSocket, String msg) throws Exception{


        for(WebSocketSession aSession : socketData){

            String status = aSocket.getStatus();

            if(aSocket.getSession()!=aSession.getId()){
                if(aSession.isOpen()){
                    System.out.println(msg);
                    aSession.sendMessage(new TextMessage(msg));
                }
                else{
                    removeuser(aSession,aSocket);
                    //repo.delete(aSocket);
                    //socketData.remove(aSession);
                }


            }

        }

        return;
    }

    public void broadcastmsg(WebSocketSession session, WebSocketData aSocket, String msg) throws Exception{
        String username = "";
        String txt = "";
        for(WebSocketSession aSession : socketData){


            WebSocketData aUser = repo.findBySession(aSession.getId());
            String status = aUser.getStatus();
            username = aUser.getUsername();

            System.out.println("Current Status of: "+ username + " is :"+ status);

            if(aSession.getId()!=session.getId() && !status.equals("OFF")){
                if(aSession.isOpen()){


                    System.out.println("Sending msg to : "+ username + " is :"+ status);
                    txt = "{\"action\":\"rcv\", \"msg\":\""+msg+"\", \"user\":\""+username+"\"}";
                    System.out.println(txt);
                    aSession.sendMessage(new TextMessage(txt));
                }
                else{
                    removeuser(aSession,aSocket);
                    //repo.delete(aSocket);
                    //socketData.remove(aSession);
                }


            }

        }

        return;
    }
}
