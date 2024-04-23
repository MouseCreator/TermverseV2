package mouse.project.lib.web.parse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Service;
import mouse.project.lib.web.exception.BodyParsingException;

import java.util.Collection;
@Service
public class JacksonBodyParser implements BodyParser {

    private final ObjectMapper objectMapper;
    @Auto
    public JacksonBodyParser() {
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    public <T> T parse(String body, Class<T> expectedType) {
        try {
            return objectMapper.readValue(body, expectedType);
        } catch (JsonProcessingException e) {
            throw new BodyParsingException("Failed to parse body", e);
        }
    }

    @Override
    public <T> Collection<T> parseAll(String body, Class<T> expectedType) {
        try {
            CollectionType collectionType = objectMapper.getTypeFactory()
                    .constructCollectionType(Collection.class, expectedType);
            return objectMapper.readValue(body, collectionType);
        } catch (JsonProcessingException e) {
            throw new BodyParsingException("Failed to parse collection body", e);
        }
    }

    @Override
    public <T> T parseAttribute(String body, String attributeName, Class<T> expectedType) {
        try {
            JsonNode jsonNode = objectMapper.readTree(body);
            JsonNode attributeNode = jsonNode.get(attributeName);
            if (attributeNode != null) {
                return objectMapper.convertValue(attributeNode, expectedType);
            } else {
                throw new BodyParsingException("Attribute not found: " + attributeName);
            }
        } catch (JsonProcessingException e) {
            throw new BodyParsingException("Failed to parse attribute " + attributeName, e);
        }
    }

    @Override
    public String unparse(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new BodyParsingException("Failed to unparse object", e);
        }
    }

    @Override
    public <T> Collection<T> parseAllByAttribute(String body, String attributeName, Class<T> expectedType) {
        try {
            JsonNode jsonNode = objectMapper.readTree(body);
            JsonNode attributeNode = jsonNode.get(attributeName);
            if (attributeNode != null) {
                CollectionType collectionType = objectMapper.getTypeFactory()
                        .constructCollectionType(Collection.class, expectedType);
                return objectMapper.convertValue(attributeNode, collectionType);
            } else {
                throw new BodyParsingException("Attribute not found: " + attributeName);
            }
        } catch (JsonProcessingException e) {
            throw new BodyParsingException("Failed to parse attribute " + attributeName, e);
        }
    }
}
