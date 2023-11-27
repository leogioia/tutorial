package it.leogioia.api;

import it.leogioia.model.GetProdottiResponse;
import it.leogioia.model.Prodotto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/api/v1")
public class ProdottoApi {

    @Value("${descrizione.offerte}")
    String descrizioneOfferte;
    @Value("#{propertyService.getProdotti('${lista.prodotti}')}")
    List<Prodotto> prodotti;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/prodotti",
            produces = {"application/json"}
    )
    public ResponseEntity<GetProdottiResponse> getProdotti() {
        log.info("Invocazione getProdotti");

        GetProdottiResponse getProdottiResponse = GetProdottiResponse.builder()
                .descrizioneOfferte(descrizioneOfferte)
                .prodotti(prodotti)
                .build();

        return new ResponseEntity<>(getProdottiResponse, HttpStatus.OK);
    }
}
