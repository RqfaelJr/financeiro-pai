package junior.rafael.financeiro.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RazaoResponse(
        LocalDate data,
        String nome,
        BigDecimal valor,
        String tipo) {


    public RazaoResponse(LocalDate data, String nome, BigDecimal valor, String tipo) {
        this.data = data;
        this.nome = nome;
        this.valor = valor;
        this.tipo = tipo;
    }
}


