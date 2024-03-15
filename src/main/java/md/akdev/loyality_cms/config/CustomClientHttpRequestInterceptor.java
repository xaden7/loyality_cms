package md.akdev.loyality_cms.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;


import java.io.IOException;

public class CustomClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {
    private final Logger LOG = LoggerFactory.getLogger(CustomClientHttpRequestInterceptor.class);
    @Override
    public  ClientHttpResponse   intercept  (HttpRequest request,byte[] bytes, ClientHttpRequestExecution execution) throws IOException {
        LOG.info("URI: {}", request.getURI());
        return execution.execute(request, bytes);
    }
}
