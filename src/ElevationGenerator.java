import java.util.Random;

/**
 * File: ElevationGenerator.java
 * Author: Laan Rose
 * Purpose: Generates and returns double[][] arrays with values corresponding to terrain elevation
 * at given points. Uses the diamond-square algorithm to generate map values- more information on
 * that can be found here: https://en.wikipedia.org/wiki/Diamond-square_algorithm
 */
public class ElevationGenerator {
  private Random rng = new Random();

  /**
   * Generates and returns an array of elevation values. Values are generated using a rough
   * implementation of the diamond-square algorithm.
   */
  public double[][] generate() {
    // Maps generated need to have dimensions in the form of (2^x)+1.
    double[][] newMap = new double[257][257];

    // Generate elevation values using the diamond square algorithm.
    newMap = initializeMap(newMap);
    int initialRegionSize = newMap.length / 2;
    int variance = (int) (newMap.length * 9.6); // How random the map will be.
    newMap = diamondSquare(newMap, initialRegionSize, variance);

    // Smooth map out after generation.
    newMap = smoothMap(newMap);

    return newMap;
  }

  /**
   * Generates starting values on a map so the diamond-square algorithm can be used on it.
   */
  private double[][] initializeMap(double elevationMap[][]) {
    int middle = (elevationMap.length-1)/2;
    int end = elevationMap.length-1;

    // Initialize 9 points with random values: the 4 corners of the array, the center, and the 4
    // points in between corners along the edge of the array.
    elevationMap[middle][end] = rng.nextInt(100); // N.
    elevationMap[middle][0] = rng.nextInt(100); // S.
    elevationMap[end][middle] = rng.nextInt(100); // E.
    elevationMap[0][middle] = rng.nextInt(100); // W.
    elevationMap[0][0] = rng.nextInt(100); // SW.
    elevationMap[0][end] = rng.nextInt(100); // NW.
    elevationMap[end][end] = rng.nextInt(100); // NE
    elevationMap[end][0] = rng.nextInt(100); // SE.
    elevationMap[middle][middle] = rng.nextInt(100); // Center.

    // Randomly pick one the 4 corners on the map to have a starting elevation of 0- makes the final
    // product more interesting to look at.
    int coordX = end * rng.nextInt(2);
    int coordY = end * rng.nextInt(2);
    elevationMap[coordX][coordY] = 0;

    return elevationMap;
  }

