package junior.rafael.financeiro.response;

import junior.rafael.financeiro.domain.PatrimonioLiquido;

public record PatrimonioLiquidoResponse(
        Long id,
        String nome
) {

    public PatrimonioLiquidoResponse(PatrimonioLiquido pl) {
        this(pl.getId(), pl.getNome());
    }
}
