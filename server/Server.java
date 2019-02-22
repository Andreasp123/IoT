package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server extends Thread {

    private int port;
    private ServerSocket server;
    public static ArrayList<Client> clients = new ArrayList<>();

    public Server() throws IOException {

        this.port = 2000;
        start();
    }

    public Server(int port) throws IOException {
        this.port = port;

    }

    public void run() {
        try {
            server = new ServerSocket(port);
            while (true) {

                String serverHost = server.getInetAddress().getLocalHost().getHostName();
                System.out.println(serverHost);
                Socket client = server.accept();
                Thread.sleep(200);
                String ip = client.getInetAddress().getHostName();
                Client cl = new Client(client, ip, clients);
                clients.add(cl);

            }


        } catch (IOException e) {

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws IOException {
        Server server;
        if (args.length == 0) {
            server = new Server();
        }
        if (args.length == 1) {
            int port = Integer.parseInt(args[0]);
            server = new Server(port);
        }
    }


}
