package org.tahsin;

import netscape.javascript.JSObject;
import org.json.JSONObject;

import java.io.*;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    private static final ConcurrentHashMap<String, PrintWriter> clients = new ConcurrentHashMap<>();

    private static final ExecutorService pool = Executors.newFixedThreadPool(4);

    public static void main(String[] args) throws IOException {
        ServerSocket listener = new ServerSocket(80);

        while (true) {
            Socket client = listener.accept();
            client.getKeepAlive();
            pool.execute(new ClientHandler(client));
            System.out.println("Connected");
        }
    }

    private static class ClientHandler implements Runnable {

        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String response;
                while ((response = reader.readLine()) != null) {
                    JSONObject object = new JSONObject(response);
                    try {
                        if (!clients.containsKey(object.getString("from")))//sender exist or not
                            clients.put(object.getString("from"), writer);//
                        if (clients.get(object.getString("to")) != null)// receiver exist or not
                            clients.get(object.getString("to")).println(object);//
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
//                    writer.println(object);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}