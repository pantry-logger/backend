package com.pantrylogger.restapi.security;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import com.pantrylogger.domain.user.Email;
import com.pantrylogger.domain.user.ExternalAuthId;
import com.pantrylogger.domain.user.User;
import com.pantrylogger.domain.user.UserRepositoryPort;
import com.pantrylogger.domain.user.Username;

@Component
public class JwtToUserDetailsConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    private static final Logger LOGGER = LoggerFactory.getLogger(
            JwtToUserDetailsConverter.class);
    private final UserRepositoryPort userRepository;
    private final KeycloakRealmRoleConverter authoritiesConverter = new KeycloakRealmRoleConverter();

    public JwtToUserDetailsConverter(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        String keycloakId = jwt.getSubject(); // Keycloak's user UUID
        User user = userRepository.getByExternalAuthId(new ExternalAuthId(
                        keycloakId))
                .orElseGet(() -> provisionUserFromToken(jwt)); // first-login provisioning, optional

        Set<GrantedAuthority> authorities = authoritiesConverter.convert(
                jwt);
        assert authorities != null;
        CustomUserDetails principal = new CustomUserDetails(user, authorities);

        return new JwtAuthenticationToken(
                jwt,
                authorities,
                user.getUsername().toString()
        ) {
            @Override
            public Object getPrincipal() {
                return principal; // override so @AuthenticationPrincipal gives CustomUserDetails
            }
        };
    }

    private User provisionUserFromToken(Jwt jwt) {
        LOGGER.debug(jwt.getClaims().toString());
        User user = new User(
                new ExternalAuthId(jwt.getSubject()),
                new Email(jwt.getClaimAsString("email")),
                new Username(jwt.getClaimAsString("preferred_username")),
                jwt.getClaimAsString("given_name"),
                jwt.getClaimAsString("family_name")
        );
        return userRepository.save(user);
    }
}
