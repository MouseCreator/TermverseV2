package mouse.project.termverseweb.filters.helper;

import mouse.project.lib.files.PropertiesFileReader;
import mouse.project.lib.files.PropertyMap;
import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Service;

@Service
public class KeycloakDataProviderImpl implements KeycloakDataProvider {
    private KeycloakData keycloakData = null;
    private final PropertiesFileReader fileReader;
    @Auto
    public KeycloakDataProviderImpl(PropertiesFileReader fileReader) {
        this.fileReader = fileReader;
    }

    @Override
    public KeycloakData provide() {
        if (keycloakData == null) {
            keycloakData = readKeycloakData();
        }
        return keycloakData;
    }

    private KeycloakData readKeycloakData() {
        PropertyMap propertyMap = fileReader.readFile("src/main/resources/kc.secret");
        String kcClientId = propertyMap.getPropertyValue("KC_CLIENT_ID");
        String kcClientSecret = propertyMap.getPropertyValue("KC_CLIENT_SECRET");
        KeycloakData instance = new KeycloakData();
        instance.setClientId(kcClientId);
        instance.setClientSecret(kcClientSecret);
        return instance;
    }
}
