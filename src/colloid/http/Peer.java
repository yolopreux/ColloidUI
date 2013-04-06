package colloid.http;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import colloid.App;

import javafx.application.Platform;

import net.jxta.document.AdvertisementFactory;
import net.jxta.exception.PeerGroupException;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;
import net.jxta.peergroup.PeerGroup;
import net.jxta.peergroup.PeerGroupID;
import net.jxta.pipe.PipeID;
import net.jxta.pipe.PipeService;
import net.jxta.platform.NetworkConfigurator;
import net.jxta.platform.NetworkManager;
import net.jxta.protocol.PipeAdvertisement;
import net.jxta.rendezvous.RendezVousService;
import net.jxta.socket.JxtaServerSocket;

public class Peer {

    private PeerServer server;
    private PeerClient client;

    protected boolean isRunning;
    protected NetworkManager networkManager;
    protected NetworkConfigurator networkConfigurator;
    protected PeerGroup peerGroup;

    public static final String name = "_peer-colloid-jxta";
    public static final int tcpPort = 9714;
//    public static final PeerID PID = IDFactory.newPeerID(PeerGroupID.defaultNetPeerGroupID, name.getBytes());
//    public static final File configurationFile = new File("." + System.getProperty("file.separator") + name);

    private static Peer instance;

    public Peer() {
    }

    public static Peer getInstance() {

        if (instance == null) {
            instance = new Peer();
        }
        return instance;
    }

    protected void init() {
        if (Platform.isFxApplicationThread()) {
            initWithFx();
        } else {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    init();
                }
            });
        }
    }

    protected void initWithFx() {
        if (server == null) {
            server = PeerServer.init();
        }
        if (client == null) {
            client = PeerClient.init();
        }
    }

    public void run() {
        isRunning = true;
        init();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                init();
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                start();
            }
        }).start();
    }

    protected void start() {
        server.start();
    }

    public void stop() {
        if (isRunning) {
            server.stop();
            client.stop();
            log("Stop peer");
        }
    }

    protected void connectedPeersInfo(RendezVousService TheRendezVous, String Name) {

        List<PeerID> theList = TheRendezVous.getLocalRendezVousView();
        Iterator<PeerID> iter = theList.iterator();
        int count = 0;
        while (iter.hasNext()) {
            count++;
            log("Peer connected to this rendezvous:\n\n" + iter.next().toString());
        }
        if (count==0) {
            log("No peers connected to this rendezvous!");
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void send(String combatlog) {
        client.send(combatlog);
    }

    private static void log(String message, Level level) {
        Logger.getLogger(Peer.class.getName()).log(level, message);
    }

    private static void log(String message) {
        log(message, Level.INFO);
    }

    /**
     * TODO
     * @param message
     * @param level
     */
    public static void log(String name, String message, Level level) {
        App.getLogger().info(message);
        System.out.println(message);
    }
}

