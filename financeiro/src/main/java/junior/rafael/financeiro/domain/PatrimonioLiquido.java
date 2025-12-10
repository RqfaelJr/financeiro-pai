package junior.rafael.financeiro.domain;

import jakarta.persistence.Entity;
import junior.rafael.financeiro.repository.PatrimonioLiquidoRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PatrimonioLiquido extends Base{

    public PatrimonioLiquido(String nome) {
        this.setNome(nome);
    }
}
