package ai.group2.project_management_system.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Position {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String positionName;

    // Default constructor
    public Position() {
    }

    // Constructor for creating Position from ID
    public Position(int id) {
        this.id = id;
    }

    // Constructor for converting String ID from user create form
    public Position(String id) {
        // Convert the String ID to an integer
        this.id = Integer.parseInt(id);
    }

    // getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }
}
