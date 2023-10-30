package it.leogioia.test;

import it.leogioia.Calcolatrice;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

@Slf4j
public class CalcolatriceTest {

    @Test
    public void testSomma(TestInfo testInfo) {
        log.info("Start test {}.{}", getClass().getSimpleName(), testInfo.getTestMethod().get().getName());

        Calcolatrice calcolatrice = new Calcolatrice();

        int num1 = 27;
        int num2 = 53;

        int expected = 80;

        Assertions.assertEquals(expected, calcolatrice.somma(num1, num2));
    }

    @Test
    public void testDifferenza(TestInfo testInfo) {
        log.info("Start test {}.{}", getClass().getSimpleName(), testInfo.getTestMethod().get().getName());

        Calcolatrice calcolatrice = new Calcolatrice();

        int num1 = 100;
        int num2 = 20;

        int expected = 80;

        Assertions.assertEquals(expected, calcolatrice.differenza(num1, num2));
    }
}
