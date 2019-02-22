package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Client extends Thread {
    private String ip;
    private Socket socket;
    public BufferedReader in;
    public PrintWriter out;
    private String message;
    Scanner scan = new Scanner(System.in);
    static ArrayList<Client> clients;
    private boolean alive = true;




    public Client(Socket socket, String ip, ArrayList<Client> clients) throws IOException {
        this.socket = socket;
        this.ip = ip;
        this.clients = clients;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
        start();
    }

    public void run() {
        try {
            socket.setKeepAlive(Boolean.TRUE);
            socket.setSoTimeout(0);
            while(alive){
                    message = in.readLine();
                    sendMessage(message);

                Thread.sleep(200);
            }


        } catch (IOException e) {
            deleteClient();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    public void sendMessage(String message){
            for(Client c : clients){
                c.out.println(ip + ": " + message);
            }
    }

    public void deleteClient(){
        synchronized (clients){
            clients.remove(this);
        }
    }


}
