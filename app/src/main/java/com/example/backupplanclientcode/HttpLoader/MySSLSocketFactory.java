package com.example.backupplanclientcode.HttpLoader;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.HttpHost;
import org.apache.http.HttpVersion;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.LayeredSchemeSocketFactory;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

public class MySSLSocketFactory extends SSLSocketFactory implements LayeredSchemeSocketFactory {
    SSLContext sslContext = SSLContext.getInstance("TLS");

    public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        super(truststore);
        X509TrustManager tm = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        this.sslContext.init(null, new TrustManager[]{tm}, null);
    }

    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException {
        return this.sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
    }

    public Socket createSocket() throws IOException {
        return this.sslContext.getSocketFactory().createSocket();
    }

    public void fixHttpsURLConnection() {
        HttpsURLConnection.setDefaultSSLSocketFactory(this.sslContext.getSocketFactory());
    }

    /* JADX WARNING: Removed duplicated region for block: B:24:0x0043 A[SYNTHETIC, Splitter:B:24:0x0043] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static KeyStore getKeystoreOfCA(java.io.InputStream r10) {
        /*
            r1 = 0
            r0 = 0
            java.lang.String r8 = "X.509"
            java.security.cert.CertificateFactory r3 = java.security.cert.CertificateFactory.getInstance(r8)     // Catch:{ CertificateException -> 0x0031 }
            java.io.BufferedInputStream r2 = new java.io.BufferedInputStream     // Catch:{ CertificateException -> 0x0031 }
            r2.<init>(r10)     // Catch:{ CertificateException -> 0x0031 }
            java.security.cert.Certificate r0 = r3.generateCertificate(r2)     // Catch:{ CertificateException -> 0x0054, all -> 0x0051 }
            if (r2 == 0) goto L_0x0016
            r2.close()     // Catch:{ IOException -> 0x002b }
        L_0x0016:
            r1 = r2
        L_0x0017:
            java.lang.String r7 = java.security.KeyStore.getDefaultType()
            r6 = 0
            java.security.KeyStore r6 = java.security.KeyStore.getInstance(r7)     // Catch:{ Exception -> 0x004c }
            r8 = 0
            r9 = 0
            r6.load(r8, r9)     // Catch:{ Exception -> 0x004c }
            java.lang.String r8 = "ca"
            r6.setCertificateEntry(r8, r0)     // Catch:{ Exception -> 0x004c }
        L_0x002a:
            return r6
        L_0x002b:
            r4 = move-exception
            r4.printStackTrace()
            r1 = r2
            goto L_0x0017
        L_0x0031:
            r5 = move-exception
        L_0x0032:
            r5.printStackTrace()     // Catch:{ all -> 0x0040 }
            if (r1 == 0) goto L_0x0017
            r1.close()     // Catch:{ IOException -> 0x003b }
            goto L_0x0017
        L_0x003b:
            r4 = move-exception
            r4.printStackTrace()
            goto L_0x0017
        L_0x0040:
            r8 = move-exception
        L_0x0041:
            if (r1 == 0) goto L_0x0046
            r1.close()     // Catch:{ IOException -> 0x0047 }
        L_0x0046:
            throw r8
        L_0x0047:
            r4 = move-exception
            r4.printStackTrace()
            goto L_0x0046
        L_0x004c:
            r4 = move-exception
            r4.printStackTrace()
            goto L_0x002a
        L_0x0051:
            r8 = move-exception
            r1 = r2
            goto L_0x0041
        L_0x0054:
            r5 = move-exception
            r1 = r2
            goto L_0x0032
        */
        throw new UnsupportedOperationException("Method not decompiled: com.backupplan.app.HttpLoader.MySSLSocketFactory.getKeystoreOfCA(java.io.InputStream):java.security.KeyStore");
    }

    public static KeyStore getKeystore() {
        KeyStore trustStore = null;
        try {
            trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            return trustStore;
        } catch (Throwable t) {
            t.printStackTrace();
            return trustStore;
        }
    }

    public static SSLSocketFactory getFixedSocketFactory() {
        try {
            SSLSocketFactory socketFactory = new MySSLSocketFactory(getKeystore());
            socketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            return socketFactory;
        } catch (Throwable t) {
            t.printStackTrace();
            return SSLSocketFactory.getSocketFactory();
        }
    }

    public static DefaultHttpClient getNewHttpClient(KeyStore keyStore) {
        try {
            SSLSocketFactory sf = new MySSLSocketFactory(keyStore);
            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme(HttpHost.DEFAULT_SCHEME_NAME, PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
            return new DefaultHttpClient(new ThreadSafeClientConnManager(params, registry), params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }

    @Override
    public Socket createLayeredSocket(Socket socket, String target, int port, boolean autoClose) throws IOException, UnknownHostException {
        return null;
    }

    @Override
    public Socket createSocket(HttpParams params) throws IOException {
        return null;
    }

    @Override
    public Socket connectSocket(Socket sock, InetSocketAddress remoteAddress, InetSocketAddress localAddress, HttpParams params) throws IOException, UnknownHostException, ConnectTimeoutException {
        return null;
    }
}
