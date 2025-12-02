package junior.rafael.financeiro.response;

import junior.rafael.financeiro.domain.despesa.Despesa;
import junior.rafael.financeiro.domain.despesa.TipoDespesa;
import java.math.BigDecimal;
import java.time.LocalDate;

public record DespesaResponse(Long id, String nome, TipoDespesa tipo) {
    public DespesaResponse(Despesa despesa) {
        this(despesa.getId(), despesa.getNome(), despesa.getTipo());
    }
}

