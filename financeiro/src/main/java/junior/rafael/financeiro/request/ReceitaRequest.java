package junior.rafael.financeiro.request;

import junior.rafael.financeiro.domain.receita.TipoReceita;


public record ReceitaRequest(

        TipoReceita tipo,
        String nome
) {}