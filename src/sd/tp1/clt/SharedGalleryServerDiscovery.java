package sd.tp1.clt;

import java.io.IOException;
import java.net.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Francisco Rodrigues 42727
 * Luis Abreu 43322
 */
public class SharedGalleryServerDiscovery implements Runnable{

    private String client_multicast = "224.1.2.8"; // Multicast IP
    private int client_port = 9000; // Multicast Port
    private InetAddress client_address = null; // Client address
    private MulticastSocket client_socket = null; // Socket Multicast

    private Map<String,Request> servers = new ConcurrentHashMap<>(); // Map containing servers where the key is the server address and the value an object that handles the requests

    public SharedGalleryServerDiscovery() { }

    /**
     * Get the map of servers
     * @return the map of servers
     */
    public Map<String,Request> getServers() {
        return servers;
    }

    /**
     * Removes server with a certain address
     * @param address - Address of a server
     */
    public void removeServer(String address) {
        servers.remove(address);
    }

    public void run() {

        try {
            client_address = InetAddress.getByName(client_multicast);
            client_socket = new MulticastSocket();
        }
        catch (IOException e) {
            System.exit(1);
        }

        // Receives packets from servers/clients that are a FileServer
        Thread r = new Thread(() -> {
            while(true) {
                DatagramPacket reply;
                byte [] buffer = new byte[65536];
                reply = new DatagramPacket(buffer, buffer.length);
                    try {
                        client_socket.setSoTimeout(5000);
                        client_socket.receive(reply);
                        if(reply.getLength() > 0) {
                            String url = new String(reply.getData(), 0, reply.getLength());
                            if(!servers.containsKey(url) && url.contains("http")) {
                                if (url.contains("SOAP")) {
                                    System.err.println("ADDED: " + url);
                                    servers.put(url, new RequestSOAP(url));
                                }
                                else if(url.contains("REST")) {
                                    System.err.println("ADDED: " + url);
                                    servers.put(url, new RequestREST(url));
                                }
                            }
                        }
                    } catch (IOException e) {
                        System.out.println("CLIENT ERROR: No packet received!");
                    }
                }
        });
        r.start();

        // Sends packets to multicast asking who is a FileServer
        Thread s = new Thread(() -> {
                while (true) {
                    try {
                        DatagramPacket request;
                        String data_req = "FileServer";
                        byte [] data_cont = data_req.getBytes();
                        request = new DatagramPacket(data_cont, data_cont.length, client_address, client_port);
                        client_socket.send(request);
                        Thread.sleep(5000);
                    } catch (IOException | InterruptedException e) {
                        System.err.println("CLIENT ERROR: Could not send packet to servers!");
                    }
                }
        });
        s.start();

    }

}
