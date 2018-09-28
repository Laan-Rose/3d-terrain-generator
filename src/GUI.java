import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.MeshView;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

/**
 * File: GUI.java
 * Author: Laan Rose
 * Purpose: Displays a window with a 3D mesh that represents a piece of generated terrain.
 */
public class GUI extends Application {
  // UI components.
  private Scene scene;
  private Group group;
  private PerspectiveCamera camera;
  private MeshView currentMesh;
  private Label instructions;
  private Terrain terrain = new Terrain();

  public static void main(String args[]) {
    launch(args);
  }

  /**
   * Initializes and displays GUI components.
   */
  @Override
  public void start(Stage stage) {
    group = new Group();

    // Create and position camera so that it's looking down on the map.
    camera = new PerspectiveCamera(true);
    camera.setNearClip(0.1);
    camera.setFarClip(100000);
    camera.setTranslateZ(-13500);
    camera.setTranslateX(6500);
    camera.setTranslateY(-13000);
    camera.setRotationAxis(Rotate.X_AXIS);
    camera.setRotate(-35);

    // Display instructions.
    instructions = new Label("Hit 'enter' to procedurally generate a new piece of terrain.");
    instructions.setTranslateY(-11300);
    instructions.setTranslateX(camera.getTranslateX() - 400);
    instructions.setTranslateZ(-12000);
    instructions.setRotationAxis(Rotate.X_AXIS);
    instructions.setRotate(-25);
    Font labelFont = new Font("Arial Bold", 32);
    instructions.setFont(labelFont);

    // Wrap everything together and put it on screen.
    scene = new Scene(group, 0, 0,true, SceneAntialiasing.BALANCED);
    scene.setFill(Color.SKYBLUE);
    scene.setCamera(camera);

    stage.setTitle("Procedural Terrain Generation in JavaFX");
    stage.setScene(scene);
    stage.setMaximized(true);
    stage.show();

    // Generate and display a piece of terrain on startup.
    updateTerrainMesh();

    toggleKeyEvents();
  }

  /**
   * Replaces the current TriangleMesh being displayed with a new one from the 'terrain' object.
   */
  private void updateTerrainMesh() {
    // Remove old mesh.
    group.getChildren().removeAll();
    group.getChildren().clear();

    // Generate new terrain mesh and display it.
    terrain.generate();
    currentMesh = terrain.getMesh();
    group.getChildren().add(currentMesh);
    group.getChildren().add(instructions);
  }

  /**
   * Adds key functionality.
   */
  private void toggleKeyEvents() {
    scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
      @Override
      public void handle(KeyEvent e) {
        if (e.getCode() == KeyCode.ENTER) {
          updateTerrainMesh();
        }
      }
    });
  }
}