package org.openpay.client.repository;

import org.openpay.client.model.MarvelQueriesInfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarvelQueriesInfoRepository extends JpaRepository<MarvelQueriesInfo, Long> {
    MarvelQueriesInfo findByHash(String hash);
}
