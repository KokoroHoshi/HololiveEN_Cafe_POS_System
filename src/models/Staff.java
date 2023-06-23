package models;

public class Staff {
    private String id;
    private String department;
    private String name;
    private String imagePath;
    private String description;

    public Staff(String id, String department, String name, String imagePath, String description) {
        this.id = id;
        this.department = department;
        this.name = name;
        this.imagePath = imagePath;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    
}
