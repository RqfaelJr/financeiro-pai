package junior.rafael.financeiro.domain;


import jakarta.persistence.*;
import junior.rafael.financeiro.request.LancamentoRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Lancamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate data;

    @ManyToOne
    @JoinColumn(name = "debito_id")
    private Base debito;

    @ManyToOne
    @JoinColumn(name = "credito_id")
    private Base credito;

    private BigDecimal valor;

    private String descricao;

    public Lancamento(LancamentoRequest request, Base debito, Base credito) {
        this.data = request.data();
        this.debito = debito;
        this.credito = credito;
        this.valor = request.valor();
        this.descricao = request.descricao();
    }
}
