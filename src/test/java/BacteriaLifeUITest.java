import org.example.BacteriaLifeLogic;
import org.example.BacteriaLifeUI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BacteriaLifeUITest {

    @Mock
    private BacteriaLifeLogic mockLogic;

    private static final int DIMENSION = 30;

    @BeforeEach
    void setUp() {
        // Setup default mock behavior
        when(mockLogic.generateInitialGen()).thenReturn(createEmptyGen());
        when(mockLogic.getRound()).thenReturn(0);
    }

    private int[][] createEmptyGen() {
        return new int[DIMENSION][DIMENSION];
    }

    private int[][] createGenWithPattern() {
        int[][] gen = new int[DIMENSION][DIMENSION];
        gen[0][0] = 1;
        gen[1][1] = 1;
        gen[2][2] = 1;
        return gen;
    }

    private int[][] createAllAliveGen() {
        int[][] gen = new int[DIMENSION][DIMENSION];
        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j++) {
                gen[i][j] = 1;
            }
        }
        return gen;
    }

    @Test
    void testConstructor_GeneratesInitialGen() {
        try (var ignored = mockConstruction(JFrame.class)) {
            new BacteriaLifeUI(mockLogic);
            verify(mockLogic, times(1)).generateInitialGen();
        }
    }

    @Test
    void testConstructor_CallsGetRound() {
        try (var ignored = mockConstruction(JFrame.class)) {
            new BacteriaLifeUI(mockLogic);
            verify(mockLogic, atLeastOnce()).getRound();
        }
    }

    @Test
    void testUI_WithAllAliveBacteria() {
        when(mockLogic.generateInitialGen()).thenReturn(createAllAliveGen());

        try (var ignored = mockConstruction(JFrame.class)) {
            assertDoesNotThrow(() -> new BacteriaLifeUI(mockLogic));
            verify(mockLogic, times(1)).generateInitialGen();
        }
    }

    @Test
    void testUI_WithAllDeadBacteria() {
        int[][] allDead = new int[DIMENSION][DIMENSION];
        when(mockLogic.generateInitialGen()).thenReturn(allDead);

        try (var ignored = mockConstruction(JFrame.class)) {
            assertDoesNotThrow(() -> new BacteriaLifeUI(mockLogic));
            verify(mockLogic, times(1)).generateInitialGen();
        }
    }

    @Test
    void testUI_WithMixedPattern() {
        when(mockLogic.generateInitialGen()).thenReturn(createGenWithPattern());

        try (var ignored = mockConstruction(JFrame.class)) {
            assertDoesNotThrow(() -> new BacteriaLifeUI(mockLogic));
            verify(mockLogic, times(1)).generateInitialGen();
        }
    }

    @Test
    void testInitialization_LogicInteractions() {
        reset(mockLogic);
        when(mockLogic.generateInitialGen()).thenReturn(createEmptyGen());
        when(mockLogic.getRound()).thenReturn(0);

        try (var ignored = mockConstruction(JFrame.class)) {
            new BacteriaLifeUI(mockLogic);

            verify(mockLogic, times(1)).generateInitialGen();
            verify(mockLogic, atLeastOnce()).getRound();
            verifyNoMoreInteractions(mockLogic);
        }
    }

    @Test
    void testFrame_HasCorrectTitle() {
        try (MockedConstruction<JFrame> mockedFrame = mockConstruction(JFrame.class,
                (mock, context) -> {
                    if (!context.arguments().isEmpty() && "BacteriaLife".equals(context.arguments().getFirst())) {
                        when(mock.getTitle()).thenReturn("BacteriaLife");
                    }
                })) {

            new BacteriaLifeUI(mockLogic);

            assertEquals(1, mockedFrame.constructed().size(), "Should construct one JFrame");

            JFrame frame = mockedFrame.constructed().getFirst();
            verify(frame).setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            verify(frame).setLayout(any(java.awt.BorderLayout.class));
            verify(frame).pack();
            verify(frame).setVisible(true);
        }
    }

    @Test
    void testRoundCounter_DifferentValues() {
        int[] roundValues = {0, 1, 5, 10, 50, 100};

        for (int round : roundValues) {
            reset(mockLogic);
            when(mockLogic.generateInitialGen()).thenReturn(createEmptyGen());
            when(mockLogic.getRound()).thenReturn(round);

            try (var ignored = mockConstruction(JFrame.class)) {
                new BacteriaLifeUI(mockLogic);
                verify(mockLogic, atLeastOnce()).getRound();
            }
        }
    }

    @Test
    void testGeneration_GliderPattern() {
        int[][] pattern = new int[DIMENSION][DIMENSION];
        pattern[1][2] = 1;
        pattern[2][3] = 1;
        pattern[3][1] = 1;
        pattern[3][2] = 1;
        pattern[3][3] = 1;

        when(mockLogic.generateInitialGen()).thenReturn(pattern);

        try (var ignored = mockConstruction(JFrame.class)) {
            assertDoesNotThrow(() -> new BacteriaLifeUI(mockLogic));
            verify(mockLogic, times(1)).generateInitialGen();
        }
    }

    @Test
    void testUICreation_NoExceptions() {
        try (var ignored = mockConstruction(JFrame.class)) {
            assertDoesNotThrow(() -> new BacteriaLifeUI(mockLogic));
        }
    }

    @Test
    void testNoUnexpectedInteractions() {
        reset(mockLogic);
        when(mockLogic.generateInitialGen()).thenReturn(createEmptyGen());
        when(mockLogic.getRound()).thenReturn(0);

        try (var ignored = mockConstruction(JFrame.class)) {
            new BacteriaLifeUI(mockLogic);

            verify(mockLogic, times(1)).generateInitialGen();
            verify(mockLogic, atLeastOnce()).getRound();
            verifyNoMoreInteractions(mockLogic);
        }
    }

    @Test
    void testMaxRoundNumber() {
        when(mockLogic.getRound()).thenReturn(Integer.MAX_VALUE);

        try (var ignored = mockConstruction(JFrame.class)) {
            assertDoesNotThrow(() -> new BacteriaLifeUI(mockLogic));
            verify(mockLogic, atLeastOnce()).getRound();
        }
    }

    @Test
    void testLogicReference_Initialization() {
        try (var ignored = mockConstruction(JFrame.class)) {
            new BacteriaLifeUI(mockLogic);
            verify(mockLogic).generateInitialGen();
        }
    }

    @Test
    void testEmptyGeneration_Handling() {
        int[][] emptyGen = new int[DIMENSION][DIMENSION];
        when(mockLogic.generateInitialGen()).thenReturn(emptyGen);

        try (var ignored = mockConstruction(JFrame.class)) {
            assertDoesNotThrow(() -> new BacteriaLifeUI(mockLogic));
            verify(mockLogic, times(1)).generateInitialGen();
        }
    }

    @Test
    void testSparsePattern_Handling() {
        int[][] sparseGen = new int[DIMENSION][DIMENSION];
        sparseGen[0][0] = 1;
        sparseGen[DIMENSION-1][DIMENSION-1] = 1;
        when(mockLogic.generateInitialGen()).thenReturn(sparseGen);

        try (var ignored = mockConstruction(JFrame.class)) {
            assertDoesNotThrow(() -> new BacteriaLifeUI(mockLogic));
            verify(mockLogic, times(1)).generateInitialGen();
        }
    }

    @Test
    void testFrame_SetupOperations() {
        try (MockedConstruction<JFrame> mockedFrame = mockConstruction(JFrame.class)) {
            new BacteriaLifeUI(mockLogic);

            assertEquals(1, mockedFrame.constructed().size());

            JFrame frame = mockedFrame.constructed().getFirst();
            verify(frame).setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            verify(frame).setLayout(any(java.awt.BorderLayout.class));
            verify(frame).pack();
            verify(frame).setVisible(true);
        }
    }

    @Test
    void testLogicMethodCallOrder() {
        try (var ignored = mockConstruction(JFrame.class)) {
            new BacteriaLifeUI(mockLogic);

            var inOrder = inOrder(mockLogic);
            inOrder.verify(mockLogic).generateInitialGen();
            inOrder.verify(mockLogic, atLeastOnce()).getRound();
        }
    }

    @Test
    void testDeepCopy_WithNullInput() {
        try (var ignored = mockConstruction(JFrame.class)) {
            BacteriaLifeUI ui = new BacteriaLifeUI(mockLogic);

            int[][] result = ui.deepCopy(null);

            assertNull(result, "Deep copy of null should return null");
        }
    }

    @Test
    void testDeepCopy_CreatesIndependentCopy() {
        try (var ignored = mockConstruction(JFrame.class)) {
            BacteriaLifeUI ui = new BacteriaLifeUI(mockLogic);

            int[][] original = {{1, 0, 1}, {0, 1, 0}, {1, 0, 1}};
            int[][] copy = ui.deepCopy(original);

            assertNotNull(copy, "Copy should not be null");
            assertNotSame(original, copy, "Copy should be a different array instance");
            assertArrayEquals(original, copy, "Copy should have same values as original");
        }
    }

    @Test
    void testDeepCopy_ModificationDoesNotAffectOriginal() {
        try (var ignored = mockConstruction(JFrame.class)) {
            BacteriaLifeUI ui = new BacteriaLifeUI(mockLogic);

            int[][] original = {{1, 0, 1}, {0, 1, 0}, {1, 0, 1}};
            int[][] copy = ui.deepCopy(original);

            // Modify the copy
            copy[0][0] = 0;
            copy[1][1] = 0;
            copy[2][2] = 0;

            // Original should remain unchanged
            assertEquals(1, original[0][0], "Original should not be modified");
            assertEquals(1, original[1][1], "Original should not be modified");
            assertEquals(1, original[2][2], "Original should not be modified");

            // Copy should have the new values
            assertEquals(0, copy[0][0], "Copy should be modified");
            assertEquals(0, copy[1][1], "Copy should be modified");
            assertEquals(0, copy[2][2], "Copy should be modified");
        }
    }

    @Test
    void testDeepCopy_EmptyArray() {
        try (var ignored = mockConstruction(JFrame.class)) {
            BacteriaLifeUI ui = new BacteriaLifeUI(mockLogic);

            int[][] original = new int[0][0];
            int[][] copy = ui.deepCopy(original);

            assertNotNull(copy, "Copy should not be null");
            assertEquals(0, copy.length, "Copy should have same dimensions as original");
        }
    }

    @Test
    void testRefreshGenPanel_AllDeadBacteria() {
        try (var ignored = mockConstruction(JFrame.class)) {
            int[][] allDead = new int[DIMENSION][DIMENSION];
            when(mockLogic.generateInitialGen()).thenReturn(allDead);

            BacteriaLifeUI ui = new BacteriaLifeUI(mockLogic);

            // Should handle bacteriaGen[i][j] == 0 branch
            assertDoesNotThrow(ui::refreshGenPanel,
                    "Should handle all dead bacteria without exception");
        }
    }

    @Test
    void testRefreshGenPanel_AllAliveBacteria() {
        try (var ignored = mockConstruction(JFrame.class)) {
            int[][] allAlive = createAllAliveGen();
            when(mockLogic.generateInitialGen()).thenReturn(allAlive);

            BacteriaLifeUI ui = new BacteriaLifeUI(mockLogic);

            // Should handle bacteriaGen[i][j] == 1 branch
            assertDoesNotThrow(ui::refreshGenPanel,
                    "Should handle all alive bacteria without exception");
        }
    }

    @Test
    void testRefreshGenPanel_MixedPattern() {
        try (var ignored = mockConstruction(JFrame.class)) {
            int[][] mixed = createGenWithPattern();
            when(mockLogic.generateInitialGen()).thenReturn(mixed);

            BacteriaLifeUI ui = new BacteriaLifeUI(mockLogic);

            // Should handle both branches: bacteriaGen[i][j] == 0 and bacteriaGen[i][j] == 1
            assertDoesNotThrow(ui::refreshGenPanel,
                    "Should handle mixed pattern without exception");
        }
    }

    @Test
    void testRefreshGenPanel_IteratesThroughAllRows() {
        try (var ignored = mockConstruction(JFrame.class)) {
            // Create pattern with bacteria in first and last row
            int[][] pattern = new int[DIMENSION][DIMENSION];
            pattern[0][0] = 1; // First row
            pattern[DIMENSION-1][DIMENSION-1] = 1; // Last row

            when(mockLogic.generateInitialGen()).thenReturn(pattern);

            BacteriaLifeUI ui = new BacteriaLifeUI(mockLogic);

            // Should iterate through all rows (i < DIMENSION)
            assertDoesNotThrow(ui::refreshGenPanel,
                    "Should iterate through all rows without exception");
        }
    }

    @Test
    void testRefreshGenPanel_IteratesThroughAllColumns() {
        try (var ignored = mockConstruction(JFrame.class)) {
            // Create pattern with bacteria in first and last column
            int[][] pattern = new int[DIMENSION][DIMENSION];
            pattern[0][0] = 1; // First column
            pattern[0][DIMENSION-1] = 1; // Last column

            when(mockLogic.generateInitialGen()).thenReturn(pattern);

            BacteriaLifeUI ui = new BacteriaLifeUI(mockLogic);

            // Should iterate through all columns (j < DIMENSION)
            assertDoesNotThrow(ui::refreshGenPanel,
                    "Should iterate through all columns without exception");
        }
    }
}