import org.example.BacteriaLifeLogic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("BacteriaLifeLogic Tests")
class BacteriaLifeLogicTest {

    private BacteriaLifeLogic logic;
    private static final int DIMENSION = 30;

    @BeforeEach
    void setUp() {
        logic = new BacteriaLifeLogic(DIMENSION);
    }

    @Test
    @DisplayName("Should generate initial generation with correct dimensions")
    void testGenerateInitialGen_CorrectDimensions() {
        int[][] gen = logic.generateInitialGen();

        assertNotNull(gen, "Generated generation should not be null");
        assertEquals(DIMENSION, gen.length, "Generation should have correct number of rows");
        assertEquals(DIMENSION, gen[0].length, "Generation should have correct number of columns");
    }

    @Test
    @DisplayName("Should generate initial generation with only 0s and 1s")
    void testGenerateInitialGen_ValidValues() {
        int[][] gen = logic.generateInitialGen();

        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j++) {
                assertTrue(gen[i][j] == 0 || gen[i][j] == 1,
                        "Cell value should be either 0 or 1");
            }
        }
    }

    @Test
    @DisplayName("Should create new bacteria when exactly 3 neighbors exist")
    void testGenerateNewGen_Birth() {
        int[][] gen = new int[5][5];
        // Create a pattern where position (2,2) has exactly 3 neighbors
        gen[1][1] = 1;
        gen[1][2] = 1;
        gen[2][1] = 1;
        // Center cell is dead (0)
        gen[2][2] = 0;

        BacteriaLifeLogic testLogic = new BacteriaLifeLogic(5);
        int[][] newGen = testLogic.generateNewGen(gen);

        assertEquals(1, newGen[2][2], "Dead cell with 3 neighbors should become alive");
    }

    @Test
    @DisplayName("Should kill bacteria with 0 or 1 neighbors (loneliness)")
    void testGenerateNewGen_DeathByLoneliness() {
        int[][] gen = new int[5][5];
        // Create a bacteria with only 1 neighbor
        gen[2][2] = 1;
        gen[2][3] = 1;

        BacteriaLifeLogic testLogic = new BacteriaLifeLogic(5);
        int[][] newGen = testLogic.generateNewGen(gen);

        assertEquals(0, newGen[2][2], "Bacteria with 1 neighbor should die");
    }

    @Test
    @DisplayName("Should kill bacteria with more than 3 neighbors (suffocation)")
    void testGenerateNewGen_DeathBySuffocation() {
        int[][] gen = new int[5][5];
        // Create a bacteria surrounded by 4 neighbors
        gen[2][2] = 1; // Center
        gen[1][2] = 1; // Top
        gen[3][2] = 1; // Bottom
        gen[2][1] = 1; // Left
        gen[2][3] = 1; // Right

        BacteriaLifeLogic testLogic = new BacteriaLifeLogic(5);
        int[][] newGen = testLogic.generateNewGen(gen);

        assertEquals(0, newGen[2][2], "Bacteria with more than 3 neighbors should die");
    }

    @Test
    @DisplayName("Should keep bacteria alive with 2 or 3 neighbors")
    void testGenerateNewGen_Survival() {
        int[][] gen = new int[5][5];
        // Create a bacteria with exactly 2 neighbors
        gen[2][2] = 1; // Center
        gen[1][2] = 1; // Top
        gen[2][1] = 1; // Left

        BacteriaLifeLogic testLogic = new BacteriaLifeLogic(5);
        int[][] newGen = testLogic.generateNewGen(gen);

        assertEquals(1, newGen[2][2], "Bacteria with 2 neighbors should survive");
    }

    @Test
    @DisplayName("Should count neighbors correctly for corner cell")
    void testCheckNeighbours_CornerCell() {
        int[][] gen = new int[5][5];
        gen[0][0] = 1; // Top-left corner
        gen[0][1] = 1;
        gen[1][0] = 1;
        gen[1][1] = 1;

        int neighbors = BacteriaLifeLogic.checkNeighbours(gen, 0, 0);
        assertEquals(3, neighbors, "Corner cell should have 3 neighbors");
    }

    @Test
    @DisplayName("Should count neighbors correctly for edge cell")
    void testCheckNeighbours_EdgeCell() {
        int[][] gen = new int[5][5];
        gen[0][2] = 1; // Top edge
        gen[0][1] = 1;
        gen[0][3] = 1;
        gen[1][1] = 1;
        gen[1][2] = 1;
        gen[1][3] = 1;

        int neighbors = BacteriaLifeLogic.checkNeighbours(gen, 0, 2);
        assertEquals(5, neighbors, "Edge cell should count correct neighbors");
    }

    @Test
    @DisplayName("Should count neighbors correctly for center cell")
    void testCheckNeighbours_CenterCell() {
        int[][] gen = new int[5][5];
        // Surround center cell (2,2) with all 8 neighbors
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                if (i != 2 || j != 2) {
                    gen[i][j] = 1;
                }
            }
        }

        int neighbors = BacteriaLifeLogic.checkNeighbours(gen, 2, 2);
        assertEquals(8, neighbors, "Center cell should have 8 neighbors");
    }

    @Test
    @DisplayName("Should count zero neighbors correctly")
    void testCheckNeighbours_NoNeighbors() {
        int[][] gen = new int[5][5];
        gen[2][2] = 1; // Only center cell is alive

        int neighbors = BacteriaLifeLogic.checkNeighbours(gen, 2, 2);
        assertEquals(0, neighbors, "Isolated cell should have 0 neighbors");
    }

    @Test
    @DisplayName("Should return true for valid coordinates")
    void testInBounds_ValidCoordinates() {
        int[][] gen = new int[5][5];

        assertTrue(BacteriaLifeLogic.inBounds(gen, 0, 0), "Top-left corner should be in bounds");
        assertTrue(BacteriaLifeLogic.inBounds(gen, 4, 4), "Bottom-right corner should be in bounds");
        assertTrue(BacteriaLifeLogic.inBounds(gen, 2, 2), "Center should be in bounds");
    }

    @Test
    @DisplayName("Should return false for invalid coordinates")
    void testInBounds_InvalidCoordinates() {
        int[][] gen = new int[5][5];

        assertFalse(BacteriaLifeLogic.inBounds(gen, -1, 0), "Negative row should be out of bounds");
        assertFalse(BacteriaLifeLogic.inBounds(gen, 0, -1), "Negative column should be out of bounds");
        assertFalse(BacteriaLifeLogic.inBounds(gen, 5, 0), "Row beyond bounds should be invalid");
        assertFalse(BacteriaLifeLogic.inBounds(gen, 0, 5), "Column beyond bounds should be invalid");
    }

    @Test
    @DisplayName("Should detect stable generation (no changes)")
    void testCheckStableGen_StableGeneration() {
        int[][] gen1 = {{1, 0, 1}, {0, 1, 0}, {1, 0, 1}};
        int[][] gen2 = {{1, 0, 1}, {0, 1, 0}, {1, 0, 1}};

        assertTrue(BacteriaLifeLogic.checkStableGen(gen1, gen2),
                "Identical generations should be stable");
    }

    @Test
    @DisplayName("Should detect unstable generation (changes occurred)")
    void testCheckStableGen_UnstableGeneration() {
        int[][] gen1 = {{1, 0, 1}, {0, 1, 0}, {1, 0, 1}};
        int[][] gen2 = {{1, 0, 1}, {0, 0, 0}, {1, 0, 1}};

        assertFalse(BacteriaLifeLogic.checkStableGen(gen1, gen2),
                "Different generations should not be stable");
    }

    @Test
    @DisplayName("Should increment round counter when generating new generation")
    void testGetRound_Increment() {
        assertEquals(0, logic.getRound(), "Initial round should be 0");

        int[][] gen = logic.generateInitialGen();
        logic.generateNewGen(gen);

        assertEquals(1, logic.getRound(), "Round should increment after generating new generation");
    }

    @Test
    @DisplayName("Should handle multiple generations")
    void testGenerateNewGen_MultipleGenerations() {
        BacteriaLifeLogic testLogic = new BacteriaLifeLogic(5);
        int[][] gen = testLogic.generateInitialGen();

        for (int i = 0; i < 10; i++) {
            gen = testLogic.generateNewGen(gen);
            assertNotNull(gen, "Generation should not be null after multiple iterations");
        }

        assertEquals(10, testLogic.getRound(), "Round counter should match iterations");
    }

    @Test
    @DisplayName("Should handle empty generation (all dead)")
    void testGenerateNewGen_EmptyGeneration() {
        int[][] gen = new int[5][5]; // All zeros
        BacteriaLifeLogic testLogic = new BacteriaLifeLogic(5);

        int[][] newGen = testLogic.generateNewGen(gen);

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                assertEquals(0, newGen[i][j], "Empty generation should remain empty");
            }
        }
    }

    @Test
    @DisplayName("Should handle full generation (all alive)")
    void testGenerateNewGen_FullGeneration() {
        int[][] gen = new int[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                gen[i][j] = 1;
            }
        }

        BacteriaLifeLogic testLogic = new BacteriaLifeLogic(5);
        int[][] newGen = testLogic.generateNewGen(gen);

        // Most cells should die due to suffocation (8 neighbors)
        int aliveCells = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (newGen[i][j] == 1) aliveCells++;
            }
        }

        assertTrue(aliveCells < 25, "Full generation should result in many deaths");
    }

    @Test
    @DisplayName("Should handle oscillating pattern (blinker)")
    void testGenerateNewGen_OscillatingPattern() {
        int[][] gen = new int[5][5];
        // Horizontal blinker
        gen[2][1] = 1;
        gen[2][2] = 1;
        gen[2][3] = 1;

        BacteriaLifeLogic testLogic = new BacteriaLifeLogic(5);
        int[][] newGen = testLogic.generateNewGen(gen);

        // Should become vertical
        assertEquals(1, newGen[1][2], "Blinker should oscillate to vertical");
        assertEquals(1, newGen[2][2], "Center should remain");
        assertEquals(1, newGen[3][2], "Blinker should oscillate to vertical");
    }

    @Test
    @DisplayName("Should handle stable pattern (block)")
    void testGenerateNewGen_StableBlock() {
        int[][] gen = new int[5][5];
        // 2x2 block (stable pattern)
        gen[2][2] = 1;
        gen[2][3] = 1;
        gen[3][2] = 1;
        gen[3][3] = 1;

        BacteriaLifeLogic testLogic = new BacteriaLifeLogic(5);
        int[][] newGen = testLogic.generateNewGen(gen);

        assertEquals(1, newGen[2][2], "Block should remain stable");
        assertEquals(1, newGen[2][3], "Block should remain stable");
        assertEquals(1, newGen[3][2], "Block should remain stable");
        assertEquals(1, newGen[3][3], "Block should remain stable");
    }

    @Test
    @DisplayName("Should not modify original generation array")
    void testGenerateNewGen_DoesNotModifyOriginal() {
        int[][] gen = new int[5][5];
        gen[2][2] = 1;
        gen[2][3] = 1;
        gen[2][4] = 1;

        // Create a copy for comparison
        int[][] originalCopy = new int[5][5];
        for (int i = 0; i < 5; i++) {
            System.arraycopy(gen[i], 0, originalCopy[i], 0, 5);
        }

        BacteriaLifeLogic testLogic = new BacteriaLifeLogic(5);
        testLogic.generateNewGen(gen);

        assertArrayEquals(originalCopy, gen,
                "Original generation should not be modified");
    }
}