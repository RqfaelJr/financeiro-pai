package junior.rafael.financeiro.repository;

import junior.rafael.financeiro.domain.ativo.Ativo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AtivoRepository extends JpaRepository<Ativo, Long> {


    @Query("SELECT a.id FROM Ativo a")
    List<Long> findAllIds();
}
