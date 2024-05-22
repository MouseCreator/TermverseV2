package mouse.project.termverseweb.security.kc;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
@Data
public class JWKSResponse {
    private List<JwksKey> keys;
    @Data
    public static class JwksKey {
        private String kid;
        private String kty;
        private String alg;
        private String use;
        private String n;
        private String e;
        private List<String> x5c;
        private String x5t;
        @JsonProperty("x5t#S256")
        private String x5c256;
    }
}
