package md.akdev.loyality_cms.service;

import io.jsonwebtoken.Claims;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import md.akdev.loyality_cms.exception.JwtAuthException;
import md.akdev.loyality_cms.model.ClientsModel;
import md.akdev.loyality_cms.model.jwt.BlockedToken;
import md.akdev.loyality_cms.model.jwt.JwtAuthentication;
import md.akdev.loyality_cms.model.jwt.JwtResponse;
import md.akdev.loyality_cms.repository.BlockedTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtAuthService {
    private final ClientService clientService;
    private final Map<String, String> refreshStorage = new HashMap<>();
    private final JwtProvider jwtProvider;
    private final BlockedTokenRepository blockedTokenRepository;

    public JwtResponse login(String phoneNumber, String codeCard) {
        final ClientsModel user = clientService.getClientByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new JwtAuthException("Клиент не найден"));
        if (user.getCodeCard().equals(codeCard)) {
            final String accessToken = jwtProvider.generateAccessToken(user);
            final String refreshToken = jwtProvider.generateRefreshToken(user);
            refreshStorage.put(user.getPhoneNumber(), refreshToken);
            return new JwtResponse(accessToken, refreshToken);
        } else {
            throw new JwtAuthException("Не найдена карта по баркоду");
        }
    }

    public JwtResponse login(ClientsModel client) {
            final String accessToken = jwtProvider.generateAccessToken(client);
            final String refreshToken = jwtProvider.generateRefreshToken(client);
            refreshStorage.put(client.getPhoneNumber(), refreshToken);
            return new JwtResponse(accessToken, refreshToken);
    }

    public JwtResponse getAccessToken(@NonNull String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken) && blockedTokenRepository.findByToken(refreshToken).isEmpty()) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final ClientsModel user = clientService.getClientByPhoneNumber(login)
                        .orElseThrow(() -> new JwtAuthException("Клиент не найден"));
                final String accessToken = jwtProvider.generateAccessToken(user);
                return new JwtResponse(accessToken, null);
            }
        }
        return new JwtResponse(null, null);
    }

    public JwtResponse refresh(@NonNull String refreshToken) {
        final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
        final String login = claims.getSubject();

        if (jwtProvider.validateRefreshToken(refreshToken)) {
            /*
            * Отключил проверку по МАП-у, так как это работает только до перезагрузки, такое есть смысл если
            * хранить токены в базе (переделал в базе), в МАПе нет смысла
            * vbrizitschi 31.07.2027 */
//            final String saveRefreshToken = refreshStorage.get(login);
//            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
            if(blockedTokenRepository.findByToken(refreshToken).isEmpty()){
                BlockedToken blockedToken = new BlockedToken(refreshToken);

                final ClientsModel user = clientService.getClientByPhoneNumber(login)
                        .orElseThrow(() -> new JwtAuthException("Клиент не найден"));
                final String accessToken = jwtProvider.generateAccessToken(user);
                final String newRefreshToken = jwtProvider.generateRefreshToken(user);
                refreshStorage.put(user.getPhoneNumber(), newRefreshToken);
                blockedTokenRepository.save(blockedToken);
                return new JwtResponse(accessToken, newRefreshToken);
            }
        }
        throw new JwtAuthException( login  + ": невалидный refresh токен");
    }

    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }
}