  /**
   * Divides the map[][] into sub-regions and generates values for indices in these regions, one
   * region at a time.
   * <p>
   * Strongly suggest reading this if you have no idea what is going on:
   * https://en.wikipedia.org/wiki/Diamond-square_algorithm
   *
   * @param regionSize: Current size of 'regions' to perform algorithm on. Must have array lengths
   *                    of (2^X)+1. Regions become half as wide/tall with every iteration.
   * @param variance:   How much a coordinate can be increased/decreased randomly. Decreases with
   *                    every iteration.
   */
  private double[][] diamondSquare(double map[][], int regionSize, int variance) {
    while (regionSize > 2) {
      // Iterate through map[][] and generate values for regions in it that are 'regionSize' in
      // in length and height.
      for (int x = 0; x < (map.length / (regionSize - 1)); x++) {
        for (int y = 0; y < (map.length / (regionSize - 1)); y++) {
          int posX = (regionSize / 2) + x * (regionSize - 1);
          int posY = (regionSize / 2) + y * (regionSize - 1);
          double sum = 0;
          double tiles = 0;
          double randomness = rng.nextInt(variance * 2) - variance;

          // Generate array index value in the center of this region.
          if (posX - regionSize / 2 > 0 && posY + regionSize / 2 < map.length) { // NW.
            tiles++;
            sum += map[posX - regionSize / 2][posY + regionSize / 2];
          }
          if (posX + regionSize / 2 < map.length && posY + regionSize / 2 < map.length) { //NE.
            tiles++;
            sum += map[posX + regionSize / 2][posY + regionSize / 2];
          }
          if (posX - regionSize / 2 > 0 && posY - regionSize / 2 > 0) { // SW.
            tiles++;
            sum += map[posX - regionSize / 2][posY - regionSize / 2];
          }
          if (posX + regionSize / 2 < map.length && posY - regionSize / 2 > 0) { // SE.
            tiles++;
            sum += map[posX + regionSize / 2][posY - regionSize / 2];
          }
          // Set this tile to the average of its surrounding tiles.
          map[posX][posY] = (sum / tiles) + randomness;
          // Prevent the index from generating extreme values.
          if (map[posX][posY] > 100.0) {
            map[posX][posY] = 100.0;
          }
          if (map[posX][posY] < 0.0) {
            map[posX][posY] = 0.0;
          }

          // Generate northern index value.
          posX = (regionSize / 2) + x * (regionSize - 1);
          posY = (regionSize - 1) + y * (regionSize - 1);
          randomness = rng.nextInt(variance * 2) - variance;
          map[posX][posY] = getAverageForDiamond(map, posX, posY, regionSize) + randomness;
          // Keep generated values within a usable scale.
          if (map[posX][posY] > 100.0) {
            map[posX][posY] = 100.0;
          }
          if (map[posX][posY] < 0.0) {
            map[posX][posY] = 0.0;
          }

          // South.
          posX = (regionSize / 2) + x * (regionSize - 1);
          posY = y * (regionSize - 1);
          randomness = rng.nextInt(variance * 2) - variance;
          map[posX][posY] = getAverageForDiamond(map, posX, posY, regionSize) + randomness;
          if (map[posX][posY] > 100.0) {
            map[posX][posY] = 100.0;
          }
          if (map[posX][posY] < 0.0) {
            map[posX][posY] = 0.0;
          }

          // East.
          posX = x * (regionSize - 1);
          posY = (regionSize / 2) + y * (regionSize - 1);
          randomness = rng.nextInt(variance * 2) - variance;
          map[posX][posY] = getAverageForDiamond(map, posX, posY, regionSize) + randomness;
          if (map[posX][posY] > 100.0) {
            map[posX][posY] = 100.0;
          }
          if (map[posX][posY] < 0.0) {
            map[posX][posY] = 0.0;
          }

          // West.
          posX = (regionSize - 1) + x * (regionSize - 1);
          posY = (regionSize / 2) + y * (regionSize - 1);
          randomness = rng.nextInt(variance * 2) - variance;
          map[posX][posY] = getAverageForDiamond(map, posX, posY, regionSize) + randomness;
          if (map[posX][posY] > 100.0) {
            map[posX][posY] = 100.0;
          }
          if (map[posX][posY] < 0.0) {
            map[posX][posY] = 0.0;
          }
        }
      }

      // Reduce region size; every iteration modifies the map using smaller chunks.
      regionSize = (regionSize / 2) + 1;
      // Reduce how random generated values can be.
      variance = (int) Math.pow(variance, 0.65);
      // Random values need a range of at least 1.
      if (variance < 1) {
        variance = 1;
      }
    }

    return map;
  }

  /**
   * Return the average of the indices to the N/S/E/W of a given index.
   */
  private double getAverageForDiamond(double[][] map, int posX, int posY, int regionSize) {
    double sum = 0;
    double indices = 0;
    if (posX - (regionSize / 2) > 0) { // W.
      indices++;
      sum += map[posX - (regionSize / 2)][posY];
    }
    if (posX + (regionSize / 2) < map.length - 1) { // E.
      indices++;
      sum += map[posX + (regionSize / 2)][posY];
    }
    if (posY - (regionSize / 2) > 0) { // S.
      indices++;
      sum += map[posX][posY - (regionSize / 2)];
    }
    if (posY + (regionSize / 2) < map.length - 1) { // N.
      indices++;
      sum += map[posX][posY + (regionSize / 2)];
    }

    double average = sum / indices;
    return average;
  }

  /**
   * Makes every space in the map[][] array equal to the average of its surrounding spaces and then
   * returns the new, smoother array.
   */
  public double[][] smoothMap(double map[][]) {
    for (int x = 0; x < map.length; x++) {
      for (int y = 0; y < map[0].length; y++) {
        double sum = 0;
        int indices = 0;
        // Get values of the 9 surrounding indices.
        if (x > 0) { // Ignore nonexistent indices.
          sum = sum + map[x - 1][y];
          indices++;
        }
        if (x > 0 && y < map.length - 1) {
          sum = sum + map[x - 1][y + 1];
          indices++;
        }
        if (y < map.length - 1) {
          sum = sum + map[x][y + 1];
          indices++;
        }
        if (x < map.length - 1 && y < map.length - 1) {
          sum = sum + map[x + 1][y + 1];
          indices++;
        }
        if (x < map.length - 1) {
          sum = sum + map[x + 1][y];
          indices++;
        }
        if (x < map.length - 1 && y > 0) {
          sum = sum + map[x + 1][y - 1];
          indices++;
        }
        if (y > 0) {
          sum = sum + map[x][y - 1];
          indices++;
        }
        if (x > 0 && y > 0) {
          sum = sum + map[x - 1][y - 1];
          indices++;
        }

        // Make map[x][y] the average of its neighbors.
        map[x][y] = (sum / indices);
      }
    }

    return map;
  }
}
