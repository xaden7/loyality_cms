package md.akdev.loyality_cms.utils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class NetworkUtils {
    public static boolean sourceIsAvailable(String address, int port) {
        try(Socket socket = new Socket())
        {
            InetSocketAddress socketAddress = new InetSocketAddress(address, port);
            socket.connect(socketAddress, 3000);

            return true;
        }
        catch(IOException unknownHost)
        {
            return false;
        }
    }
}
