import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ChatClient {
    private String host;
    private int port;
    private Socket socket;


    public ChatClient() {
        host = "127.0.0.1";
        port = 2000;
    }

    public ChatClient(String host) {
        this.host = host;
        this.port = 2000;
    }

    public ChatClient(String host, int port) {
        this.host = host;
        this.port = port;

    }


    public void startThreads() {
        ReadInput read = new ReadInput();
        WriteOut wo = new WriteOut();
    }


    /**
     * Thread that reads user input from all connected users and relays to chat client
     */
    class ReadInput extends Thread {
        public ReadInput() {
            start();
        }

        public void run() {
            try {
                Socket socket = new Socket(host, port);
                socket.setKeepAlive(Boolean.TRUE);
                socket.setSoTimeout(0);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while (socket.isConnected()) {
                    System.out.println(in.readLine());

                }
            } catch (ConnectException e) {
                System.out.println("Unable to connect to host. Closing");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Thread that takes user input and sends to the server
     */
    class WriteOut extends Thread {
        public WriteOut() {
            start();
        }

        public void run() {
            try {
                Socket socket = new Socket(host, port);
                socket.setKeepAlive(Boolean.TRUE);
                socket.setSoTimeout(0);
                PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "ISO-8859-1"), true);
                System.out.println("Connected to: " + socket.getInetAddress());
                System.out.println("Connected on port " + socket.getPort());
                Scanner scan = new Scanner(System.in);
                while (socket.isConnected()) {
                    out.println(scan.nextLine());
                }
                scan.close();

            } catch (UnknownHostException e) {
                System.out.println("Unable to connect to " + host);
            } catch (ConnectException e) {
                System.out.println("Unable to connect to host. Closing");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) throws IOException {
        ChatClient cc;
        if (args.length == 0) {
            cc = new ChatClient();
        } else {
            if (args.length == 1) {
                String host = args[0];
                cc = new ChatClient(host);

            } else {
                String host = args[0];
                int port = Integer.parseInt(args[1]);
                cc = new ChatClient(host, port);
            }
        }
        cc.startThreads();
    }
}
