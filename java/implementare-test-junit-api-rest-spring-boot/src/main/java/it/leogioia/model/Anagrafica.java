package it.leogioia.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Anagrafica {

    private String id;
    private String nome;
    private String cognome;
    private String indirizzo;
}
