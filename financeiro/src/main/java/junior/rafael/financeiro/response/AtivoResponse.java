package junior.rafael.financeiro.response;

import junior.rafael.financeiro.domain.ativo.Ativo;
import junior.rafael.financeiro.domain.ativo.CategoriaAtivo;
import junior.rafael.financeiro.domain.ativo.TipoAtivo;

public record AtivoResponse(Long id, TipoAtivo tipo, CategoriaAtivo categoria, String nome){

    public AtivoResponse(Ativo ativo){
        this(ativo.getId(), ativo.getTipo(), ativo.getCategoria(), ativo.getNome());
    }
}
