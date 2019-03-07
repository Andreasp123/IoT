package SharedWhiteboard;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.IOException;
import java.net.*;
import java.util.*;

public class Draw extends JFrame {
    private Paper p = new Paper();

    public static void main(String[] args) throws SocketException, UnknownHostException {
        new Draw();
    }

    public Draw() throws SocketException, UnknownHostException {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().add(p, BorderLayout.CENTER);

        setSize(640, 480);
        setVisible(true);
    }
}

class Paper extends JPanel {
    public HashSet hs = new HashSet();
    DatagramSocket socket;
    private DatagramPacket request;
    private InetAddress host;


    public Paper() throws SocketException, UnknownHostException {

        setBackground(Color.white);
        addMouseListener(new L1());
        addMouseMotionListener(new L2());
        socket = new DatagramSocket(2001);
        host = InetAddress.getLocalHost();

        FetchDrawing fd = new FetchDrawing(hs,socket);

    }

    public synchronized void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.black);
        synchronized (hs){
        Iterator i = hs.iterator();
            while(i.hasNext()) {

                Point p = (Point)i.next();
                String message = Integer.toString(p.x) + " " + Integer.toString(p.y);
                byte[] bytes = message.getBytes();
                int length = bytes.length;
                request = new DatagramPacket(bytes, length, host, 2000);
                try {
                    socket.send(request);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                g.fillOval(p.x, p.y, 2, 2);
            }
        }

    }

    public void addPoint(Point p) {
        hs.add(p);
        repaint();
    }


    class L1 extends MouseAdapter {
        public void mousePressed(MouseEvent me) {
            addPoint(me.getPoint());
        }
    }

    class L2 extends MouseMotionAdapter {
        public void mouseDragged(MouseEvent me) {
            addPoint(me.getPoint());
        }
    }

    class FetchDrawing extends Thread {
        DatagramSocket socket;
        DatagramPacket response;

        public FetchDrawing(HashSet hs, DatagramSocket socket) throws SocketException, UnknownHostException {
            this.socket = socket;
            host = InetAddress.getLocalHost();
            start();
        }

        public void run(){
            byte[] data = new byte[1024];
            response = new DatagramPacket(data, data.length);
            while(true){

                try {
                    socket.receive(response);
                    String converted = new String(response.getData(), 0, response.getLength(),
                            "US-ASCII");
                    String[] xy = converted.split(" ");
                    Point p = new Point(Integer.parseInt(xy[0]), Integer.parseInt(xy[1]));
                    synchronized (hs){
                        hs.add(p);
                    }


                    repaint();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}


