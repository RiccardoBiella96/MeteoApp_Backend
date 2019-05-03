package ch.supsi.dti.isin.jwt.dao;

import ch.supsi.dti.isin.jwt.model.Authority;
import ch.supsi.dti.isin.jwt.model.AuthorityName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Authority findByName(AuthorityName name);

}