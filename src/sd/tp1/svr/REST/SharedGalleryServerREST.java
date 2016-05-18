package sd.tp1.svr.REST;

import com.sun.net.httpserver.HttpServer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import sd.tp1.svr.SharedGalleryFileSystemUtilities;
import sd.tp1.svr.SharedGalleryClientDiscovery;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.io.*;
import java.net.*;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Francisco Rodrigues 42727
 * Luis Abreu 43322
 */
@Path("/")
public class SharedGalleryServerREST {

    private static File basePath = new File("./FileServerREST"); // Path where the server files are
    private static String local_password;
    private static final File KEYSTORE = new File("./server.jks");

    private static KafkaProducer<String, String> producer;

    /**
     * The methods from this class act the same way as the ones from REQUEST interface, but instead of null return an error status code
     */

    @GET
    @Path("password={password}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getListOfAlbums(@PathParam("password") String password) {
        if(validate(password)) {
            List<String> lst = SharedGalleryFileSystemUtilities.getDirectoriesFromPath(basePath);
            if(lst != null)
                return Response.ok(lst).build();
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @POST
    @Path("&password={password}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createAlbum(@PathParam("password") String password, String album) {
        if(validate(password)) {
            if(album.equalsIgnoreCase(SharedGalleryFileSystemUtilities.createDirectory(basePath, album))) {
                sendToConsumers("Albuns", "Updated at " + System.nanoTime());
                return Response.status(Response.Status.CREATED).entity(album).build();
            }
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @GET
    @Path("/{album}&password={password}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getListOfPictures(@PathParam("album") String album, @PathParam("password") String password) {
        if(validate(password)) {
            List<String> lst = SharedGalleryFileSystemUtilities.getPicturesFromDirectory(basePath, album);
            if(lst != null)
                return Response.ok(lst).build();
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @DELETE
    @Path("/{album}&password={password}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAlbum(@PathParam("album") String album, @PathParam("password") String password) {
        if(validate(password)) {
            boolean created = SharedGalleryFileSystemUtilities.deleteDirectory(basePath, album);
            if(created) {
                sendToConsumers("Albuns", "Updated at " + System.nanoTime());
                return Response.ok(true).build();
            }
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @GET
    @Path("/{album}/{picture}&password={password}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getPictureData(@PathParam("album") String album, @PathParam("picture") String picture, @PathParam("password") String password) {
        if(validate(password)) {
            byte [] data = SharedGalleryFileSystemUtilities.getDataFromPicture(basePath, album, picture);
            if(data != null)
                return Response.ok(data).build();
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @POST
    @Path("/{album}/{picture}&password={password}")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadPicture(@PathParam("album") String album, @PathParam("picture") String picture, @PathParam("password") String password, byte [] data) {
        if(validate(password)) {
            String new_name = SharedGalleryFileSystemUtilities.createPicture(basePath, album, picture, data);
            if(new_name != null && picture.equalsIgnoreCase(new_name)) {
                sendToConsumers(album, "Updated at " + System.nanoTime());
                return Response.status(Response.Status.CREATED).entity(SharedGalleryFileSystemUtilities.removeExtension(new_name)).build();
            }
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @DELETE
    @Path("/{album}/{picture}&password={password}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePicture(@PathParam("album") String album, @PathParam("picture") String picture, @PathParam("password") String password) {
        if(validate(password)) {
            Boolean created = SharedGalleryFileSystemUtilities.deletePicture(basePath, album, picture);
            if(created) {
                sendToConsumers(album, "Updated at " + System.nanoTime());
                return Response.ok(true).build();
            }
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    private void sendToConsumers(String topic, String event) {
        System.out.println("[ PROXY ] Sending event to consumer: " + topic + " " + event);
        producer.send(new ProducerRecord<>(topic, event));
    }

    private static boolean validate(String password) {
        return password.equalsIgnoreCase(local_password);
    }

    public static void main(String[] args) throws Exception {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("JKS PASSWORD: ");
        char [] jks_password = reader.readLine().toCharArray();

        System.out.println("KEY PASSWORD: ");
        char [] key_password = reader.readLine().toCharArray();

        System.out.println("LOCAL PASSWORD: ");
        local_password = reader.readLine();

        basePath.mkdir();

        URI baseUri = UriBuilder.fromUri("https://" + InetAddress.getLocalHost().getHostAddress() + "/FileServerREST").port(9090).build();
        ResourceConfig config = new ResourceConfig();
        config.register(SharedGalleryServerREST.class);

        SSLContext sslContext = SSLContext.getInstance("TLSv1");

        KeyStore keyStore = KeyStore.getInstance("JKS");

        try (InputStream is = new FileInputStream(KEYSTORE)) {
            keyStore.load(is, jks_password);
        }

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keyStore, key_password);

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(keyStore);

        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());

        // Event dessimination - Producer

        Properties env = System.getProperties();
        Properties props = new Properties();

        props.put("zk.connect", env.getOrDefault("zk.connect", "localhost:2181/"));
        props.put("bootstrap.servers", env.getOrDefault("bootstrap.servers", "localhost:9092"));
        props.put("log.retention.ms", 5000);

        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("key.serializer", StringSerializer.class.getName());
        props.put("value.serializer", StringSerializer.class.getName());

        producer = new KafkaProducer<>(props);

        HttpServer server = JdkHttpServerFactory.createHttpServer(baseUri, config, sslContext);

        System.err.println("SharedGalleryServerREST: Started @ " + baseUri.toString());

        new Thread(new SharedGalleryClientDiscovery(baseUri.toString())).start();
    }

}



