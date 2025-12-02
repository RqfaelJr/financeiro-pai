package junior.rafael.financeiro.response;

import junior.rafael.financeiro.domain.receita.Receita;
import junior.rafael.financeiro.domain.receita.TipoReceita;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ReceitaResponse(Long id, TipoReceita tipo,  String nome) {
    public ReceitaResponse(Receita receita) {
        this(receita.getId(), receita.getTipo(), receita.getNome());
    }
}

