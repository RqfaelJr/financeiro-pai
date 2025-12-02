package junior.rafael.financeiro.response;

import java.math.BigDecimal;

public record DREResponse(

        String nome,
        BigDecimal valor,
        String tipo
) {
}
