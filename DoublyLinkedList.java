import java.util.function.Function;

/**
 * A standard-issue DoublyLinkedList.
 * This class is moderately optimized for runtime speed: it will access from
 * head or tail depending on which is closest.
 *
 * @author Calum McConnell
 * @version 0.0.1
 */
public class DoublyLinkedList<E>
{
    private Node<E> head;
    private Node<E> tail;
    private Node<E> curr;
    private int len = 0;
    // indicates whether we are locked; has nothing to do with GC
    private boolean finalized = false;
    
    /**
     * This is an internal method that fetches Node objects.
     * It is used for other list manipulations, and will work optimally.
     * (iterating from head or tail based on the index provided)
     * <br>
     * Does not validate input: prone to throwing null pointer
     * exceptions if input is bad, but is not guarenteed to
     * 
     * @param index the index of the node to fetch
     * @return the node object at that index
     * @throws IndexOutOfBoundsException when index is too large or too small
     */
    private Node<E> getNodeAtDex(int index){
        Node<E> retval;
        
        if(index >= len || index < 0){
            throw new IndexOutOfBoundsException(
                String.format("Index %d out of bounds for list length %d", index, len));
        }
        
        if(len/2 - index > 0){
            // go forwards: we're close to the start
            retval = head;
            for(int i = 0; i < index; i++){
                retval = retval.next;
            }
        }
        else{
            // go backwards
            retval = tail;
            for(int i = len-1; i > index; i--){
                retval = retval.prev;
            }
        }
        return retval;
    }
    
    /**
     * Retreives the element at index N.
     * 
     * @param index the index of the element to be retreived
     * @return the Nth element of the list
     */
    public E get(int index){
        return getNodeAtDex(index).data;
    }
    
    /**
     * Removes the element at index n, causing the elements at either
     * side to be adjusted to match it.
     * 
     * @param index the index of the element to be removed
     */
    public void remove(int index){
        // no modification to finalized arrays
        if(finalized){
            throw new IllegalStateException("This list has been locked: modifications are impossible");
        }
        
        Node<E> target = getNodeAtDex(index);  
        // check if we're at the front
        if(target.prev != null){
            target.prev.next = target.next;
        }
        else{
            // change head
            head = target.next;
        }
        
        // check if we're at the end
        if(target.next != null){
            target.next.prev = target.prev;
        }
        else{
            tail = target.prev;
        }
        
        // check if the curr pointer is to the dropped element
        if(curr == target){
            curr = target.next;
        }
        
        // decrement length
        len --;
    }    
    
    /**
     * Adds an element to the end of the list 
     * @param data the value of the element to add
     */
    public void add(E data){
        add(len, data);
    }
    
    /**
     * Adds an element at the provided index, such that a
     * subsequent get(index) will return the value provided.
     * This will move other elements to suit.
     * 
     * @param index the index at which the element is too be placed
     * @param data the data which is within the element
     */
    public void add(int index, E data){
        if(finalized){
            throw new IllegalStateException("This list has been locked: modifications are impossible");
        }
        
        // make the new node
        Node<E> newNode = new Node<E>();
        newNode.data = data;
        
        if(index == 0){
            newNode.next= head;
            
            // handle empty list case, with some nice symetry
            // an if-and-if (as opposed to if-else-if) would be nice, but is niche
            if(index==len){
                tail = newNode;
                // also init curr pointer
                curr = newNode;
            }
            else{
                head.prev = newNode;
            }
            head = newNode;
        }
        else if(index == len){
            newNode.prev=tail;
            tail.next = newNode;
            tail = newNode;
        }
        else{
            // insert into middle: this function call also does the bounds check
            Node<E> target = getNodeAtDex(index);
            
            // set up our new node
            newNode.prev = target.prev;
            newNode.next = target;
            // and integrate it
            target.prev.next = newNode;
            target.prev = newNode;
        }
        
        // increment len, and pat yourself on the back.
        len++;
        
    }
    
    /**
     * Gets the number of elements in this list.
     * 
     * @return the number of elements in this list: 0 if empty
     */
    public int size(){
        return len;
    }
    
    /**
     * This is a "fake" queue: it doesn't modify the underlying
     * list at all.  It is used for repeated iteration, or other
     * cases where you want to go through the structure without needing
     * to clone it and save a copy, or repeatedly pay the O(N) cost of
     * seeking.
     * @return the element at the current 'fake head' of the queue
     */
    public E fakePop(){
        // Throw an IndexOutOfBounds error (as opposed to a NPE)
        if(curr == null){
            throw new IndexOutOfBoundsException("fake pop went too far this time!");
        }
        E retval = curr.data;
        curr = curr.next;
        return retval;
    }
    
