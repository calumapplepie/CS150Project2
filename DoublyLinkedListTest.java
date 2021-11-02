import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// get more test coverage, easily!
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

// We will use this to build expected values for the parametized tests
import java.util.ArrayList;
/**
 * This tests the DoublyLinkedList class.   It is very thorough, using parameterized tests
 * for extra coverage.  
 * 
 * It may look suspicously similar to linked list test classes that I have submitted in
 * the past, both for various labs and for previous projects.  Typographic errors that
 * imply that this class should be abstract or generic are just that: typographic errors.
 * Pay them no attention.
 *
 * @author  Calum McConnell
 * @version 0.0.1
 */
public class DoublyLinkedListTest
{   
    // Sample arrays we will work each method over: initialized in setUp();
    protected DoublyLinkedList<Integer> emptyArray;
    protected DoublyLinkedList<Integer> increasingToNineArray;
    protected DoublyLinkedList<Integer> increasingToTwentyArray;
    protected DoublyLinkedList<Integer> halfEmptiedArray;
    protected DoublyLinkedList<Integer> decreasingFromTwentyArray;
    
    // For when a method should behave a certain way regardless of what you call it on,
    // make it easy to call it on all of our different arrays
    protected DoublyLinkedList<Integer>[] testArraysArray;
    
    // the array from which the other arrays are filled.
    protected Integer[] expectedValues;
    
    /**
     * This method will create the various arrays I use.
     * 
     * It is also responsible for (deterministically!) filling the
     * arrays it generates.
     */
    public void createArrays(){
        expectedValues = new Integer[20];
        // an array of generics cannot require that all the generics inserted into
        // it are parameterized off the same type: I'm still not entirely clear why.
        // By declaring it like this, we make it clear to the compiler that we don't
        // expect it to try that
        testArraysArray = new DoublyLinkedList[5];
        
        for(int i = 0; i < expectedValues.length; i++){
            expectedValues[i] = i;
        }
        
        emptyArray = new DoublyLinkedList<Integer>();
        increasingToNineArray = new DoublyLinkedList<Integer>();
        increasingToTwentyArray = new DoublyLinkedList<Integer>();
        halfEmptiedArray = new DoublyLinkedList<Integer>();
        decreasingFromTwentyArray = new DoublyLinkedList<Integer>();
    }
    
    /**
     * This method returns some integer.  The object it returns
     * should compare equal to any other object it returned, but should
     * really be a different object.  Basically, this gives us a constant,
     * which we can test to see is properly retreived.
     */
    public Integer getSampleValue(){
        return 999;
    }
    
    /**
     * Sets up the test fixture.
     *
     * This (re-)initializes the various arrays.  It delegates to
     * createArrays for the actual object allocation: for reasons
     * that are probably fairly clear.
     */
    @BeforeEach
    public void setUp()
    {
        createArrays();
        
        // That initialized the arrays: so now we fill them
        for(int i = 0; i < 9; i++){
            increasingToNineArray.add(expectedValues[i]);
        }   
        for(int i = 0; i < 20; i++){
            increasingToTwentyArray.add(expectedValues[i]);
        }
        
        // Now make a partially emptied array
        halfEmptiedArray = increasingToTwentyArray.clone();
        for(int i = 20; i > 10; i--){
            halfEmptiedArray.remove(0);
        }
        
        // And a decending array
        for(int i = 19; i >= 0; i--){
            decreasingFromTwentyArray.add(expectedValues[i]);
        }
        
        // Now build the array-of-arrays
        testArraysArray[0] = emptyArray;
        testArraysArray[1] = increasingToNineArray;
        testArraysArray[2] = increasingToTwentyArray;
        testArraysArray[3] = halfEmptiedArray;
        testArraysArray[4] = decreasingFromTwentyArray;
        
    }
    
