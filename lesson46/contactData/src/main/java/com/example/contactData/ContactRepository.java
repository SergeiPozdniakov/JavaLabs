package com.example.contactData;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ContactRepository extends JpaRepository<Contact, Long> {


    @Modifying
    @Query("UPDATE Contact c SET c.phone = :phone WHERE c.id = :id")
    void updatePhone(@Param("id") Long id, @Param("phone") String phone);


    @Modifying
    @Query("UPDATE Contact c SET c.email = :email WHERE c.id = :id")
    void updateEmail(@Param("id") Long id, @Param("email") String email);
}
