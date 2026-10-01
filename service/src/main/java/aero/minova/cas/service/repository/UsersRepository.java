package aero.minova.cas.service.repository;

import java.util.Optional;

import aero.minova.cas.service.model.Users;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends DataEntityRepository<Users> {

    public Optional<Users> findByUsernameAndLastActionGreaterThan(String username, int lastAction);

    public default Optional<Users> findByUsername(String username) {
        return findByUsernameAndLastActionGreaterThan(username, 0);
    }
}
