package it.leogioia.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@Slf4j
@SpringBootTest
public class PropertyServiceTest {

    @Value("${mia.property.string}")
    String miaPropertyString;

    @Value("#{propertyService.getInt('${mia.property.int}')}")
    Integer miaPropertyInt;

    @Value("#{propertyService.getArrayString('${mia.property.array.string}')}")
    String[] miaPropertyArrayString;

    @Value("#{propertyService.getMapStringInt('${mia.property.mappa.string.int}')}")
    Map miaPropertyMappaStringInt;

    @Test
    public void testProperties(TestInfo testInfo) {
        log.info("Start test {}.{}", getClass().getSimpleName(), testInfo.getDisplayName());

        log.info("miaPropertyString: {}", miaPropertyString);
        log.info("miaPropertyInt: {}", miaPropertyInt);
        log.info("miaPropertyArrayString: {}", miaPropertyArrayString);
        log.info("miaPropertyMappaStringInt: {}", miaPropertyMappaStringInt);
    }
}
