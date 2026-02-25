package site.meowcat.texit.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class User {
    @Id @GeneratedValue
    private long id;
    private String username;
    private String password; // hashed
    private String role = "USER"; // can be USER or ADMIN
}