    /**
     * Test removing the fakePop() target.
     */
    @Test
    public void testRemovingNext(){
        for(DoublyLinkedList<Integer> i : testArraysArray){
            DoublyLinkedList<Integer> iClone = i.clone();
            // skip if we are on the empty
            if(i.size() < 4){
                continue;
            }
            // make sure we're at the start of the list
            i.resetFakeQueue();
            
            // throw away input
            i.fakePop();
            iClone.remove(0);
            assertEquals(i.fakePop(),iClone.fakePop());
            // do the same for a non-head index
            iClone.remove(1);
            i.fakePop();
            assertEquals(i.fakePop(),iClone.fakePop());
        }
    }
        
    /**
     * This tests the array of arrays in the test framework itself.
     * This is to ensure that the array exists properly, and won't
     * cause breakage when I use it in other tests
     */
    @Test
    public void arrayOfArraysWorks(){
        // This also break any later tests if it isn't reset
        for(DoublyLinkedList i : testArraysArray){
            i.add(1);
            assertEquals(1,i.get(i.size()-1));
        }
    }
    
    /**
     * This tests the size() method
     */
    @Test
    public void testSizes(){
         assertEquals(0,emptyArray.size());
         assertEquals(9,increasingToNineArray.size());
         assertEquals(20,increasingToTwentyArray.size());
         assertEquals(10,halfEmptiedArray.size());
         assertEquals(20,decreasingFromTwentyArray.size());         
    }
    
    /**
     * This checks that the values in our expectedValues array were properly
     * assigned to their respective spots in the various arrays.
     * It also tests the get() method
     */
    @Test
    public void verifyTestArrays(){
        // First verify the increasing arrays
        for(int i = 0; i < 20; i ++){
            assertEquals(expectedValues[i],increasingToTwentyArray.get(i));
            assertEquals(expectedValues[19-i],decreasingFromTwentyArray.get(i));
            if(i < 9){
                assertEquals(expectedValues[i],increasingToNineArray.get(i));
            }
            if(i > 9){
                assertEquals(expectedValues[i], halfEmptiedArray.get(i-10));
            }
        }
    }
    
    /**
     * This makes sure that isEmpty works properly
     */
    @Test
    public void testIsEmpty(){
        // Test the sample arrays
        assertTrue(emptyArray.isEmpty());
        assertFalse(increasingToNineArray.isEmpty());
        assertFalse(increasingToTwentyArray.isEmpty());
        assertFalse(halfEmptiedArray.isEmpty());
        
        // Now see if it changes properly
        emptyArray.add(getSampleValue());
        assertFalse(emptyArray.isEmpty());
        emptyArray.remove(0);
        assertTrue(emptyArray.isEmpty());
        
        // Does the clear method change it?
        increasingToNineArray.clear();
        assertTrue(increasingToNineArray.isEmpty());
    }    
            
    /**
     * Checks if remove() works right, on a bunch of different inputs.
     * This works with a 9-member array, so the backing array shouldn't
     * resize after use
     */
    @ParameterizedTest
    @ValueSource(ints = {0,1,2,3,4,5,6,7,8})
    public void testRemoveParameterizedSmall(int i){
        // We will be testing contents with this string we are now building
        ArrayList<Integer> expected = convertToArrayList(increasingToNineArray);
        increasingToNineArray.remove(i);
        
        // Modify the expected array appropriately
        expected.remove(i);
        
        assertEquals(expected,convertToArrayList(increasingToNineArray));
        
        // Double check the size() method
        assertEquals(8,increasingToNineArray.size());
    }
    
