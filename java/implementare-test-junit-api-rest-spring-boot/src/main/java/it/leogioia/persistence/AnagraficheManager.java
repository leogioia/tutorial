package it.leogioia.persistence;

import it.leogioia.model.Anagrafica;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AnagraficheManager {

    private static final Map<String, Anagrafica> ANAGRAFICHE = new HashMap<String, Anagrafica>();

    static {
        String id = "1";
        ANAGRAFICHE.put(id,
                Anagrafica.builder()
                        .id(id)
                        .nome("Mario")
                        .cognome("Rossi")
                        .indirizzo("Via Roma, 54")
                        .build()
        );
    }

    public List<Anagrafica> getAnagrafiche() {
        return new ArrayList<Anagrafica>(ANAGRAFICHE.values());
    }

    public Anagrafica getAnagrafica(String id) {
        return ANAGRAFICHE.get(id);
    }

    public Anagrafica putAnagrafica(Anagrafica anagrafica) {
        ANAGRAFICHE.put(anagrafica.getId(), anagrafica);
        return anagrafica;
    }
}
