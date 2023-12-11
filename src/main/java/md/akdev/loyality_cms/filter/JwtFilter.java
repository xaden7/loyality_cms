package md.akdev.loyality_cms.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import md.akdev.loyality_cms.model.JwtAuthentication;
import md.akdev.loyality_cms.service.DeviceService;
import md.akdev.loyality_cms.service.JwtProvider;
import md.akdev.loyality_cms.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

    private static final String AUTHORIZATION = "Authorization";
    private final JwtProvider jwtProvider;
    private final DeviceService deviceService;
    @Value("${jwt.secret.access}")
    String jwtAccessSecret;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc) throws IOException, ServletException {

        final String token = getTokenFromRequest((HttpServletRequest) request);
        final String deviceId = getDeviceId((HttpServletRequest) request);
        if (token != null ) {
            try {
                Claims tClaims = Jwts.parserBuilder().setSigningKey(jwtAccessSecret).build().parseClaimsJws(token).getBody();
                if (!tClaims.isEmpty()) {
                    final Claims claims = jwtProvider.getAccessClaims(token);
                    final JwtAuthentication jwtInfoToken = JwtUtils.generate(claims);
                    jwtInfoToken.setAuthenticated(true);
                    SecurityContextHolder.getContext().setAuthentication(jwtInfoToken);
                    if(deviceId != null){
                        deviceService.lastConnectDate(deviceId);
                    }
                }
            } catch (ExpiredJwtException expExc) {
                ObjectMapper mapper = new ObjectMapper();
                HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
                httpServletResponse.setContentType(APPLICATION_JSON_VALUE);
                Map<String, String> error = new HashMap<>();
                error.put("Message", "Token expired");
                error.put("ErrorDetails", expExc.getMessage());
                mapper.writeValue(httpServletResponse.getOutputStream(), error);
            } catch (UnsupportedJwtException unExc) {
                ObjectMapper mapper = new ObjectMapper();
                HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
                httpServletResponse.setContentType(APPLICATION_JSON_VALUE);
                Map<String, String> error = new HashMap<>();
                error.put("Message", "Unsupported jwt");
                error.put("ErrorDetails", unExc.getMessage());
                mapper.writeValue(httpServletResponse.getOutputStream(), error);
            } catch (Exception mjEx) {
                ObjectMapper mapper = new ObjectMapper();
                HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
                httpServletResponse.setContentType(APPLICATION_JSON_VALUE);
                Map<String, String> error = new HashMap<>();
                error.put("Message", "Malformed jwt");
                error.put("ErrorDetails", mjEx.getMessage());
                mapper.writeValue(httpServletResponse.getOutputStream(), error);
            }
        }
        fc.doFilter(request, response);
    }


    private String getTokenFromRequest(HttpServletRequest request) {
        final String bearer = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }

    private String getDeviceId(HttpServletRequest request){
        try {
            final String deviceId = request.getHeader("deviceId");
            return deviceId;
        }catch (Exception e){ return null; }

    }

}
