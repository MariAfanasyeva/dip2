package serialization;
import java.util.List;

public class Order {
    private final List<String> ingredients;

    public Order(List<String> ingredients) {
        this.ingredients = ingredients;
    }
}

