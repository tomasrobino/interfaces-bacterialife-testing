import org.example.Circle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Circle Tests")
class CircleTest {

    private Circle circle;
    private static final int EXPECTED_SIZE = 10; // BACTERIA_SIZE from BacteriaLifeUI

    @BeforeEach
    void setUp() {
        circle = new Circle(Color.BLACK);
    }

    @Test
    @DisplayName("Should create circle with specified color")
    void testConstructor_WithColor() {
        Circle blackCircle = new Circle(Color.BLACK);
        Circle whiteCircle = new Circle(Color.WHITE);

        assertEquals(Color.BLACK, blackCircle.getColor(), "Circle should have black color");
        assertEquals(Color.WHITE, whiteCircle.getColor(), "Circle should have white color");
    }

    @Test
    @DisplayName("Should not be enabled by default")
    void testConstructor_NotEnabled() {
        assertFalse(circle.isEnabled(), "Circle should not be enabled");
    }

    @Test
    @DisplayName("Should have correct preferred size")
    void testGetPreferredSize() {
        Dimension size = circle.getPreferredSize();

        assertNotNull(size, "Preferred size should not be null");
        assertEquals(EXPECTED_SIZE, size.width, "Width should match bacteria size");
        assertEquals(EXPECTED_SIZE, size.height, "Height should match bacteria size");
    }

    @Test
    @DisplayName("Should update color when setCircleColor is called")
    void testSetCircleColor() {
        circle.setCircleColor(Color.RED);
        assertEquals(Color.RED, circle.getColor(), "Color should be updated to red");

        circle.setCircleColor(Color.BLUE);
        assertEquals(Color.BLUE, circle.getColor(), "Color should be updated to blue");
    }

    @Test
    @DisplayName("Should return current color")
    void testGetColor() {
        Circle redCircle = new Circle(Color.RED);
        assertEquals(Color.RED, redCircle.getColor(), "Should return the current color");
    }

    @Test
    @DisplayName("Should handle null color gracefully")
    void testSetCircleColor_NullColor() {
        // This tests if the component can handle null without throwing exception
        assertDoesNotThrow(() -> circle.setCircleColor(null),
                "Setting null color should not throw exception");
    }

    @Test
    @DisplayName("Should not be content area filled")
    void testButtonProperties_ContentAreaFilled() {
        assertFalse(circle.isContentAreaFilled(),
                "Content area should not be filled");
    }

    @Test
    @DisplayName("Should not have border painted")
    void testButtonProperties_BorderPainted() {
        assertFalse(circle.isBorderPainted(),
                "Border should not be painted");
    }

    @Test
    @DisplayName("Should not have focus painted")
    void testButtonProperties_FocusPainted() {
        assertFalse(circle.isFocusPainted(),
                "Focus should not be painted");
    }

    @Test
    @DisplayName("Should not be opaque")
    void testButtonProperties_Opaque() {
        assertFalse(circle.isOpaque(),
                "Circle should not be opaque");
    }

    @Test
    @DisplayName("Should handle color changes multiple times")
    void testSetCircleColor_MultipleChanges() {
        circle.setCircleColor(Color.RED);
        assertEquals(Color.RED, circle.getColor());

        circle.setCircleColor(Color.GREEN);
        assertEquals(Color.GREEN, circle.getColor());

        circle.setCircleColor(Color.BLUE);
        assertEquals(Color.BLUE, circle.getColor());

        circle.setCircleColor(Color.WHITE);
        assertEquals(Color.WHITE, circle.getColor());
    }

    @Test
    @DisplayName("Should create circles with different colors independently")
    void testMultipleCircles_IndependentColors() {
        Circle circle1 = new Circle(Color.BLACK);
        Circle circle2 = new Circle(Color.WHITE);
        Circle circle3 = new Circle(Color.RED);

        assertEquals(Color.BLACK, circle1.getColor());
        assertEquals(Color.WHITE, circle2.getColor());
        assertEquals(Color.RED, circle3.getColor());

        // Changing one shouldn't affect others
        circle1.setCircleColor(Color.BLUE);
        assertEquals(Color.BLUE, circle1.getColor());
        assertEquals(Color.WHITE, circle2.getColor());
        assertEquals(Color.RED, circle3.getColor());
    }

    @Test
    @DisplayName("Should maintain size after color changes")
    void testPreferredSize_RemainsConstant() {
        Dimension sizeBefore = circle.getPreferredSize();

        circle.setCircleColor(Color.RED);
        Dimension sizeAfter = circle.getPreferredSize();

        assertEquals(sizeBefore.width, sizeAfter.width,
                "Width should remain constant after color change");
        assertEquals(sizeBefore.height, sizeAfter.height,
                "Height should remain constant after color change");
    }

    @Test
    @DisplayName("Should handle custom colors")
    void testCustomColors() {
        Color customColor = new Color(100, 150, 200);
        circle.setCircleColor(customColor);

        assertEquals(customColor, circle.getColor(),
                "Should handle custom RGB colors");
    }
}