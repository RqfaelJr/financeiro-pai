package junior.rafael.financeiro.domain.ativo;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import junior.rafael.financeiro.domain.Base;
import junior.rafael.financeiro.request.AtivoRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Ativo extends Base {

    @Enumerated(EnumType.STRING)
    private TipoAtivo tipo;
    @Enumerated(EnumType.STRING)
    private CategoriaAtivo categoria;

    public Ativo(AtivoRequest request) {
        this.tipo = request.tipo();
        this.categoria = request.categoria();
        this.setNome(request.nome());
    }
}

