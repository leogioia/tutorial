package it.leogioia.test.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.IOException;
import java.util.List;

@Slf4j
public class JsonUtil {

    private static ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        return objectMapper;
    }

    public static <T> List<T> fromJsonToObjectList(Class<T> objectType, String jsonSource) throws IOException {
        ObjectMapper objectMapper = objectMapper();
        return objectMapper.readValue(jsonSource, objectMapper.getTypeFactory().constructCollectionType(List.class, objectType));
    }

    public static <T> T fromJsonToObject(Class<T> objectType, String jsonSource) throws IOException {
        return fromJsonToObject(objectType, jsonSource, new String[][]{});
    }

    public static <T> T fromJsonToObject(Class<T> objectType, String jsonSource, String[][] replacement) throws IOException {
        for (String[] s1 : replacement) {
            jsonSource = jsonSource.replace(s1[0], s1[1]);
        }
        ObjectMapper objectMapper = objectMapper();
        return objectMapper.readValue(jsonSource, objectType);
    }

    public static String fromObjectToJson(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = objectMapper();
        return objectMapper.writeValueAsString(object);
    }

    public static void compareObject(Object expected, Object actual) throws JsonProcessingException, JSONException {
        ObjectMapper objectMapper = objectMapper();
        String expectedJson = objectMapper.writeValueAsString(expected);
        String actualJson = objectMapper.writeValueAsString(actual);
        log.debug("Comparing json -> expected: {} | actual {}", expectedJson, actualJson);
        JSONAssert.assertEquals(expectedJson, actualJson, true);
    }

    public static void compareObject(String expectedJson, Object actual) throws JsonProcessingException, JSONException {
        ObjectMapper objectMapper = objectMapper();
        String actualJson = objectMapper.writeValueAsString(actual);
        log.debug("Comparing json -> expected: {} | actual {}", expectedJson, actualJson);
        JSONAssert.assertEquals(expectedJson, actualJson, true);
    }

    public static void compareObject(String expectedJson, String actualJson) throws JsonProcessingException, JSONException {
        log.debug("Comparing json -> expected: {} | actual {}", expectedJson, actualJson);
        JSONAssert.assertEquals(expectedJson, actualJson, true);
    }
}
