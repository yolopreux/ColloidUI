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

    private boolean isRunning;
    protected NetworkManager networkManager;
    protected NetworkConfigurator networkConfigurator;
    protected PeerGroup peerGroup;

    public static final String Name = "_peer-colloid-jxta";
    public static final int TcpPort = 9714;
    public static final PeerID PID = IDFactory.newPeerID(PeerGroupID.defaultNetPeerGroupID, Name.getBytes());
    public static final File ConfigurationFile = new File("." + System.getProperty("file.separator") + Name);

    protected static Peer instance;

    public Peer() {
        init();
    }

    public static Peer getInstance() {

        if (instance == null) {
            instance = new Peer();
        }
        return instance;
    }

    protected void init() {
        try {
            networkManager = new NetworkManager(NetworkManager.ConfigMode.RENDEZVOUS, Name, ConfigurationFile.toURI());
            // Retrieving the network configurator
            networkConfigurator = networkManager.getConfigurator();

            // Setting more configuration
            networkConfigurator.setTcpPort(TcpPort);
            networkConfigurator.setTcpEnabled(true);
            networkConfigurator.setTcpIncoming(true);
            networkConfigurator.setTcpOutgoing(true);
            // Setting the Peer ID
            log("Setting the peer ID to :\n\n" + PID.toString());
            networkConfigurator.setPeerID(PID);

            // Starting the JXTA network
            log("Start the JXTA network");
            peerGroup = networkManager.startNetwork();

        } catch (IOException ex) {
            log(ex.toString(), Level.SEVERE);
        } catch (PeerGroupException ex) {
            log(ex.toString(), Level.SEVERE);
        }
    }

    public void run() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                start();
                isRunning = true;
            }
        }).start();
    }

    protected void start() {
        if (isRunning) {
            return;
        }
        try {

            // Waiting for other peers to connect to JXTA
            log("Waiting for other peers to connect to JXTA");

            // Creating the JXTA socket server
            int BackLog = 20;
            int Timeout = 30000;

            JxtaServerSocket serverSocket = new JxtaServerSocket(peerGroup, GetPipeAdvertisement(), BackLog, Timeout);
            log("JXTA socket server created");

            Socket colloidSocket = serverSocket.accept();

            if (colloidSocket != null) {
                log("Socket connection established");

                // Retrieving the input streams
                InputStream inputStream = colloidSocket.getInputStream();
                DataInput dataInput = new DataInputStream(inputStream);

                String IncomingMessage = dataInput.readUTF();
                log("Received socket message:\n\n" + IncomingMessage);

                // Retrieving the output stream
                OutputStream outputStream = colloidSocket.getOutputStream();
                DataOutput dataOutput = new DataOutputStream(outputStream);

                // Sending a message
                dataOutput.writeUTF("Hello from " + Name);
                outputStream.flush();

                // Sleeping for 10 seconds
                goToSleep(10000);

                // Closing the streams
                outputStream.close();
                inputStream.close();

                // Closing the socket
                colloidSocket.close();
            }

            // Closing the JXTA socket server
            serverSocket.close();

            // Retrieving connected peers
            connectedPeersInfo(peerGroup.getRendezVousService(), Name);

        } catch (IOException ex) {
            log(ex.toString(), Level.SEVERE);
        }
    }

    public void stop() {
        log("Stop the JXTA network");
        networkManager.stopNetwork();
    }

    public static PipeAdvertisement GetPipeAdvertisement() {

        // Creating a Pipe Advertisement
        PipeAdvertisement MyPipeAdvertisement = (PipeAdvertisement) AdvertisementFactory.newAdvertisement(PipeAdvertisement.getAdvertisementType());
        PipeID MyPipeID = IDFactory.newPipeID(PeerGroupID.defaultNetPeerGroupID, Name.getBytes());

        MyPipeAdvertisement.setPipeID(MyPipeID);
        MyPipeAdvertisement.setType(PipeService.UnicastType);
        MyPipeAdvertisement.setName("Test Socket");
        MyPipeAdvertisement.setDescription("Created by " + Name);

        return MyPipeAdvertisement;
    }

    private void log(String message, Level level) {
        Logger.getLogger(Peer.class.getName()).log(level, message);
    }

    private void log(String message) {
        log(message, Level.INFO);
    }

    public static final void goToSleep(long Duration) {
        long Delay = System.currentTimeMillis() + Duration;
        while (System.currentTimeMillis()<Delay) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException Ex) {
                // We don't care
            }
        }
    }

    private void connectedPeersInfo(RendezVousService TheRendezVous, String Name) {

        List<PeerID> TheList = TheRendezVous.getLocalRendezVousView();
        Iterator<PeerID> Iter = TheList.iterator();
        int count = 0;
        while (Iter.hasNext()) {
            count++;
            log("Peer connected to this rendezvous:\n\n" + Iter.next().toString());
        }

        if (count==0) {
            log("No peers connected to this rendezvous!");
        }
    }
}
