package junior.rafael.financeiro.request;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LancamentoRequest(

        Long debito,
        Long credito,
        LocalDate data,
        String descricao,
        BigDecimal valor
) {
}
