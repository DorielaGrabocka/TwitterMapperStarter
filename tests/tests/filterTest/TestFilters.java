package tests.filterTest;

import filters.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import twitter4j.*;



import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


public class TestFilters {

    /*****************************Test Basic Filter starts here***************************************/
    @Test
    public void testBasic() {
        Filter f = new BasicFilter("fred");
        assertTrue(f.matches(makeStatus("Fred Flintstone")));
        assertTrue(f.matches(makeStatus("fred Flintstone")));
        assertFalse(f.matches(makeStatus("Red Skelton")));
        assertFalse(f.matches(makeStatus("red Skelton")));
    }

    @Test
    public void testBasicToString() {
        Filter f = new BasicFilter("fred");
        assertEquals("fred", f.toString());
    }

    @Test
    public void testBasicTerms() {
        Filter f = new BasicFilter("fred");
        assertTrue(f.terms().contains("fred"));
        assertFalse(f.terms().contains("fred111"));
    }

    /******************************************Test Not Filter starts here***************************************/
    @Test
    public void testNot() {
        Filter f = new NotFilter(new BasicFilter("fred"));
        assertFalse(f.matches(makeStatus("Fred Flintstone")));
        assertFalse(f.matches(makeStatus("fred Flintstone")));
        assertTrue(f.matches(makeStatus("Red Skelton")));
        assertTrue(f.matches(makeStatus("red Skelton")));
    }

    @Test
    public void testNotToString() {
        Filter f = new NotFilter(new BasicFilter("fred"));
        assertEquals("not fred",f.toString());
    }

    @Test
    public void testNotTerms() {
        Filter f = new NotFilter(new BasicFilter("fred"));
        assertTrue(f.terms().contains("fred"));
        assertFalse(f.terms().contains("ed"));
    }

    /*************************************Test And Filter starts here***************************************/
    @Test
    public void testAnd(){
        Filter f = new AndFilter(
                new BasicFilter("Fred"),
                new BasicFilter("Flinstone")
        );
        assertTrue(f.matches(makeStatus("Fred Flinstone")));
        assertTrue(f.matches(makeStatus("fred Flinstone")));
        assertTrue(f.matches(makeStatus("Fred flinstone")));
        assertFalse(f.matches(makeStatus("Fred tstone")));
        assertFalse(f.matches(makeStatus("Red Skelton")));
        assertFalse(f.matches(makeStatus("red Skelton")));
    }

    @Test
    public void testAndToString() {
        Filter f = new AndFilter(
                new BasicFilter("green"),
                new BasicFilter("clovers")
        );
        assertEquals("(green and clovers)",f.toString());
    }

    @Test
    public void testAndTerms(){
        Filter f = new AndFilter(
                new BasicFilter("Fred"),
                new BasicFilter("Flinstone")
        );

        List<String> terms = f.terms();
        assertTrue(terms.contains("Fred"));
        assertTrue(terms.contains("Flinstone"));
        assertFalse(terms.contains("bread"));
    }

    /*******************************************Test Or Filter starts here************************************/
    @Test
    public void testOr(){
        Filter f = new OrFilter(
                new BasicFilter("Fred"),
                new BasicFilter("Flinstone")
        );
        assertTrue(f.matches(makeStatus("Fred Flinstone")));
        assertTrue(f.matches(makeStatus("fred1 Flinstone")));
        assertTrue(f.matches(makeStatus("Fred flinstone1")));
        assertFalse(f.matches(makeStatus("eda stone")));
    }

    @Test
    public void testOrToString() {
        Filter f = new OrFilter(
                new BasicFilter("green"),
                new BasicFilter("clovers")
        );
        assertEquals("(green or clovers)",f.toString());
    }

    @Test
    public void testOrTerms(){
        Filter f = new OrFilter(
                new BasicFilter("Fred"),
                new BasicFilter("Flinstone")
        );

        List<String> terms = f.terms();
        assertTrue(terms.contains("Fred"));
        assertTrue(terms.contains("Flinstone"));
        assertFalse(terms.contains("bread"));
    }


    /**Helper method that creates a mock  object representing a status
     * @param text - is the supposed text of the status
     * @return  a mocked status object
     * */
    private Status makeStatus(String text) {
        Status mockedStatus = Mockito.mock(Status.class);
        Mockito.when(mockedStatus.getText()).thenReturn(text);
        return mockedStatus;
    }
}
