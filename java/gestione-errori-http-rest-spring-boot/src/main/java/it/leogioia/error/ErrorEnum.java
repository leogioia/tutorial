package it.leogioia.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorEnum {

    E01("E01", "Anagrafica non presente", 404);

    private String code;
    private String message;
    private int httpStatus;
}
