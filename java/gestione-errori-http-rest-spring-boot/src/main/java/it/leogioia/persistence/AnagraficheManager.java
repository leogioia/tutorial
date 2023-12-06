package it.leogioia.persistence;

import anagrafica_v1.model.Anagrafica;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnagraficheManager {

    private static final Map<String, Anagrafica> ANAGRAFICHE = new HashMap<String, Anagrafica>();

    static {
        String id = "1";
        Anagrafica anagrafica = new Anagrafica();
        anagrafica.setId(id);
        anagrafica.setNome("Mario");
        anagrafica.setCognome("Rossi");
        anagrafica.setIndirizzo("Via Roma, 54");

        ANAGRAFICHE.put(id, anagrafica);
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