    /**
     * Checks if remove() works right, on a bunch of different inputs.
     */
    @ParameterizedTest
    @ValueSource(ints = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19})
    public void testRemoveParameterizedBig(int i){
        // We will be testing contents with this string we are now building
        ArrayList<Integer> expected = convertToArrayList(increasingToTwentyArray);
        increasingToTwentyArray.remove(i);
        
        // Modify the expected one appropriately
        expected.remove(i);
        assertEquals(expected,convertToArrayList(increasingToTwentyArray));
        
        // ensure size() is correct
        assertEquals(19,increasingToTwentyArray.size());
    }
    
    /**
     * Checks if add() works right, on a bunch of different inputs.
     * This works with a 9-member array, so the backing array should
     * resize right after use
     */
    @ParameterizedTest
    @ValueSource(ints = {0,1,2,3,4,5,6,7,8,9})
    public void testAddParameterizedSmall(int i){
        // We will be testing contents with this string we are now building
        ArrayList<Integer> expected = convertToArrayList(increasingToNineArray);
        increasingToNineArray.add(i,getSampleValue());
        
        // Modify the expected array appropriately
        expected.add(i,getSampleValue());
        
        assertEquals(expected,convertToArrayList(increasingToNineArray));
        
        //Also test the array sizes, while we're here
        assertEquals(10,increasingToNineArray.size());        
    }
    
    /**
     * Checks if add() works right, on a bunch of different inputs.
     * This is on the 20 member array, so it shouldn't resize after use
     */
    @ParameterizedTest
    @ValueSource(ints = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20})
    public void testAddParameterizedBig(int i){
        // We will be testing contents with this string we are now building
        ArrayList<Integer> expected = convertToArrayList(increasingToTwentyArray);
        increasingToTwentyArray.add(i,getSampleValue());
        
        // Modify the expected one appropriately
        expected.add(i,getSampleValue());
 
        assertEquals(expected,convertToArrayList(increasingToTwentyArray));
        
        //Also test the sizes, while we're here
        assertEquals(21,increasingToTwentyArray.size());
    }
    
    /**
     * Test that an empty array behaves badly when you remove or get an element
     */
    @Test
    public void testEmptyArrayOOB(){
        // This uses assertThrows to check that a specific exception is thrown
        // that means we need to pass the code we want to test as a lambda function
        
        // Test removals
        assertThrows(IndexOutOfBoundsException.class,()->emptyArray.remove(0));
        assertThrows(IndexOutOfBoundsException.class,()->emptyArray.remove(1));
        
        // Test gets
        assertThrows(IndexOutOfBoundsException.class,()->emptyArray.get(0));
        assertThrows(IndexOutOfBoundsException.class,()->emptyArray.get(1));
    }
    
    /**
     * Test that OOB get accesses in general break things
     * 
     * Obviously, if the testEmptyArrayOOB fails, these will too. But this
     * tests a more general case, and breaking up the tests is therefore
     * advantageous
     */
    @Test
    public void testOOBGet(){
        for(DoublyLinkedList i : testArraysArray){
            // Test get access after the array ends
            assertThrows(IndexOutOfBoundsException.class,()->i.get(i.size()));
            assertThrows(IndexOutOfBoundsException.class,()->i.get(i.size()+11));
            
            // Test get accesses before array begins
            assertThrows(IndexOutOfBoundsException.class,()->i.get(-1));
            assertThrows(IndexOutOfBoundsException.class,()->i.get(-20));
        }
    }
    
    /**
     * Test that OOB add accesses break things
     * 
     * I am testing OOB seperately from standard usage, because it is
     * a seperate requirement of these methods.  I am testing each
     * method seperately because they are supposed to be distinct methods
     */
    @Test
    public void testOOBAdd(){
        for(DoublyLinkedList i : testArraysArray){
            // Test get access after the array ends
            assertThrows(IndexOutOfBoundsException.class,()->i.add(i.size()+1,1));
            assertThrows(IndexOutOfBoundsException.class,()->i.add(i.size()+12,1));
            
            // Test get accesses before array begins
            assertThrows(IndexOutOfBoundsException.class,()->i.add(-1,1));
            assertThrows(IndexOutOfBoundsException.class,()->i.add(-20,1));
        }
    }
    
    /**
     * Test that OOB remove accesses in general break things
     * 
     * Obviously, if the testEmptyArrayOOB fails, these will too. But this
     * tests a more general case, and breaking up the tests is therefore
     * advantageous
     */
    @Test
    public void testOOBRemove(){
        for(DoublyLinkedList i : testArraysArray){
            // Test get access after the array ends
            assertThrows(IndexOutOfBoundsException.class,()->i.remove(i.size()));
            assertThrows(IndexOutOfBoundsException.class,()->i.remove(i.size()+11));
            
            // Test get accesses before array begins
            assertThrows(IndexOutOfBoundsException.class,()->i.remove(-1));
            assertThrows(IndexOutOfBoundsException.class,()->i.remove(-20));
        }
    }
    
    /**
     * Ensure that clones are equal to their originals, but distinct objects
     */
    @Test
    public void testClone(){
        for (DoublyLinkedList<Integer> i : testArraysArray){
            DoublyLinkedList<Integer> iClone = i.clone();
            // Ensure the backing array and (most) internal variables are the same
            assertEquals(i.toString(),iClone.toString());
            assertEquals(i.size(), iClone.size());
            
            // Ensure that the memory addresses of the clones are different
            // (the clones are different objects).  This gets 'identity hash',
            // which for us is just the hash, but doesnt have to be!  Who knows if
            // the next project brings a HashMap, and if I just reuse this whole thing again
            assertNotEquals(System.identityHashCode(i),System.identityHashCode(iClone));
          
            // now change the old, and see if the clone changes
            
            // first, backup the array, so we can check if it doesnt change (assertArrayNotEquals doesnt exist)
            String iCloneStr = iClone.toString();
            i.add(getSampleValue());
            assertEquals(iCloneStr, iClone.toString());
            assertNotEquals(i.size(), iClone.size());
            assertNotEquals(i.toString(),iClone.toString());
            
            // Reclone, and ensure the changes propagate
            iClone = i.clone();
            assertEquals(i.toString(), iClone.toString());
            assertEquals(i.size(), iClone.size());
        }
    }
    
    /**
     * This examines the sample arrays, and verifies that they are
     * as I expect them to be. It also tests the toString method.
     * 
     * If this test fails, that means the tools I use to test are unreliable.
     */
    @Test
    public void testToStringGeneric(){
        
        // Is empty empty?
        assertEquals("",emptyArray.toString());
        
        // Are strings the same regardless of ordering?
        assertEquals(increasingToTwentyArray.toString().length(), decreasingFromTwentyArray.toString().length());
        
        // Are strings corresponding to longer arrays longer?
        assertTrue(increasingToTwentyArray.toString().length() > increasingToNineArray.toString().length());
        assertTrue(increasingToTwentyArray.toString().length() > halfEmptiedArray.toString().length());
    }
       
    /**
     * Test the next() and reset() methods, together
     */
    @Test
    public void testCounterAndReset(){
        // go through the full list
        for(int i = 0; i <20; i++){
            assertEquals(increasingToTwentyArray.get(i), increasingToTwentyArray.fakePop());
        }
        // We are now OOB: does it notice?
        assertThrows(IndexOutOfBoundsException.class, ()->increasingToTwentyArray.fakePop());
        
        // Reset, and repeat
        increasingToTwentyArray.resetFakeQueue();
        
        for(int i = 0; i <20; i++){
            assertEquals(increasingToTwentyArray.get(i), increasingToTwentyArray.fakePop());
        }
    }
    
    
    /**
     * This is a helper method: it converts a MyArrayList to an ArrayList<Integer>
     * This makes it easier to build the test arrays dynamically,
     * which enables parameterized tests for much greater coverage.
     */
    public ArrayList<Integer> convertToArrayList(DoublyLinkedList<Integer> mine){
        // Create new array list, pre-reserving the memory
        // we want to optimize these tests, but not the class.  because thats how optimization works.
        ArrayList<Integer> standard = new ArrayList<Integer>(mine.size());
        
        // reset the fake queue: tests of that queue shouldn't be creating an
        // ArrayList in the middle of their operation
        mine.resetFakeQueue();
        
        // loop through the array and add each element to the list. 
        for(int i= 0; i< mine.size(); i ++){
            standard.add(mine.fakePop());
        }
        
        
        // reset the counter: because otherwise, calling this twice makes things go BOOM
        mine.resetFakeQueue();
        
        return standard;
    }
}
