package cn.sanii.earth.util;

import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import cn.sanii.earth.model.IP;
import cn.sanii.earth.model.LocalCookieJar;
import okhttp3.OkHttpClient;
import org.apache.commons.lang3.StringUtils;

import javax.net.ssl.*;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.SecureRandom;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @Author: shouliang.wang
 * @Date: 2018/10/15 11:50
 * @Description: 创建OKHTTP客户端
 */
@Slf4j
public class OkHttpUtil {

    private static OkHttpClient ok = new OkHttpClient();

    private OkHttpUtil() {
    }

    /**
     * 设置代理等创建http客户端
     *
     * @param ip
     * @return
     */
    public static OkHttpClient getOkHttpClinet(IP ip) {
        return OkHttpClient(ip);
    }

    public static OkHttpClient getDefault() {
        return OkHttpClient(null);
    }


    private static OkHttpClient OkHttpClient(IP ip) {
        Stopwatch started = Stopwatch.createStarted();
        OkHttpClient.Builder builder = ok.newBuilder()
                .sslSocketFactory(createSSLSocketFactory())
                .followRedirects(true)  //自动重定向
                .followSslRedirects(false)
                .connectTimeout(30, TimeUnit.SECONDS)       //设置连接超时
                .readTimeout(30, TimeUnit.SECONDS)          //设置读超时
                .writeTimeout(30, TimeUnit.SECONDS)
                .cookieJar(new LocalCookieJar());//为OkHttp设置自动携带Cookie的功能
        try {
            if (!Objects.isNull(ip) && StringUtils.isNotBlank(ip.getHost())) {
                builder.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip.getHost(), ip.getPort())));
            }
            ok = builder.build();
            return ok;
        } catch (Exception e) {
            log.error("getbillOkHttpClinet error:{}", e.getMessage());
        } finally {
            log.info("getbillOkHttpClinet finish:{}", started.elapsed(TimeUnit.MILLISECONDS));
        }
        return builder.build();
    }


    private static class TrustAllCerts implements X509TrustManager {

        @Override
        public void checkClientTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws java.security.cert.CertificateException {

        }

        @Override
        public void checkServerTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws java.security.cert.CertificateException {

        }

        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[0];
        }
    }

    private static class TrustAllHostnameVerifier implements HostnameVerifier {

        @Override
        public boolean verify(String s, SSLSession sslSession) {
            return true;
        }
    }

    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());
            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
            log.error("createSSLSocketFactory error", e);
        }
        return ssfFactory;
    }
}
