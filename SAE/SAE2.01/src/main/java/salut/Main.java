package salut;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.SetupView;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        SetupView setupView = new SetupView(stage);
        Scene scene = new Scene(setupView.getRoot(), 1000, 800);
        stage.setScene(scene);
        stage.setTitle("Mange-moi si tu peux - Configuration");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
