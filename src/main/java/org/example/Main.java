package org.example; // CHẮC CHẮN KHỚP VỚI org.example.Main trong pom.xml

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Pos;

public class Main extends Application { // Đổi tên thành Main nếu pom.xml dùng org.example.Main

    private Label statusLabel;
    private int clickCount = 0;

    @Override
    public void start(Stage stage) {
        // 1. Tạo Label để hiển thị trạng thái
        statusLabel = new Label("JavaFX is configured. Click the button to test! ✅");

        // 2. Tạo Button và thiết lập hành động khi nhấn
        Button testButton = new Button("TEST CLICK");
        testButton.setOnAction(e -> {
            clickCount++;
            statusLabel.setText("Button clicked " + clickCount + " times!");
        });

        // 3. Tạo Layout (VBox)
        VBox root = new VBox(20);
        root.getChildren().addAll(statusLabel, testButton);
        root.setAlignment(Pos.CENTER);

        // 4. Thiết lập Scene và Stage (cửa sổ chính)
        Scene scene = new Scene(root, 400, 250);

        stage.setTitle("JavaFX 21 Test App");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        // Lệnh khởi chạy ứng dụng JavaFX
        launch(args);
    }
}
