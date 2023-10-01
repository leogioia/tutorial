package it.leogioia.java.undici.maven;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {

    public static void main(String[] args) {
        String params = String.join(",", args);

        log.info("Starting with params [{}]", params);
    }
}
