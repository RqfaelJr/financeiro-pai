package junior.rafael.financeiro.response;

import java.util.List;

public record BuscaLancamentoResponse(

        List<LancamentoResponse> lancamentos,
        Boolean ativoNegativo,
        Boolean passivoNegativo,
        Boolean receitasNegativo,
        Boolean despesasNegativo,
        Boolean comparativoErrado
) {
}
