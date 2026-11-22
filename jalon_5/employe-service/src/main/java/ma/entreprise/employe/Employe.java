package ma.entreprise.employe;

import jakarta.persistence.*;

@Entity
@Table(name = "employes")
public class Employe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false)
    private Long departementId;

    @Column(nullable = false)
    private Boolean actif;

    protected Employe() {
    }

    Employe(String nom, String email, Long departementId, Boolean actif) {
        modifier(nom, email, departementId, actif);
    }

    void modifier(String nom, String email, Long departementId, Boolean actif) {
        this.nom = nom;
        this.email = email;
        this.departementId = departementId;
        this.actif = actif;
    }

    EmployeDto.Vue vue() {
        return new EmployeDto.Vue(id, nom, email, departementId, actif);
    }
}
