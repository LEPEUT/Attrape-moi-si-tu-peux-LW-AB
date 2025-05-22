package view;

import controller.GameController;
import controller.SetupController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.CellType;
import model.GameMap;

public class SetupView {
    private final Stage stage;
    private BorderPane root;
    private GridPane grid;
    private GameMap gameMap;
    private SetupController controller;
    private ComboBox<CellType> selector;

    public SetupView(Stage stage) {
        this.stage = stage;
        buildUI();
    }

    public Pane getRoot() {
        return root;
    }

    private void buildUI() {
        root = new BorderPane();

        VBox topPane = new VBox(10);
        topPane.setAlignment(Pos.CENTER);

        Spinner<Integer> rowSpinner = new Spinner<>(5, 30, 10);
        Spinner<Integer> colSpinner = new Spinner<>(5, 30, 10);
        Button generateBtn = new Button("Générer carte");

        HBox sizeBox = new HBox(10, new Label("Lignes:"), rowSpinner, new Label("Colonnes:"), colSpinner, generateBtn);
        sizeBox.setAlignment(Pos.CENTER);
        topPane.getChildren().add(sizeBox);

        root.setTop(topPane);

        grid = new GridPane();
        grid.setHgap(0);
        grid.setVgap(0);
        grid.setPadding(Insets.EMPTY);
        grid.setAlignment(Pos.CENTER);
        root.setCenter(grid);

        HBox bottomPane = new HBox(10);
        selector = new ComboBox<>();
        selector.getItems().addAll(CellType.values());
        selector.setValue(CellType.HERBE);
        Button resetBtn = new Button("Réinitialiser");
        Button confirmBtn = new Button("Valider");

        bottomPane.getChildren().addAll(new Label("Type à placer : "), selector, resetBtn, confirmBtn);
        bottomPane.setAlignment(Pos.CENTER);
        root.setBottom(bottomPane);

        generateBtn.setOnAction(e -> {
            int rows = rowSpinner.getValue();
            int cols = colSpinner.getValue();
            gameMap = new GameMap(rows, cols);
            controller = new SetupController(gameMap);
            drawMap();
        });

        resetBtn.setOnAction(e -> {
            if (controller != null) {
                controller.resetMap();
                selector.getItems().clear();
                selector.getItems().addAll(CellType.values());
                selector.setValue(CellType.HERBE);
                drawMap();
            }
        });

        confirmBtn.setOnAction(e -> {
            if (controller == null || gameMap == null) return;

            if (!controller.isConfigurationValide()) {
                controller.showValidationError();
                return;
            }

            if (!SetupController.estConnexe(gameMap)) {
                showError("La sortie est inaccessible depuis le mouton.");
                return;
            }


            GameController gameController = new GameController(gameMap);
            GameView gameView = new GameView(stage, gameController);
            Scene gameScene = new Scene(gameView.getRoot(), 800, 600);
            stage.setScene(gameScene);


        });
    }

    //termine le drawMap j'ai la flemme wassime frr c'est trop dure

    private void drawMap() {
        grid.getChildren().clear();
        for (int i = 0; i < gameMap.getRows(); i++) {
            for (int j = 0; j < gameMap.getCols(); j++) {
                StackPane stack = new StackPane();

                ImageView bg = new ImageView(getImageForType(CellType.HERBE));
                bg.setFitWidth(40);
                bg.setFitHeight(40);
                stack.getChildren().add(bg);

                CellType current = gameMap.getCell(i, j);
                if (current != CellType.HERBE) {
                    ImageView fg = new ImageView(getImageForType(current));
                    fg.setFitWidth(40);
                    fg.setFitHeight(40);
                    stack.getChildren().add(fg);
                }

                Button btn = new Button();
                btn.setGraphic(stack);
                btn.setPadding(Insets.EMPTY);
                btn.setStyle("-fx-background-color: transparent;");
                btn.setPrefSize(40, 40);

                int row = i, col = j;
                btn.setOnAction(e -> {
                    CellType selectedType = selector.getValue();
                    CellType cellBefore = gameMap.getCell(row, col);

                    if ((cellBefore == CellType.LOUP || cellBefore == CellType.MOUTON)
                            && selectedType != cellBefore) {
                        showError("Vous ne pouvez pas remplacer un LOUP ou un MOUTON.");
                        return;
                    }

                    boolean isOnBorder = row == 0 || row == gameMap.getRows() - 1 ||
                            col == 0 || col == gameMap.getCols() - 1;

                    boolean isCorner = (row == 0 && col == 0) ||
                            (row == 0 && col == gameMap.getCols() - 1) ||
                            (row == gameMap.getRows() - 1 && col == 0) ||
                            (row == gameMap.getRows() - 1 && col == gameMap.getCols() - 1);

                    if (isOnBorder && selectedType != CellType.SORTIE && cellBefore == CellType.ROCHER) {
                        showError("Vous ne pouvez modifier les bords qu'en y plaçant une SORTIE.");
                        return;
                    }

                    if (!isOnBorder && selectedType == CellType.SORTIE) {
                        showError("La sortie doit être placée uniquement sur un bord.");
                        return;
                    }

                    if (isCorner && selectedType == CellType.SORTIE) {
                        showError("La sortie ne peut pas être placée dans un coin.");
                        return;
                    }

                    if (selectedType == CellType.SORTIE || selectedType == CellType.LOUP || selectedType == CellType.MOUTON) {
                        for (int r = 0; r < gameMap.getRows(); r++) {
                            for (int c = 0; c < gameMap.getCols(); c++) {
                                if (gameMap.getCell(r, c) == selectedType && (r != row || c != col)) {
                                    controller.setCell(r, c, CellType.HERBE);
                                }
                            }
                        }
                        selector.getItems().remove(selectedType);
                    }

                    controller.setCell(row, col, selectedType);
                    drawMap();
                });

                grid.add(btn, j, i);
            }
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
