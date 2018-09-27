import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

/**
 * File: Terrain.java
 * Author: Laan Rose
 * Purpose: Represents a terrain landscape; contains the raw elevation data and a 3D mesh version of
 * the terrain.
 */
public class Terrain {
  private double elevationMap[][] = new double[257][257]; // Indices refer to height at a point.
  private TriangleMesh meshBase = new TriangleMesh();
  private MeshView mesh = new MeshView(); // 3D representation of this Terrain object.
  private ElevationGenerator elevGenerator = new ElevationGenerator();

  /**
   * Generates the elevation values and 3D mesh objects representing this piece of terrain.
   */
  public void generate() {
    // Clear old elevation data.
    for (int x = 0; x < elevationMap.length; x++) {
      for (int y = 0; y < elevationMap[0].length; y++) {
        elevationMap[x][y] = 0;
      }
    }
    // Generate elevation.
    elevationMap = elevGenerator.generate();

    // Use elevation[][] to generate a 3D mesh of this terrain.
    generateMesh();
  }

  /**
   * Generates a 3D mesh representing this terrain object based on the height values in
   * elevation[][].
   */
  private void generateMesh() {
    meshBase = null;
    meshBase = new TriangleMesh();

    // Initialize textures.
    meshBase.getTexCoords().addAll(0, 0);

    // Create vertices.
    int size = elevationMap.length;
    for (int x = 0; x < size; x++) {
      for (int z = 0; z < size; z++) {
        double height = elevationMap[x][z] * 10;
        meshBase.getPoints().addAll(
          x * 50, -(int) height, z * 50
        );
      }
    }

    // Initialize terrain color.
    meshBase.getTexCoords().addAll(0, 0);

    // Add triangles that make up the mesh.
    int swVert, seVert, nwVert, neVert;
    for (int hori = 1; hori < size - 1; hori++) {
      for (int vert = 1; vert < size - 1; vert++) {
        // Vertex coordinates for meshes use only 1 coordinate instead of 2 like a Cartesian graph
        // does. This converts the sw, se, nw, and ne Cartesian coordinates to the correct vertex
        // coordinate.
        swVert = ((hori - 1) * size) + vert;
        seVert = (hori * size) + vert;
        nwVert = ((hori - 1) * size) + vert + 1;
        neVert = (hori * size) + vert + 1;

        meshBase.getFaces().addAll(
          swVert, 0, seVert, 0, nwVert, 0,
          seVert, 0, neVert, 0, nwVert, 0
        );
      }
    }

    // Finalize miesh and add color to it.
    mesh = null;
    mesh = new MeshView(meshBase);
    PhongMaterial color = new PhongMaterial(Color.SANDYBROWN);
    mesh.setMaterial(color);
  }

  public MeshView getMesh() {
    return mesh;
  }
}
