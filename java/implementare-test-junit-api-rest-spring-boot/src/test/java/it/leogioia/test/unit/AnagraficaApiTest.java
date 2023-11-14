package it.leogioia.test.unit;

import it.leogioia.model.Anagrafica;
import it.leogioia.test.utils.FileUtil;
import it.leogioia.test.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AnagraficaApiTest {

    @LocalServerPort
    private int port;

    @Test
    public void testGetAnagrafiche(TestInfo testInfo) throws Exception {
        log.info("Start test {}", testInfo.getTestMethod().get().getName());
        String requestUrl = "http://localhost:" + port + "/api/v1/anagrafiche";

        OkHttpClient client = new OkHttpClient()
                .newBuilder()
                .build();

        Request request = new Request.Builder()
                .url(requestUrl)
                .build();

        Call call = client.newCall(request);

        Response response = call.execute();

        Assertions.assertEquals(200, response.code());
        Assertions.assertTrue(JsonUtil.fromJsonToObjectList(Anagrafica.class, response.body().string()).size() > 0);
    }

    @ParameterizedTest
    @CsvSource({
            "2,Giuseppe,Bianchi,Via Garibaldi",
            "3,Marco,Verdi,Via Marconi"
    })
    public void testPostAndGetAnagrafica(String id, String nome, String cognome, String indirizzo, TestInfo testInfo) throws Exception {
        log.info("Start test {} with param: id={}, nome={}, cognome={}, indirizzo={}",
                testInfo.getTestMethod().get().getName(),
                id, nome, cognome, indirizzo);

        OkHttpClient client = new OkHttpClient()
                .newBuilder()
                .build();

        String requestUrl = "http://localhost:" + port + "/api/v1/anagrafiche";

        String expected = FileUtil.readResourceFile("expected/anagrafica.json")
                .replace("${id}", id)
                .replace("${nome}", nome)
                .replace("${cognome}", cognome)
                .replace("${indirizzo}", indirizzo);

        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json"), expected);

        Request request = new Request.Builder()
                .url(requestUrl)
                .post(requestBody)
                .build();

        Call call = client.newCall(request);

        Response response = call.execute();

        Assertions.assertEquals(200, response.code());

        request = new Request.Builder()
                .url(requestUrl + "/" + id)
                .build();

        call = client.newCall(request);

        response = call.execute();

        Assertions.assertEquals(200, response.code());
        JsonUtil.compareObject(expected, response.body().string());
    }

    @Test
    public void testGetAnagraficaNotFound(TestInfo testInfo) throws Exception {
        log.info("Start test {}", testInfo.getTestMethod().get().getName());
        String requestUrl = "http://localhost:" + port + "/api/v1/anagrafica/100";

        OkHttpClient client = new OkHttpClient()
                .newBuilder()
                .build();

        Request request = new Request.Builder()
                .url(requestUrl)
                .build();

        Call call = client.newCall(request);

        Response response = call.execute();

        Assertions.assertEquals(404, response.code());
    }
}
