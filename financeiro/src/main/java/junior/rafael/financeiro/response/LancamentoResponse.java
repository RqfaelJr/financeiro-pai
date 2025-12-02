package junior.rafael.financeiro.response;

import junior.rafael.financeiro.domain.Lancamento;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LancamentoResponse(Long id,
                                 String debito,
                                 String credito,
                                 String descricao,
                                 LocalDate data,
                                 BigDecimal valor,
                                 Boolean ativoNegativo,
                                 Boolean passivoNegativo,
                                 Boolean receitasNegativo,
                                 Boolean despesasNegativo,
                                 Boolean comparativoErrado){
    public LancamentoResponse(Lancamento lancamento,
                              Boolean ativoNegativo,
                              Boolean passivoNegativo,
                              Boolean receitasNegativo,
                              Boolean despesasNegativo,
                              Boolean comparativoErrado) {
        this(
                lancamento.getId(),
                lancamento.getDebito().getNome(),
                lancamento.getCredito().getNome(),
                lancamento.getDescricao(),
                lancamento.getData(),
                lancamento.getValor(),
                ativoNegativo,
                passivoNegativo,
                receitasNegativo,
                despesasNegativo,
                comparativoErrado
        );
    }

    public static LancamentoResponse fromEntity(Lancamento lancamento) {
        return new LancamentoResponse(lancamento, null, null, null, null, null);
    }

}
