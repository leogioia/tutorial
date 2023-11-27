package it.leogioia.service;

import it.leogioia.model.Prodotto;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PropertyService {

    public List<Prodotto> getProdotti(String p) {
        String prodottiString = Optional.ofNullable(p).orElse("");

        return Arrays.stream(prodottiString.split(","))
                .map(pr -> Prodotto.builder()
                        .descrizione(pr.substring(0, pr.indexOf(":")))
                        .prezzo(Double.parseDouble(pr.substring(pr.indexOf(":") + 1)))
                        .build())
                .collect(Collectors.toList());
    }
}
