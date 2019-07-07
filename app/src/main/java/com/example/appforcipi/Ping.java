package com.example.appforcipi;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;

import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;

public class Ping{ //extends AsyncTask<Context,Void, String> {

    //ArrayList<ArrayList<Context>,ArrayList<String>>

    public String net = "NO_CONNECTION";
    public String host;
    public String ip;
    public int dns = Integer.MAX_VALUE;
    public int cnt = Integer.MAX_VALUE;


    public static Ping ping(URL url, Context ctx) {
        Ping r = new Ping();
        if (isNetworkConnected(ctx)) {
            r.net = getNetworkType(ctx);
            try {
                String hostAddress;
                long start = System.currentTimeMillis();
                hostAddress = InetAddress.getByName(url.getHost()).getHostAddress();
                long dnsResolved = System.currentTimeMillis();
                Socket socket = new Socket(hostAddress, url.getPort());
                socket.close();
                long probeFinish = System.currentTimeMillis();
                r.dns = (int) (dnsResolved - start);
                r.cnt = (int) (probeFinish - dnsResolved);
                r.host = url.getHost();
                r.ip = hostAddress;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return r;
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Nullable
    public static String getNetworkType(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            return activeNetwork.getTypeName();
        }
        return null;
    }

    @Override
    public String toString() {
        return "{" + "net='" + net + '\'' +
                ", host='" + host + '\'' +
                ", ip='" + ip + '\'' +
                ", dns=" + dns +
                ", cnt=" + cnt +
                '}';
    }
    /*@Override
    protected String doInBackground(Context... contexts) {
        return null;
    }*/
}