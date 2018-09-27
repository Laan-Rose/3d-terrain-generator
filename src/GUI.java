import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.MeshView;
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
  private MeshView currentMesh; // 3D mesh of terrain.
  private VBox vBox;
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

    camera = new PerspectiveCamera(true);
    camera.setNearClip(0.1);
    camera.setFarClip(100000);
    // Position camera so that it's looking down on the map.
    camera.setTranslateZ(-13500);
    camera.setTranslateX(6500);
    camera.setTranslateY(-13000);
    camera.setRotationAxis(Rotate.X_AXIS);
    camera.setRotate(-35);

    vBox = new VBox(group);

    scene = new Scene(vBox, 1000, 720, true, SceneAntialiasing.BALANCED);
    scene.setFill(Color.SKYBLUE);
    scene.setCamera(camera);

    stage.setTitle("Procedural Terrain Generation in JavaFX");
    stage.setScene(scene);
    stage.setMaximized(true);
    stage.show();

    // Generate and display a piece of terrain on startup.
    terrain.generate();
    currentMesh = terrain.getMesh();
    group.getChildren().add(currentMesh);

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
        // Camera controls.
        if (e.getCode() == KeyCode.W) {
          camera.setTranslateZ(camera.getTranslateZ() + 200);
        }
        if (e.getCode() == KeyCode.A) {
          camera.setTranslateX(camera.getTranslateX() - 200);
        }
        if (e.getCode() == KeyCode.S) {
          camera.setTranslateZ(camera.getTranslateZ() - 200);
        }
        if (e.getCode() == KeyCode.D) {
          camera.setTranslateX(camera.getTranslateX() + 200);
        }
        if (e.getCode() == KeyCode.R) {
          camera.setRotationAxis(Rotate.X_AXIS);
          if (camera.getRotate() < 0) {
            camera.setRotate(camera.getRotate() + 2.0);
          }
        }
        if (e.getCode() == KeyCode.F) {
          camera.setRotationAxis(Rotate.X_AXIS);
          if (camera.getRotate() >= -150) {
            camera.setRotate(camera.getRotate() - 2.0);
          }
        }
      }
    });
  }
}