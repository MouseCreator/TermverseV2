package mouse.project.termverseweb.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.security.interfaces.RSAPublicKey;

import static org.junit.jupiter.api.Assertions.*;

class KeyServiceTest {
    private KeyService keyService;

    @BeforeEach
    void setUp() {
        keyService = new KeyService();
    }

    @Test
    void convertToRSAPublicKey() {
        String token = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJYUFZfb1Q5ZFVjQWFUNFlRV0tPVURnTW1OaFIxem5nWTJyYkRHZVBNOUhFIn0.eyJleHAiOjE3MTYyOTEyMDQsImlhdCI6MTcxNjI4OTQwNCwianRpIjoiZjdmYWRkNzYtN2JkYi00YzEzLWI1ZWItYjc0OTExMTU1NjY0IiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MTgwL3JlYWxtcy90ZXJtdmVyc2UiLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiMzQ2NzFmNDQtN2U1Yy00MGJlLWExNmItN2RmMGI3ZTZiYzNiIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoidGVybXZlcnNlLWFwcCIsInNlc3Npb25fc3RhdGUiOiJkODM0YTA1ZC0wOWEzLTRhMzYtOWUyNy1hYzQ3YWUwY2QyMjgiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbImh0dHA6Ly9sb2NhbGhvc3Q6ODA4MCIsImh0dHA6Ly9sb2NhbGhvc3Q6MzAwMCJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiZGVmYXVsdC1yb2xlcy10ZXJtdmVyc2UiLCJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJlbWFpbCBwcm9maWxlIiwic2lkIjoiZDgzNGEwNWQtMDlhMy00YTM2LTllMjctYWM0N2FlMGNkMjI4IiwiZW1haWxfdmVyaWZpZWQiOnRydWUsInByZWZlcnJlZF91c2VybmFtZSI6InVzZXIxIiwiZW1haWwiOiJ1c2VyMUBtYWlsLmNvbSJ9.n3MoRdOLiUV_TaX6tIA4AbbI_g-HHUXBhOnVTIKp-5NvhGDWs2c_O8Q7-HQojYjQDLvsOpkqnPr3TuXF3sRfapbOtPmvgom5ENvMiT5z08dFjnrtzjrGPDbyf7rkebprsH1UnE6DIWOtP62q-DsCxQnZj04qYCEnFfNzvyCbPy5uFE2eO42cvLNxcdUzlZA0oN19tFpOpEpJ2INwplYmK1sSKfSO8oY2sashstZr0wTIfH0HP_-cr_mcNXW4HX2IcCup1jB_6M8ABpkUA60JMdWX6hrCfxZ9CIpnYgweAYpNq1-c3Cf6wWLEilL5oGYRsxOn54i1BX8EodsBUkIWBA";
        String keysJson =
                """
                {
                    "keys": [
                        {
                            "kid": "L-ufaI47oCb7KellZug-QYWXxe_KqOgV7_e4Lt4nLe8",
                            "kty": "RSA",
                            "alg": "RSA-OAEP",
                            "use": "enc",
                            "n": "4FVHPHqdbspn1ePSYM6MjHyvYyfZf-nEzUd6Q82xaoPnp_Kzo5Vft7XmhktNFGPLfY2PjGeAaq50kKZrH6TyOU3BDsxbu7tc8WnmPYS2GPC6r4cJrgNXl98xDpoIivqji7-iq3wrI6Ng8n4RAXJCpvSiC3wGQTGJK9xKhha80AtXT5bCFDwUISVXPt52kT7wk5gody-Xm6yDHWNJGogWwaEvgQN0mtadNaiBxsC9A89C64_hcL2X9fJqPrWQTXmhXDT_9f_9sC4U8G1zkIanG-QO9PFSKpASNaRfPiijB8cTxEBkiJQo6iG-iYHjC50e2nvy6FuMtIXTF4xEsgspWQ",
                            "e": "AQAB",
                            "x5c": [
                                "MIICoTCCAYkCBgGPAOFLLzANBgkqhkiG9w0BAQsFADAUMRIwEAYDVQQDDAl0ZXJtdmVyc2UwHhcNMjQwNDIxMTMzNjU1WhcNMzQwNDIxMTMzODM1WjAUMRIwEAYDVQQDDAl0ZXJtdmVyc2UwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQDgVUc8ep1uymfV49JgzoyMfK9jJ9l/6cTNR3pDzbFqg+en8rOjlV+3teaGS00UY8t9jY+MZ4BqrnSQpmsfpPI5TcEOzFu7u1zxaeY9hLYY8LqvhwmuA1eX3zEOmgiK+qOLv6KrfCsjo2DyfhEBckKm9KILfAZBMYkr3EqGFrzQC1dPlsIUPBQhJVc+3naRPvCTmCh3L5ebrIMdY0kaiBbBoS+BA3Sa1p01qIHGwL0Dz0Lrj+FwvZf18mo+tZBNeaFcNP/1//2wLhTwbXOQhqcb5A708VIqkBI1pF8+KKMHxxPEQGSIlCjqIb6JgeMLnR7ae/LoW4y0hdMXjESyCylZAgMBAAEwDQYJKoZIhvcNAQELBQADggEBAJuasO1gOv+J0TA4CUOetWzc7x9HyYtrLH9Ycu2dIxOVb7EizR23MWUBkDbT7mGO/FoyRwRL3IJTVvOxGG3lF/5EvQJ1VbgbUVXCmuFtadKH44A67L3P/VPf/Dl9qBfTO3/M3PD91hehoEX99CCHsLYFe169cQkzU4nbloZpJP5eIgho7ax0Te9mOJMKgdXD+PGp6hEXechdkqgsWHt0z184u2mSD8ycF6/US/t1324wE+YnWSY1r3NaGzocMerwY0YkyMfLzWpznQoFNhVI9lKdqySn9635Fd2el/qCHQZufevpqZfY8u9KxwUxmdLFoIKzyB+efNVmMaWDGmq1OjU="
                            ],
                            "x5t": "3G68s56ALFbvninFigSE4A8J0NM",
                            "x5t#S256": "f2HSmZu45blmoi8_F9GTMCJh4yHHq2-br1_qyZPLSiw"
                        },
                        {
                            "kid": "XPV_oT9dUcAaT4YQWKOUDgMmNhR1zngY2rbDGePM9HE",
                            "kty": "RSA",
                            "alg": "RS256",
                            "use": "sig",
                            "n": "tr_fa2c3kO2mkAe26KZoEIyK2AJoOKOJJ3RBJ95CizRXeAwe7n6L3ZnwAWjgqkIFyqzwe2OY9qcV4gT6la5dPg4_S8QCNkCm7SPp3I6-WEsuKfGyP9HZiJT5AXKVlt1rBuWMPMIbuAF6HZVFAmX6o4UUEhcB9C7wjF-cDb70zrxtm0G9lDptSequg-DLXDET7xTItOcTdMQ71PbgsXR98NzQhDhIqtxduRDGJ1VG3GdCXQmea357N-i0_oCD1SmIuMfZsd1A2TtfyG6JQBpeIyDabsOXvh11L-RX5Li4a1NAg5eEaCQ3ngF0lM1g1bxxs3rHHGBgvMt1ik8feSRe9Q",
                            "e": "AQAB",
                            "x5c": [
                                "MIICoTCCAYkCBgGPAOFKxTANBgkqhkiG9w0BAQsFADAUMRIwEAYDVQQDDAl0ZXJtdmVyc2UwHhcNMjQwNDIxMTMzNjU1WhcNMzQwNDIxMTMzODM1WjAUMRIwEAYDVQQDDAl0ZXJtdmVyc2UwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQC2v99rZzeQ7aaQB7bopmgQjIrYAmg4o4kndEEn3kKLNFd4DB7ufovdmfABaOCqQgXKrPB7Y5j2pxXiBPqVrl0+Dj9LxAI2QKbtI+ncjr5YSy4p8bI/0dmIlPkBcpWW3WsG5Yw8whu4AXodlUUCZfqjhRQSFwH0LvCMX5wNvvTOvG2bQb2UOm1J6q6D4MtcMRPvFMi05xN0xDvU9uCxdH3w3NCEOEiq3F25EMYnVUbcZ0JdCZ5rfns36LT+gIPVKYi4x9mx3UDZO1/IbolAGl4jINpuw5e+HXUv5FfkuLhrU0CDl4RoJDeeAXSUzWDVvHGzesccYGC8y3WKTx95JF71AgMBAAEwDQYJKoZIhvcNAQELBQADggEBAFCTPfihGKMeg/21WbJUSVadJnkyWtQr9fXZZxWYNSYCWxOupism69akJ/1LnbM8J9uELfuRc7saDApkwogrXM8Z0WE47TfC6y0vt5HZep6G6nV8UVZpZ0roQ917Fx26+HnFJPYkCxTcsZcVFqAATgvpjgKtBoHPGTF9UNFpN9TxTf9zBPi64Tg5Jd3U2iIMKmrLAG1v4bxL44PjaT878BweqI89F8/oFAb0LQ4+80Nnv1yYysX9qm/mpYd1amTZP+m+B/l6xa2DZcBcxXWP2HuuMYNw0xhL62FIp7IPzWYq82XmIWIaPabClwRpJ7inNfp/kYuisKr6zGCh3PKACgc="
                            ],
                            "x5t": "uh08mogMvz9CvKUmCMPtohyng_4",
                            "x5t#S256": "E7xrNCX22JrgvN7VTx8lPRKHiovB7RJujnSGf6Ud0KU"
                        }
                    ]
                }
                """;
        RSAPublicKey rsaPublicKey = keyService.convertToRSAPublicKey(keysJson);
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withPublicKey(rsaPublicKey).build();
        Jwt decodedJwt = decoder.decode(token);
        assertNotNull(decodedJwt);
    }
}