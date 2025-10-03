import javafx.scene.control.Button;
import javafx.scene.text.Font;

public class ButtonFactory {
    public static Button createButton(String text, double width) {
        Button btn = new Button(text);
        btn.setPrefWidth(width);
        btn.setFont(new Font("Arial", 20));
        return btn;
    }
}
