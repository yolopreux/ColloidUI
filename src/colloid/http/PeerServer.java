package colloid.http;

import net.jxta.document.AdvertisementFactory;
import net.jxta.exception.PeerGroupException;
import net.jxta.id.IDFactory;
import net.jxta.impl.id.UUID.PeerGroupID;
import net.jxta.peergroup.PeerGroup;
import net.jxta.pipe.PipeID;
import net.jxta.pipe.PipeService;
import net.jxta.platform.NetworkManager;
import net.jxta.protocol.PipeAdvertisement;
import net.jxta.socket.JxtaServerSocket;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.MessageFormat;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import colloid.App;
import colloid.http.User.UnloggedUserError;

public class PeerServer {

    private transient PeerGroup netPeerGroup = null;
    public final static String SOCKETIDSTR = "urn:jxta:cbid-59616261646162614E504720503250336578130B8AD24518835DCF215ED3EDC604";
    private boolean isRunning = false;
    private transient NetworkManager manager;

    public PeerServer() throws IOException, PeerGroupException {
        manager = new NetworkManager(NetworkManager.ConfigMode.ADHOC, "SocketServer",
                new File(new File(".cache"), "SocketServer").toURI());
        manager.startNetwork();
        netPeerGroup = manager.getNetPeerGroup();
    }

    public static PipeAdvertisement createSocketAdvertisement() {
        return createSocketAdvertisement(null);
    }

    public static PipeAdvertisement createSocketAdvertisement(String ID) {
        PipeID socketID = null;
        String peerID;

        try {
            peerID = String.format("ColloidPeer%s", User.getInstance().me().getName());
        } catch (UnloggedUserError e) {
            peerID = String.format("ColloidPeer");
        }
        if (ID != null) {
            peerID = "ColloidPeer" + ID;
        }
//        try {
//            socketID = (PipeID) IDFactory.fromURI(new URI(SOCKETIDSTR));
//        } catch (URISyntaxException use) {
//            use.printStackTrace();
//        }
        Peer.log(PeerServer.class.getName(), String.format("createSocketAdvertisement(): set peerID %s", peerID), Level.INFO);
        socketID = IDFactory.newPipeID(PeerGroupID.defaultNetPeerGroupID, peerID.getBytes());
        PipeAdvertisement advertisement = (PipeAdvertisement)
                AdvertisementFactory.newAdvertisement(PipeAdvertisement.getAdvertisementType());

        advertisement.setPipeID(socketID);
        advertisement.setType(PipeService.UnicastType);
        advertisement.setName(peerID);
        return advertisement;
    }

    /**
     * Wait for connections
     */
    public void run() {
        isRunning = true;
        JxtaServerSocket serverSocket = null;
        try {
            serverSocket = new JxtaServerSocket(netPeerGroup, createSocketAdvertisement(), 10);
            serverSocket.setSoTimeout(0);
        } catch (IOException e) {
            App.getLogger().severe(e.toString());
            System.out.println("failed to create a server socket");
            e.printStackTrace();
        }

        while (isRunning) {
            try {
                System.out.println("Waiting for connections");
                Socket socket = serverSocket.accept();
                if (socket != null) {
                    System.out.println("New socket connection accepted");
                    Thread thread = new Thread(new ConnectionHandler(socket), "Connection Handler Thread");
                    thread.start();
                }
            } catch (Exception e) {
                App.getLogger().log(Level.SEVERE, e.toString());
                e.printStackTrace();
            }
        }
    }

    private class ConnectionHandler implements Runnable {
        Socket socket = null;

        ConnectionHandler(Socket socket) {
            this.socket = socket;
        }

        private void handle(Socket socket) {
            try {
                // get the socket output stream
                OutputStream out = socket.getOutputStream();
                // get the socket input stream
                InputStream in = socket.getInputStream();
                DataInput dis = new DataInputStream(in);

                System.out.println(MessageFormat.format("Sending/Receiving {0} bytes.", 0));
                String data;

                data = dis.readUTF();
                out.write("ok".getBytes());
                System.out.println(data);

                out.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void run() {
            handle(socket);
        }
    }

    public static void main(String args[]) {

         System.setProperty("net.jxta.logging.Logging", "FINEST");
         System.setProperty("net.jxta.level", "FINEST");
         System.setProperty("java.util.logging.config.file", "logging.properties");

        try {
            //Thread.currentThread().setName(PeerServer.class.getName() + ".main()");
            PeerServer socEx = new PeerServer();
            socEx.run();
        } catch (Throwable e) {
            Logger.getLogger("err").log(Level.SEVERE, e.toString());
            System.err.println("Failed : " + e);
            e.printStackTrace(System.err);
            System.exit(-1);
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public static PeerServer init() {
//        System.setProperty("net.jxta.logging.Logging", "FINEST");
//        System.setProperty("net.jxta.level", "FINEST");
//        System.setProperty("java.util.logging.config.file", "logging.properties");
       PeerServer socEx = null;
       try {
           //Thread.currentThread().setName(PeerServer.class.getName() + ".main()");
           socEx = new PeerServer();

           return socEx;
       } catch (Throwable e) {
           App.getLogger().log(Level.SEVERE, e.toString());
           System.err.println("Failed : " + e);
           e.printStackTrace(System.err);
       }

       return socEx;
    }

    public void start() {
        run();
    }

    public void stop() {
        isRunning = false;
        manager.stopNetwork();
    }
}
