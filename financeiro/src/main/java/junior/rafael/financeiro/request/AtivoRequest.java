package junior.rafael.financeiro.request;

import junior.rafael.financeiro.domain.ativo.CategoriaAtivo;
import junior.rafael.financeiro.domain.ativo.TipoAtivo;

public record AtivoRequest(

    TipoAtivo tipo,
    CategoriaAtivo categoria,
    String nome
) {
}
