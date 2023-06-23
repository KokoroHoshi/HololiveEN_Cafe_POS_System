package models;

public class Product {
    private String id;
    private String category;
    private String name;
    private int price;
    private String imagePath;
    private String description;

    public Product(String id, String category, String name, int price, String imagePath, String description) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.price = price;
        this.imagePath = imagePath;
        this.description = description; 
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
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
