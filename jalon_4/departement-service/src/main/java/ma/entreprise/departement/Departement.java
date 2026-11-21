package ma.entreprise.departement;

import jakarta.persistence.*;

@Entity
@Table(name = "departements")
public class Departement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nom;

    @Column(nullable = false)
    private Boolean actif;

    protected Departement() {
    }

    Departement(String nom, Boolean actif) {
        modifier(nom, actif);
    }

    void modifier(String nom, Boolean actif) {
        this.nom = nom;
        this.actif = actif;
    }

    DepartementDto.Vue vue() {
        return new DepartementDto.Vue(id, nom, actif);
    }
}
