package com.ryuunomi.inmotech.security.oauth2;

import com.ryuunomi.inmotech.entities.Usuario;
import com.ryuunomi.inmotech.enums.AuthProvider;
import com.ryuunomi.inmotech.enums.CapacidadUsuario;
import com.ryuunomi.inmotech.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        if (!"google".equals(registrationId)) {
            throw new OAuth2AuthenticationException("Solo se soporta login con Google");
        }

        Map<String, Object> attributes = oAuth2User.getAttributes();
        String providerId = (String) attributes.get("sub");
        String email = (String) attributes.get("email");
        String givenName = (String) attributes.get("given_name");
        String familyName = (String) attributes.get("family_name");
        String pictureUrl = (String) attributes.get("picture");

        if (email == null) {
            throw new OAuth2AuthenticationException("Google no proporciono un email");
        }

        Optional<Usuario> usuarioOpt = usuarioRepository.findByProviderAndProviderId(AuthProvider.GOOGLE, providerId);

        Usuario usuario;
        if (usuarioOpt.isPresent()) {
            usuario = usuarioOpt.get();
        } else {
            Optional<Usuario> usuarioPorEmail = usuarioRepository.findByEmail(email);
            if (usuarioPorEmail.isPresent()) {
                usuario = usuarioPorEmail.get();
                usuario.setProvider(AuthProvider.GOOGLE);
                usuario.setProviderId(providerId);
                if (pictureUrl != null && usuario.getImagen() == null) {
                    var img = new com.ryuunomi.inmotech.entities.ImagenUsuario();
                    img.setUrl(pictureUrl);
                    img.setUsuario(usuario);
                    usuario.setImagen(img);
                }
            } else {
                usuario = new Usuario();
                usuario.setEmail(email);
                usuario.setNombre(givenName != null ? givenName : "");
                usuario.setApellido(familyName != null ? familyName : "");
                usuario.setProvider(AuthProvider.GOOGLE);
                usuario.setProviderId(providerId);
                usuario.setFechaRegistro(LocalDate.now());
                usuario.setContrasenia(UUID.randomUUID().toString());

                Set<CapacidadUsuario> capacidades = new HashSet<>();
                capacidades.add(CapacidadUsuario.USUARIO);
                usuario.setCapacidades(capacidades);

                if (pictureUrl != null) {
                    var img = new com.ryuunomi.inmotech.entities.ImagenUsuario();
                    img.setUrl(pictureUrl);
                    img.setUsuario(usuario);
                    usuario.setImagen(img);
                }
            }
            usuarioRepository.save(usuario);
        }

        List<String> roles = usuario.getCapacidades().stream()
                .map(capacidad -> "ROLE_" + capacidad.name())
                .toList();

        Map<String, Object> userAttributes = new HashMap<>(attributes);
        userAttributes.put("email", usuario.getEmail());
        userAttributes.put("id", usuario.getId());
        userAttributes.put("roles", roles);
        userAttributes.put("provider", AuthProvider.GOOGLE.name());

        return new DefaultOAuth2User(
                roles.stream().map(org.springframework.security.core.authority.SimpleGrantedAuthority::new).toList(),
                userAttributes,
                "email"
        );
    }
}
