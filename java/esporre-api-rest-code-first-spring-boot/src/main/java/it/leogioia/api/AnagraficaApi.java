package it.leogioia.api;

import io.swagger.v3.oas.annotations.Parameter;
import it.leogioia.model.Anagrafica;
import it.leogioia.persistence.AnagraficheManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/api/v1")
public class AnagraficaApi {

    @Autowired
    AnagraficheManager anagraficheManager;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/anagrafiche",
            produces = {"application/json"}
    )
    public ResponseEntity<List<Anagrafica>> getAnagrafiche() {
        log.info("Invocazione getAnagrafiche");
        return new ResponseEntity<List<Anagrafica>>(anagraficheManager.getAnagrafiche(), HttpStatus.OK);
    }

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/anagrafiche/{idAnagrafica}",
            produces = {"application/json"}
    )
    public ResponseEntity<Anagrafica> getAnagrafica(@Parameter(required = true) @PathVariable(name = "idAnagrafica") String idAnagrafica) {
        log.info("Invocazione getAnagrafica({})", idAnagrafica);
        return new ResponseEntity<Anagrafica>(anagraficheManager.getAnagrafica(idAnagrafica), HttpStatus.OK);
    }

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/anagrafiche",
            produces = {"application/json"}
    )
    public ResponseEntity<Anagrafica> postAnagrafica(@Parameter(required = true) @RequestBody Anagrafica anagrafica) {
        log.info("Invocazione postAnagrafica({})", anagrafica);
        return new ResponseEntity<Anagrafica>(anagraficheManager.putAnagrafica(anagrafica), HttpStatus.OK);
    }
}
