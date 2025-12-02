package junior.rafael.financeiro.domain.passivo;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import junior.rafael.financeiro.domain.Base;
import junior.rafael.financeiro.request.PassivoRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Passivo extends Base {

    @Enumerated(EnumType.STRING)
    private TipoPassivo tipo;

    @Enumerated(EnumType.STRING)
    private CategoriaPassivo categoria;

    public Passivo(PassivoRequest request) {
        this.tipo = request.tipo();
        this.categoria = request.categoria();
        this.setNome(request.nome());
    }
}

