package tests.filterTest;

import filters.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test the parser.
 */
public class TestParser {

    /*****************************************Test BasicFilter starts here****************************/
    @Test
    public void testBasic() throws SyntaxError {
        Filter f = new Parser("trump").parse();
        assertTrue(f instanceof BasicFilter);
        assertTrue(((BasicFilter)f).toString().equals("trump"));
    }

    @Test()
    public void testBasicWithTwoWords() throws SyntaxError {
        Exception e = assertThrows(SyntaxError.class, () -> new Parser("green banana").parse());
        assertTrue(e instanceof SyntaxError);
        assertEquals(e.getMessage(), "Extra stuff at end of input");
    }

    @Test()
    public void testBasicWithThreeWords() throws SyntaxError {
        Exception e =
                assertThrows(SyntaxError.class, () -> new Parser("green banana or yellow banana").parse());
        assertTrue(e instanceof SyntaxError);
        assertEquals(e.getMessage(), "Extra stuff at end of input");
    }

    /*****************************************Test OrFilter starts here****************************/
    @Test
    public void testOr() throws SyntaxError {
        Filter f = new Parser("trump or biden").parse();
        assertTrue(f instanceof OrFilter);
        assertTrue(((OrFilter)f).toString().equals("(trump or biden)"));
    }

    /*****************************************Test AndFilter starts here****************************/
    @Test
    public void testAnd() throws SyntaxError {
        Filter f = new Parser("trump and biden").parse();
        assertTrue(f instanceof AndFilter);
        assertTrue(((AndFilter)f).toString().equals("(trump and biden)"));
    }

    /*****************************************Test NotFilter starts here****************************/
    @Test
    public void testNot() throws SyntaxError {
        Filter f = new Parser("not trump").parse();
        assertTrue(f instanceof NotFilter);
        assertTrue(((NotFilter)f).toString().equals("not trump"));
    }

    /*****************************************Test MixedExpressions starts here****************************/
    @Test
    public void testMixedExpression1() throws SyntaxError {
        Filter x = new Parser("trump and (evil or blue) and red or green and not not purple").parse();
        assertTrue(x.toString().equals("(((trump and (evil or blue)) and red) or (green and not not purple))"));
    }

    @Test
    public void testMixedExpression2() throws SyntaxError {
        Filter x = new Parser("green and not banana or apple and orange or not pear").parse();
        assertEquals(x.toString(),("(((green and not banana) or (apple and orange)) or not pear)"));
    }

    @Test
    public void testMixedExpression3() throws SyntaxError {
        Filter x = new Parser("green and not (banana or apple) and orange or not pear").parse();
        assertEquals(x.toString(),("(((green and not (banana or apple)) and orange) or not pear)"));
    }
}
