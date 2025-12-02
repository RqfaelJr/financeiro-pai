package junior.rafael.financeiro.domain.receita;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import junior.rafael.financeiro.domain.Base;
import junior.rafael.financeiro.request.ReceitaRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class Receita extends Base {

    @Enumerated(EnumType.STRING)
    private TipoReceita tipo;

    public Receita(ReceitaRequest request) {
        this.tipo = request.tipo();
        this.setNome(request.nome());
    }
}

