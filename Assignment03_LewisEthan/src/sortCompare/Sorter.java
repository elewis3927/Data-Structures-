package sortCompare;
import java.util.*;

/**
 * An interface for sorting algorithms
 * 
 * @author dave
 * @date 2/7/2009
 */
interface Sorter<E extends Comparable<E>>{
	public void sort(ArrayList<E> data);
}
