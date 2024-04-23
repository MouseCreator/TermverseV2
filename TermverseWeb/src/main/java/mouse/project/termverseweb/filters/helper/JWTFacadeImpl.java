package mouse.project.termverseweb.filters.helper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Service;

import java.security.PublicKey;

@Service
public class JWTFacadeImpl implements JWTFacade {
    private final JWTDecoder decoder;
    private PublicKey publicKey;
    @Auto
    public JWTFacadeImpl(JWTDecoder decoder) {
        this.decoder = decoder;
        publicKey = null;
    }

    @Override
    public Jws<Claims> getClaims(String jwtToken) {
        String realm = "termverse";
        if (publicKey == null) {
            publicKey = decoder.getPublicKey(realm);
        }
        return decoder.decode(jwtToken, publicKey);
    }
}
