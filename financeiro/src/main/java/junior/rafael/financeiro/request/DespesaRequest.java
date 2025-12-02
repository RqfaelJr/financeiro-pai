package junior.rafael.financeiro.request;

import junior.rafael.financeiro.domain.despesa.TipoDespesa;

public record DespesaRequest(

        TipoDespesa tipo,
        String nome
) {}

