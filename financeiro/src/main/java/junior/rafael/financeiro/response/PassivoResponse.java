package junior.rafael.financeiro.response;

import junior.rafael.financeiro.domain.passivo.Passivo;
import junior.rafael.financeiro.domain.passivo.TipoPassivo;
import junior.rafael.financeiro.domain.passivo.CategoriaPassivo;
import java.math.BigDecimal;
import java.time.LocalDate;

public record PassivoResponse(Long id, TipoPassivo tipo, CategoriaPassivo categoria, String nome) {
    public PassivoResponse(Passivo passivo) {
        this(passivo.getId(), passivo.getTipo(), passivo.getCategoria(), passivo.getNome());
    }
}


