package junior.rafael.financeiro.response;

import java.math.BigDecimal;

public record RelatorioResponse(


        String nome,
        BigDecimal valor
) {
}
