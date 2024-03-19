//package md.akdev.loyality_cms.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

//@Configuration
//public class RequestLoggingFilterConfig  {
//
//
//    @Bean
//    public AbstractRequestLoggingFilter logFilter() throws ServletException {
//        AbstractRequestLoggingFilter filter = new AbstractRequestLoggingFilter() {
//
//            @Override
//            protected void beforeRequest(HttpServletRequest request,String message) {
//                logger.info(message);
//            }
//
//            @Override
//            protected void afterRequest( HttpServletRequest request, String message) {
//
//            }
//        };
//
//        filter.afterPropertiesSet( );
//
//        filter.setIncludeQueryString(true);
//        filter.setIncludePayload(true);
//        filter.setMaxPayloadLength(10000);
//        filter.setIncludeHeaders(true);
//        filter.setIncludeClientInfo(true);
//        return filter;
//    }


//}
