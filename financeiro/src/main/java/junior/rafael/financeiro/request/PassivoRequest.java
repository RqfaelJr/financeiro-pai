package junior.rafael.financeiro.request;

import junior.rafael.financeiro.domain.passivo.TipoPassivo;
import junior.rafael.financeiro.domain.passivo.CategoriaPassivo;

public record PassivoRequest(

    TipoPassivo tipo,
    CategoriaPassivo categoria,
    String nome
) {}

