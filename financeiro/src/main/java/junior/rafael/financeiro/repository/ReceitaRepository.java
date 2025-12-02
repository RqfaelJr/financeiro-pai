package junior.rafael.financeiro.repository;

import junior.rafael.financeiro.domain.receita.Receita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReceitaRepository extends JpaRepository<Receita, Long> {

    @Query("SELECT r.id FROM Receita r")
    List<Long> findAllIds();
}
