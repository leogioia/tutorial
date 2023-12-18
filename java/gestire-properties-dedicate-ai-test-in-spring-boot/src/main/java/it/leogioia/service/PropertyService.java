package it.leogioia.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class PropertyService {

    public int getInt(String p) {
        String prop = Optional.ofNullable(p).orElse("").trim();
        return Integer.parseInt(prop);
    }

    public String[] getArrayString(String p) {
        String prop = Optional.ofNullable(p).orElse("");
        return prop.split(",");
    }

    public Map<String, Integer> getMapStringInt(String p) {
        String prop = Optional.ofNullable(p).orElse("");

        Map<String, Integer> result = new HashMap<>();

        Arrays.stream(prop.split(","))
                .forEach(pr -> result.put(
                        pr.substring(0, pr.indexOf(":")),
                        Integer.parseInt(pr.substring(pr.indexOf(":") + 1))
                ));

        return result;
    }
}
