package org.tanuneko.im.net.util;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by neko32 on 2016/12/06.
 */
@SuppressWarnings("ALL")
public class NetworkIFUtil {

    private NetworkIFUtil() {
        throw new IllegalStateException("no use");
    }

    public static InetAddress findPreferredNetIF(final String preferredIF) throws IOException {
        InetAddress inetAddress = null;
        Enumeration<NetworkInterface> inet = NetworkInterface.getNetworkInterfaces();
        NetworkInterface inetIf;
        while(inet.hasMoreElements()) {
            inetIf = inet.nextElement();
            if(inetIf.getName().equalsIgnoreCase(preferredIF)) {
                inetAddress = selectBestFitAddress(inetIf.getInetAddresses());
                break;
            }
        }
        if(inetAddress == null) {
            inetAddress = InetAddress.getLocalHost();
        }
        return inetAddress;
    }

    public static List<String> getNetworkIFList() throws SocketException {
        Enumeration<NetworkInterface> inet = NetworkInterface.getNetworkInterfaces();
        NetworkInterface inetIf;
        List<String> ifs = new ArrayList<>();
        while(inet.hasMoreElements()) {
            inetIf = inet.nextElement();
            ifs.add(inetIf.getName());
        }
        return ifs;
    }

    private static InetAddress selectBestFitAddress(Enumeration<InetAddress> addrs) {
        InetAddress addr = null;
        // assume first Inet4Address is the best one to use
        while(addrs.hasMoreElements()) {
            addr = addrs.nextElement();
            if(addr instanceof Inet4Address) {
                break;
            }
        }
        return addr;
    }
}
