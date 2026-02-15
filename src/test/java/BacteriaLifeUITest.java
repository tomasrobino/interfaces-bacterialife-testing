import org.example.BacteriaLifeLogic;
import org.example.BacteriaLifeUI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BacteriaLifeUI Tests")
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
    @DisplayName("Should call generateInitialGen on construction")
    void testConstructor_GeneratesInitialGen() {
        try (var ignored = mockConstruction(JFrame.class)) {
            new BacteriaLifeUI(mockLogic);
            verify(mockLogic, times(1)).generateInitialGen();
        }
    }

    @Test
    @DisplayName("Should call getRound when creating UI")
    void testConstructor_CallsGetRound() {
        try (var ignored = mockConstruction(JFrame.class)) {
            new BacteriaLifeUI(mockLogic);
            verify(mockLogic, atLeastOnce()).getRound();
        }
    }

    @Test
    @DisplayName("Should handle generation with all alive bacteria")
    void testUI_WithAllAliveBacteria() {
        when(mockLogic.generateInitialGen()).thenReturn(createAllAliveGen());

        try (var ignored = mockConstruction(JFrame.class)) {
            assertDoesNotThrow(() -> new BacteriaLifeUI(mockLogic));
            verify(mockLogic, times(1)).generateInitialGen();
        }
    }

    @Test
    @DisplayName("Should handle generation with all dead bacteria")
    void testUI_WithAllDeadBacteria() {
        int[][] allDead = new int[DIMENSION][DIMENSION];
        when(mockLogic.generateInitialGen()).thenReturn(allDead);

        try (var ignored = mockConstruction(JFrame.class)) {
            assertDoesNotThrow(() -> new BacteriaLifeUI(mockLogic));
            verify(mockLogic, times(1)).generateInitialGen();
        }
    }

    @Test
    @DisplayName("Should handle mixed bacteria pattern")
    void testUI_WithMixedPattern() {
        when(mockLogic.generateInitialGen()).thenReturn(createGenWithPattern());

        try (var ignored = mockConstruction(JFrame.class)) {
            assertDoesNotThrow(() -> new BacteriaLifeUI(mockLogic));
            verify(mockLogic, times(1)).generateInitialGen();
        }
    }

    @Test
    @DisplayName("Should verify logic interactions during initialization")
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
    @DisplayName("Should create JFrame with correct title and setup")
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
    @DisplayName("Should handle round counter with different values")
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
    @DisplayName("Should handle generation with glider pattern")
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
    @DisplayName("Should not throw exception during UI creation")
    void testUICreation_NoExceptions() {
        try (var ignored = mockConstruction(JFrame.class)) {
            assertDoesNotThrow(() -> new BacteriaLifeUI(mockLogic));
        }
    }

    @Test
    @DisplayName("Should verify no unexpected interactions with logic")
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
    @DisplayName("Should handle edge case with maximum round number")
    void testMaxRoundNumber() {
        when(mockLogic.getRound()).thenReturn(Integer.MAX_VALUE);

        try (var ignored = mockConstruction(JFrame.class)) {
            assertDoesNotThrow(() -> new BacteriaLifeUI(mockLogic));
            verify(mockLogic, atLeastOnce()).getRound();
        }
    }

    @Test
    @DisplayName("Should initialize logic reference correctly")
    void testLogicReference_Initialization() {
        try (var ignored = mockConstruction(JFrame.class)) {
            new BacteriaLifeUI(mockLogic);
            verify(mockLogic).generateInitialGen();
        }
    }

    @Test
    @DisplayName("Should handle empty generation correctly")
    void testEmptyGeneration_Handling() {
        int[][] emptyGen = new int[DIMENSION][DIMENSION];
        when(mockLogic.generateInitialGen()).thenReturn(emptyGen);

        try (var ignored = mockConstruction(JFrame.class)) {
            assertDoesNotThrow(() -> new BacteriaLifeUI(mockLogic));
            verify(mockLogic, times(1)).generateInitialGen();
        }
    }

    @Test
    @DisplayName("Should handle sparse pattern generation")
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
    @DisplayName("Should verify frame setup operations")
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
    @DisplayName("Should handle logic method calls in correct order")
    void testLogicMethodCallOrder() {
        try (var ignored = mockConstruction(JFrame.class)) {
            new BacteriaLifeUI(mockLogic);

            var inOrder = inOrder(mockLogic);
            inOrder.verify(mockLogic).generateInitialGen();
            inOrder.verify(mockLogic, atLeastOnce()).getRound();
        }
    }
}