package com.ntt.core.service.plugins.http;

import com.tencent.msdk.dns.DnsService;
import com.tencent.msdk.dns.core.IpSet;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class DnsServiceWrapper {

    private static final InetAddress[] EMPTY_ADDRESSES = new InetAddress[0];

    private static final String proxyHost = System.getProperty("http.proxyHost");
    private static final String proxyPort = System.getProperty("http.proxyPort");
    private static final boolean useHttpProxy = proxyHost != null && proxyPort != null;

    public static final boolean useHttpDns = true;

    public static InetAddress[] getAddrsByName(String hostname) {
        // 客户端启用HTTP代理时, 不使用HTTPDNS
        if (useHttpProxy || !useHttpDns) {
            // LocalDNS只取第一个IP
            InetAddress localAddr = getAddrByNameByLocal(hostname);
            return localAddr != null ? new InetAddress[]{localAddr} : EMPTY_ADDRESSES;
        }

        IpSet ipSet = DnsService.getAddrsByName(hostname);
        System.out.println("ipSet: " + ipSet);

        if (IpSet.EMPTY == ipSet) {
            System.err.println("isSet isEmpty");
            return EMPTY_ADDRESSES;
        }

        // 当前v6环境质量较差, 优先选择v4 IP, 且只考虑使用第一个v6 IP
        if (ipSet.v6Ips.length > 0 && ipSet.v4Ips.length > 0) {
            InetAddress[] v4Addresses = new InetAddress[ipSet.v4Ips.length];
            for (int i = 0; i < ipSet.v4Ips.length; i++) {
                try {
                    v4Addresses[i] = InetAddress.getByName(ipSet.v4Ips[i]);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
            InetAddress v6Address;
            try {
                v6Address = InetAddress.getByName(ipSet.v6Ips[0]);
            } catch (UnknownHostException e) {
                v6Address = null;
                e.printStackTrace();
            }
            return v6Address != null ? new InetAddress[]{v6Address} : v4Addresses;
        } else if (ipSet.v6Ips.length > 0) {
            try {
                InetAddress v6Address = InetAddress.getByName(ipSet.v6Ips[0]);
                return new InetAddress[]{v6Address};
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        } else if (ipSet.v4Ips.length > 0) {
            InetAddress[] v4Addresses = new InetAddress[ipSet.v4Ips.length];
            for (int i = 0; i < ipSet.v4Ips.length; i++) {
                try {
                    v4Addresses[i] = InetAddress.getByName(ipSet.v4Ips[i]);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
            return v4Addresses;
        }

        InetAddress localAddr = getAddrByNameByLocal(hostname);
        return localAddr != null ? new InetAddress[]{localAddr} : EMPTY_ADDRESSES;
    }

    private static InetAddress getAddrByNameByLocal(String hostname) {
        try {
            return InetAddress.getByName(hostname);
        } catch (UnknownHostException e) {
            return null;
        }
    }
}
