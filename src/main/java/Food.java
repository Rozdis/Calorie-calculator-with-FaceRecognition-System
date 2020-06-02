public class Food {
    int id;
    String name;
    int calories, grams;
    float proteins;
    float fats;

    float carbohydrates;
    String type, time;
    public Food(){}

    public Food(int id, String name, int calories, float proteins, float fats, float carbohydrates) {
        this.id = id;
        this.name = name;
        this.calories = calories;
        this.proteins = proteins;
        this.fats = fats;
        this.carbohydrates = carbohydrates;
    }

    public Food(int id, String name, int calories, float proteins, float fats, float carbohydrates, int grams, String time) {
        this.id = id;
        this.name = name;
        this.calories = calories;
        this.proteins = proteins;
        this.fats = fats;
        this.carbohydrates = carbohydrates;
        this.time = time;
        this.grams = grams;
    }

    public int getGrams() {
        return grams;
    }

    public String getTime() {
        return time;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public void setProteins(float proteins) {
        this.proteins = proteins;
    }

    public void setFats(float fats) {
        this.fats = fats;
    }

    public void setCarbohydrates(float carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCalories() {
        return calories;
    }

    public float getProteins() {
        return proteins;
    }

    public float getFats() {
        return fats;
    }

    public float getCarbohydrates() {
        return carbohydrates;
    }

    public String getType() {
        return type;
    }
    // public Food(){}
}