    /**
     * Resets the "fake" queue to the start
     */
    public void resetFakeQueue(){
        curr = head;
    }
    
    /**
     * This method is a bit ugly: but it's needed to efficently do routings
     * other than "go down the list, stopping at each pickup then each destination"
     * <p>
     * It applies the lambda function to each member of this list one at a time:
     * each result is placed in a new list, which is returned.  For instance, 
     * if this list were to contain ShipmentOrders and the provided function
     * calculated the distance between the current position and the pickup, then
     * the returned value would be each of the doubles that resulted from that
     * calculation
     * @param function The function to apply to each of the List members
     * @return A list of the results, in the same order as this list
     */
    public <G> DoublyLinkedList<G> applyFunctionToList(Function<E,G> function){
        // lets just write this out now
        Node<E> cur = head;
        DoublyLinkedList<G> results = new DoublyLinkedList<G>();
        while(cur != null){
            G result = function.apply(cur.data);
            results.add(result);
            cur = cur.next;
        }
        return results;
    }
    
    /**
     * This overrides the toString method, to provide a string representation
     * of the entire dataset.
     */
    public String toString(){
        String retval = "";
        // iterate over the list again
        // it'd be neat if we could use the applyFunctionToList shenanigans, but
        // java lambdas don't work that way (for good reason)
        Node<E> cur = head;
        while(cur != null){
            retval += cur.data.toString();
            retval += ", ";
            cur = cur.next;
        }
        return retval;
    }
    
    /**
     * Locks the list in it's current state.  This makes any future
     * add() or remove() operations an exception.  The elements can still
     * be modified, however.
     * <p>
     * There is no unlock() operation. This is intentional.  Its not
     * much of a lock if anyone can open it
     */
    public void lock(){
        finalized = true;
    }
    
    /**
     * States whether the list is currently locked, or if it can still
     * be modified
     * @return true if the list is unable to be modified, false otherwise
     */
    public boolean isLocked(){
        return finalized;
    }
    
    /**
     * States whether or not the list is empty
     * @return true if there are no elements: false otherwise
     */
    public boolean isEmpty(){
        return len==0;
    }
    
    /**
     * Empties out the list
     */
    public void clear(){
        // set len=0, drop head, tail, and curr references.
        len = 0;
        head = null;
        curr = null;
        tail = null;
    }
    
    /**
     * Appends another list onto the end of this one:
     * it is not a deep copy, but it is very fast
     * @param other another list, with a compatable type, which will be
     * grafted onto this one.  Must not be empty.
     */
    public void append(DoublyLinkedList<E> other){
        // switch to a clone of other, to avoid breakage
        other = other.clone();
        
        // change tail to point at the other's head as the next element
        // and vice versa for the other head
        tail.next = other.head;
        other.head.next = tail;
        
        // now move our tail to the end of the combined list, and increase len
        tail = other.tail;
        len += other.len;
        
    }
    
    /**
     * Clones the object.  This also clears the isLocked flag, which
     * may be useful.  Note that contained elements are not cloned:
     * this is a shallow cloning operation
     * 
     * @return a shallow clone of this list
     */
    public DoublyLinkedList<E> clone(){
        DoublyLinkedList<E> retval = new DoublyLinkedList<E>();
        // if our list is empty, return this as-is
        if(len == 0){
            return retval;
        }
        
        // we can't just copy over the head/tail refs, that'd be too easy
        // iterate through, duplicating each element and tacking it on
        Node<E> nextOrig = head;
        Node<E> tmp = new Node<E>();
        tmp.data = head.data;
        retval.head = tmp;
        Node<E> prevClone = tmp;
        while(nextOrig.next != null){
            // grab next element
            nextOrig = nextOrig.next;
            prevClone = tmp;
            
            // init our newest node
            tmp = new Node<E>();
            tmp.data = nextOrig.data;
            tmp.prev = prevClone;
            
            // connect it in, and prp for next loop.
            prevClone.next = tmp;
            
        }
        retval.tail = tmp;
        // set up some auxilary variables
        retval.len = len;
        retval.curr = retval.head;
        return retval;
    }
    
    private class Node<E>{
        E data;
        Node<E> prev;
        Node<E> next;
    }
}
