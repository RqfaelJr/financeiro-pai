package junior.rafael.financeiro.response;

import java.math.BigDecimal;

public record BalanceteResponse(

        String nome,
        BigDecimal saldoInicialDevedor,
        BigDecimal saldoInicialCredor,
        BigDecimal saldoMovimentacaoDevedor,
        BigDecimal saldoMovimentacaoCredor,
        BigDecimal saldoFinalDevedor,
        BigDecimal saldoFinalCredor
){
}
