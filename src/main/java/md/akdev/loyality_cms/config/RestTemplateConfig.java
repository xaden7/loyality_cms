package md.akdev.loyality_cms.config;

import org.apache.hc.client5.http.classic.HttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate clientHttpRequestFactory(HttpClient httpClient){
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();

        clientHttpRequestFactory.setHttpClient(httpClient);

        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor("OnicaIon", "tbND3tkEDVKO"));
        restTemplate.setErrorHandler(new CustomClientErrorHandler());
        restTemplate.getInterceptors().add(new CustomClientHttpRequestInterceptor());
        return restTemplate;
    }

}
