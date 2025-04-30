//package md.akdev.loyality_cms.config;
//
//import com.fasterxml.jackson.annotation.JsonAutoDetect;
//import com.fasterxml.jackson.core.JsonGenerator;
//import com.fasterxml.jackson.databind.*;
//import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
//import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.io.IOException;
//@Configuration
//public class ObjectMapperConfig {
//    @Bean
//    public ObjectMapper objectMapper(){
//
//        DefaultSerializerProvider sp = new DefaultSerializerProvider.Impl();
//        sp.setNullValueSerializer(new JsonSerializer<Object>() {
//            public void serialize(Object value, JsonGenerator jgen,
//                                  SerializerProvider provider)
//                    throws IOException
//            {
//                jgen.writeString("");
//            }
//        });
//
//        return new ObjectMapper()
//                .setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY))
//                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
//                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
//                .findAndRegisterModules()
//                .setSerializerProvider(sp
//                )
//                ;
//    }
//}
