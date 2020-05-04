package com.api.IP;

import com.api.model.IPs;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * @author 李星源
 * @version 1.0
 * @date 2020/03/21
 */
public class IPUtil {

    public static String getClientIpAddress(HttpServletRequest request) {
        String clientIp=request.getHeader("x-forwarded-for");
        if(clientIp==null||clientIp.length()==0||"unknown".equalsIgnoreCase(clientIp))
            clientIp=request.getHeader("Proxy-Client-IP");
        if(clientIp==null||clientIp.length()==0||"unknown".equalsIgnoreCase(clientIp))
            clientIp=request.getHeader("WL-Proxy-Client-IP");
        if(clientIp==null||clientIp.length()==0||"unknown".equalsIgnoreCase(clientIp))
            clientIp=request.getRemoteAddr();
        if (clientIp==null||clientIp.length()==0||clientIp.indexOf(":")>-1) {
            try {
                clientIp= InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                clientIp=null;
            }
        }
        return clientIp;
    }

    public static boolean isCommonIP(String ip,List<IPs> iPsList){
        int len=iPsList.size();
        if (iPsList==null)
            return true;
        for (int i=0;i<len;i++)
            if (ip.equals(iPsList.get(i)))
                return true;
        return false;
    }
}
