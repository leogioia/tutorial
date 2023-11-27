package it.leogioia.api;

import anagrafica_v1.api.AnagraficheApi;
import anagrafica_v1.model.Anagrafica;
import anagrafica_v1.model.ListaAnagrafiche;
import it.leogioia.persistence.AnagraficheManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/api/v1")
public class AnagraficaApiImpl implements AnagraficheApi {

    @Autowired
    AnagraficheManager anagraficheManager;

    public ResponseEntity<ListaAnagrafiche> getAnagrafiche() {
        log.info("Invocazione getAnagrafiche");
        ListaAnagrafiche listaAnagrafiche = new ListaAnagrafiche();
        listaAnagrafiche.addAll(anagraficheManager.getAnagrafiche());
        return new ResponseEntity<>(listaAnagrafiche, HttpStatus.OK);
    }

    public ResponseEntity<Anagrafica> getAnagrafica(String idAnagrafica) {
        log.info("Invocazione getAnagrafica({})", idAnagrafica);
        return new ResponseEntity<Anagrafica>(anagraficheManager.getAnagrafica(idAnagrafica), HttpStatus.OK);
    }

    public ResponseEntity<Anagrafica> postAnagrafica(Anagrafica anagrafica) {
        log.info("Invocazione postAnagrafica({})", anagrafica);
        return new ResponseEntity<Anagrafica>(anagraficheManager.putAnagrafica(anagrafica), HttpStatus.OK);
    }
}
