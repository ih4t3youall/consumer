package ar.com.sourcesistemas.consumer.sockets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSocketImpl {

    public ServerSocketImpl(int port) throws IOException {

        System.out.printf("ServerSocket started in: %s\n", port);
        ServerSocket socket = null;
        socket = new ServerSocket(port);

        System.out.println("Waiting for kick start");
        Socket cliente =  socket.accept();
        cliente.setSoLinger(true, 10);

        ObjectInputStream buffer = new ObjectInputStream(
                cliente.getInputStream());

        String message = null;
        try {
            message = (String) buffer.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        if (message != null && message.equals("Start")){
            System.out.println("Message received");
            System.out.println("Ready to start processing");
        }

        try {
            socket.close();
        }catch(Exception e){
            System.exit(5);

        }


    }


}
