package com.example.contacts;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.util.Collections;
import java.util.List;

public class ContactDao {
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    public ContactDao(NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.namedJdbcTemplate = namedJdbcTemplate;
    }

    public List<Contact> getAllContacts() {
        return namedJdbcTemplate.query(
                "SELECT id, name, surname, email, phone_number FROM contact",
                (rs, rowNum) -> new Contact(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getString("email"),
                        rs.getString("phone_number")
                )
        );
    }

    public Contact getContact(long contactId) {
        try {
            return namedJdbcTemplate.queryForObject(
                    "SELECT id, name, surname, email, phone_number FROM contact WHERE id = :id",
                    Collections.singletonMap("id", contactId),
                    (rs, rowNum) -> new Contact(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getString("surname"),
                            rs.getString("email"),
                            rs.getString("phone_number")
                    )
            );
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return null;
        }
    }

    public long addContact(Contact contact) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedJdbcTemplate.update(
                "INSERT INTO contact(name, surname, email, phone_number) VALUES (:name, :surname, :email, :phone)",
                new MapSqlParameterSource()
                        .addValue("name", contact.getName())
                        .addValue("surname", contact.getSurname())
                        .addValue("email", contact.getEmail())
                        .addValue("phone", contact.getPhone()),
                keyHolder,
                new String[] {"id"}
        );
        return keyHolder.getKey().longValue();
    }

    public void updatePhoneNumber(long contactId, String phoneNumber) {
        namedJdbcTemplate.update(
                "UPDATE contact SET phone_number = :phone WHERE id = :id",
                new MapSqlParameterSource()
                        .addValue("phone", phoneNumber)
                        .addValue("id", contactId)
        );
    }

    public void updateEmail(long contactId, String email) {
        namedJdbcTemplate.update(
                "UPDATE contact SET email = :email WHERE id = :id",
                new MapSqlParameterSource()
                        .addValue("email", email)
                        .addValue("id", contactId)
        );
    }

    public void deleteContact(long contactId) {
        namedJdbcTemplate.update(
                "DELETE FROM contact WHERE id = :id",
                Collections.singletonMap("id", contactId)
        );
    }
}