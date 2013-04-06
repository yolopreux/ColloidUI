package colloid.http;

import net.jxta.exception.PeerGroupException;
import net.jxta.peergroup.PeerGroup;
import net.jxta.platform.NetworkManager;
import net.jxta.protocol.PipeAdvertisement;
import net.jxta.socket.JxtaSocket;

import java.io.*;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.text.MessageFormat;

import colloid.App;

public class PeerClient {

    private transient NetworkManager manager = null;

    private transient PeerGroup netPeerGroup = null;
    private transient PipeAdvertisement pipeAdv;
    private transient boolean waitForRendezvous = false;

    protected ArrayDeque<String> logdata = new ArrayDeque<String>();

    public PeerClient(boolean waitForRendezvous) {
        try {
            manager = new NetworkManager(NetworkManager.ConfigMode.ADHOC, "SocketClient",
                    new File(new File(".cache"), "SocketClient").toURI());
            start();
        } catch (Exception e) {
            App.getLogger().log(Level.SEVERE, e.toString());
            e.printStackTrace();
        }

        netPeerGroup = manager.getNetPeerGroup();
        pipeAdv = PeerServer.createSocketAdvertisement("1");
        if (waitForRendezvous) {
            manager.waitForRendezvousConnection(0);
        }
    }

    /**
     * Interact with the server.
     */
    public void send(final String log) {
        logdata.add(log);
    }

    public void send() {
        sendLogdata("hallo");
    }

    public void run() {
        Thread.currentThread().setName(PeerClient.class.getName() + ".send()");
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    String log = logdata.poll();
                    if (log != null) {
                        sendLogdata(log);
                    }
                }
            }
        }).start();
    }

    protected void sendLogdata(String logdata) {
        System.out.println(String.format("Sending log data: %s", logdata));
        try {
            if (waitForRendezvous) {
                manager.waitForRendezvousConnection(0);
            }

            System.out.println("Connecting to the server");
            JxtaSocket socket = new JxtaSocket(netPeerGroup,
                    // no specific peerid
                    null,
                    pipeAdv,
                    // connection timeout: 5 seconds(5000)
                    1000,
                    // reliable connection
                    true);

            // get the socket output stream
            OutputStream out = socket.getOutputStream();
            DataOutput dos = new DataOutputStream(out);

            // get the socket input stream
            InputStream in = socket.getInputStream();
            DataInput dis = new DataInputStream(in);

            dos.writeUTF(logdata);
            out.flush();

            out.close();
            in.close();

            socket.close();
            System.out.println("Socket connection closed");
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    public void stop() {
        manager.stopNetwork();
    }

    public void start() {
        try {
            run();
            manager.startNetwork();
        } catch (PeerGroupException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PeerClient init() {
//        System.setProperty("net.jxta.logging.Logging", "FINEST");
//        System.setProperty("net.jxta.level", "FINEST");
//        System.setProperty("java.util.logging.config.file", "logging.properties");
        PeerClient socEx = null;
       try {
           Thread.currentThread().setName(PeerClient.class.getName() + ".main()");
           String value = System.getProperty("RDVWAIT", "false");
           boolean waitForRendezvous = Boolean.valueOf(value);
           socEx = new PeerClient(waitForRendezvous);
       } catch (Throwable e) {
           App.getLogger().log(Level.SEVERE, e.toString());
           System.out.flush();
           System.err.println("Failed : " + e);
           e.printStackTrace(System.err);
       }

       return socEx;
    }

}
