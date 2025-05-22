package view;

import controller.GameController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.CellType;
import model.Direction;

public class GameView {
    private final BorderPane root = new BorderPane();
    private final GridPane grid = new GridPane();
    private final GameController controller;
    private final Stage stage;
    private boolean tourMouton = true;
    private int deplacementsRestants = 2;
    private int compteurTour = 1;
    private Label tourLabel;
    private Label compteurLabel;

    private Label margueriteLabel;
    private Label cactusLabel;
    private Label ortieLabel;

    public GameView(Stage stage, GameController controller) {
        this.stage = stage;
        this.controller = controller;

        grid.setHgap(1);
        grid.setVgap(1);
        grid.setPadding(new Insets(10));
        grid.setAlignment(Pos.CENTER);
        root.setCenter(grid);

        tourLabel = new Label("Tour : MOUTON");
        tourLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        compteurLabel = new Label("Compteur de tours : 1");
        compteurLabel.setStyle("-fx-font-size: 14px;");


        margueriteLabel = new Label("🌼 Marguerites : 0");
        cactusLabel = new Label("🌵 Cactus : 0");
        ortieLabel = new Label("🪴 Orties : 0");

        VBox statsBox = new VBox(10, margueriteLabel, cactusLabel, ortieLabel);
        statsBox.setAlignment(Pos.CENTER_LEFT);
        statsBox.setPadding(new Insets(10));
        root.setRight(statsBox);


        Button haut = new Button("↑");
        Button bas = new Button("↓");
        Button gauche = new Button("←");
        Button droite = new Button("→");

        haut.setOnAction(e -> deplacerActif(Direction.HAUT));
        bas.setOnAction(e -> deplacerActif(Direction.BAS));
        gauche.setOnAction(e -> deplacerActif(Direction.GAUCHE));
        droite.setOnAction(e -> deplacerActif(Direction.DROITE));

        HBox horizontal = new HBox(10, gauche, droite);
        horizontal.setAlignment(Pos.CENTER);

        VBox controls = new VBox(5, tourLabel, compteurLabel, haut, horizontal, bas);
        controls.setAlignment(Pos.CENTER);
        controls.setPadding(new Insets(10));
        root.setBottom(controls);

        drawMap();
    }

    public BorderPane getRoot() {
        return root;
    }

    private void drawMap() {
        grid.getChildren().clear();
        var map = controller.getMap();
        for (int i = 0; i < map.getRows(); i++) {
            for (int j = 0; j < map.getCols(); j++) {
                CellType type = map.getCell(i, j);
                ImageView imgView = new ImageView(getImageForType(type));
                imgView.setFitWidth(40);
                imgView.setFitHeight(40);
                grid.add(imgView, j, i);
            }
        }
    }

    private void deplacerActif(Direction dir) {
        if (tourMouton) {
            boolean aBouge = controller.deplacerMouton(dir);
            if (!aBouge) return;

            drawMap();
            updateStats();

            if (controller.isMoutonAttrape()) {
                showEndMessage("💀 Le loup a attrapé le mouton !");
                return;
            } else if (controller.isVictoire()) {
                showEndMessage("🎉 Le mouton a atteint la sortie !");
                return;
            }

            deplacementsRestants--;
            if (deplacementsRestants == 0) {
                tourMouton = false;
                deplacementsRestants = 3;
                tourLabel.setText("Tour : LOUP");
            }
        } else {
            boolean aBouge = controller.deplacerLoup(dir);
            if (!aBouge) return;

            drawMap();

            if (controller.isMoutonAttrape()) {
                showEndMessage("💀 Le loup a attrapé le mouton !");
                return;
            }

            deplacementsRestants--;
            if (deplacementsRestants == 0) {
                tourMouton = true;

                if (controller.aBonusMarguerite()) {
                    deplacementsRestants = 4;
                    controller.retirerBonusMarguerite();
                } else if (controller.aMalusCactus()) {
                    deplacementsRestants = 1;
                    controller.retirerMalusCactus();
                } else {
                    deplacementsRestants = 2;
                }

                compteurTour++;
                compteurLabel.setText("Compteur de tours : " + compteurTour);
                tourLabel.setText("Tour : MOUTON");
            }
        }
    }

    private void updateStats() {
        margueriteLabel.setText("🌼 Marguerites : " + controller.getMargueritesMangees());
        cactusLabel.setText("🌵 Cactus : " + controller.getCactusTouches());
        ortieLabel.setText("🪴 Orties : " + controller.getOrtiesTraversees());
    }

    private void showEndMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Fin de partie");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
        resetToSetup();
    }


    private void resetToSetup() {
        SetupView setupView = new SetupView(stage);
        Scene setupScene = new Scene(setupView.getRoot(), 800, 800);
        stage.setScene(setupScene);
        stage.setTitle("Mange-moi si tu peux - Configuration");
    }

    private Image getImageForType(CellType type) {
        return switch (type) {
            case ROCHER -> new Image(getClass().getResource("/images/rocher.png").toExternalForm());
            case HERBE -> new Image(getClass().getResource("/images/herbe.png").toExternalForm());
            case MARGUERITE -> new Image(getClass().getResource("/images/marguerite.png").toExternalForm());
            case CACTUS -> new Image(getClass().getResource("/images/cactus.png").toExternalForm());
            case ORTIE -> new Image(getClass().getResource("/images/ortie.png").toExternalForm());
            case LOUP -> new Image(getClass().getResource("/images/loup.png").toExternalForm());
            case MOUTON -> new Image(getClass().getResource("/images/mouton.png").toExternalForm());
            case SORTIE -> new Image(getClass().getResource("/images/sortie.png").toExternalForm());
            default -> new Image(getClass().getResource("/images/herbe.png").toExternalForm());
        };
    }
}
