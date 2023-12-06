package it.leogioia.api;

import anagrafica_v1.api.AnagraficheApi;
import anagrafica_v1.model.Anagrafica;
import it.leogioia.error.ErrorException;
import it.leogioia.persistence.AnagraficheManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

import static it.leogioia.error.ErrorEnum.E01;

@Slf4j
@Controller
@RequestMapping("/api/v1")
public class AnagraficaApiImpl implements AnagraficheApi {

    @Autowired
    AnagraficheManager anagraficheManager;

    public ResponseEntity<Anagrafica> getAnagrafica(String idAnagrafica) {
        log.info("Invocazione getAnagrafica({})", idAnagrafica);

        Anagrafica anagrafica = Optional.ofNullable(anagraficheManager.getAnagrafica(idAnagrafica))
                .orElseThrow(() -> new ErrorException(E01));

        return new ResponseEntity<>(anagrafica, HttpStatus.OK);
    }
}
