package com.errasoft.friendfinder.repo;

import com.errasoft.friendfinder.model.ContactInfo;
import com.errasoft.friendfinder.model.security.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactInfoRepo extends JpaRepository<ContactInfo, Long>  {
    Page<ContactInfo> findAllByOrderByCreatedDateDesc(Pageable pageable);
    Page<ContactInfo> findByAccountOrderByCreatedDateDesc(Account account,Pageable pageable);

    @Query("""
        SELECT c FROM ContactInfo c
        WHERE LOWER(c.phone) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(c.message) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(c.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(c.account.username) LIKE LOWER(CONCAT('%', :keyword, '%'))
        ORDER BY c.createdDate DESC
        """)
    List<ContactInfo> searchContacts(@Param("keyword") String keyword);


}
