package com.Salora.SaloraWebService.Repository.Token;

import com.Salora.SaloraWebService.Model.Token.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<UserToken, String> {
    @Query("SELECT t FROM UserToken t JOIN t.userEntity u WHERE u.id = :id AND (t.expired = false or t.revoked = false)")
    List<UserToken> findAllValidTokenByUser(@Param("id") String id);
    Optional<UserToken> findByToken(String token);
}
