package junior.rafael.financeiro.domain.despesa;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import junior.rafael.financeiro.domain.Base;
import junior.rafael.financeiro.request.DespesaRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Despesa extends Base {

    @Enumerated(EnumType.STRING)
    private TipoDespesa tipo;

    public Despesa(DespesaRequest request) {
        this.tipo = request.tipo();
        this.setNome(request.nome());
    }
}

