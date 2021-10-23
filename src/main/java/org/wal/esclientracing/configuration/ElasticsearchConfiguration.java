package org.wal.esclientracing.configuration;

import brave.Tracing;
import brave.httpasyncclient.TracingHttpAsyncClientBuilder;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static org.elasticsearch.client.RestClientBuilder.*;

@Configuration
public class ElasticsearchConfiguration {

    private static final String USER_AGENT_HEADER_VALUE = String.format(Locale.ROOT, "elasticsearch-java/%s (Java/%s)",
            "7.15.1", System.getProperty("java.version"));

    @Bean
    RestClientBuilder restClientBuilder(Tracing tracing, @Value("${elasticsearch.endpoints[0]}") String endpoints) {

        //final var httpHosts = endpoints.stream().map(HttpHost::new).toArray(HttpHost[]::new);
        final var httpHosts = HttpHost.create(endpoints);


        return RestClient.builder(httpHosts)

                .setHttpClientConfigCallback(httpClientBuilder -> {
                    RequestConfig.Builder requestConfigBuilder = RequestConfig.custom()
                            .setConnectTimeout(DEFAULT_CONNECT_TIMEOUT_MILLIS)
                            .setSocketTimeout(DEFAULT_SOCKET_TIMEOUT_MILLIS);
                    try {
                        return TracingHttpAsyncClientBuilder.create(tracing)
                                .setDefaultRequestConfig(requestConfigBuilder.build())
                                //default settings for connection pooling may be too constraining
                                .setMaxConnPerRoute(DEFAULT_MAX_CONN_PER_ROUTE).setMaxConnTotal(DEFAULT_MAX_CONN_TOTAL)
                                .setSSLContext(SSLContext.getDefault())
                                .setUserAgent(USER_AGENT_HEADER_VALUE)
                                .setTargetAuthenticationStrategy(new PersistentCredentialsAuthenticationStrategy());
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    return httpClientBuilder;

                });
    }

    @Bean
    RestHighLevelClient high(RestClientBuilder restClient) {
        return new RestHighLevelClient(restClient);
    }
}
