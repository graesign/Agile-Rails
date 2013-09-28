package helpers;

import java.util.Stack;
/**
 * this class is a helper class to do special things with Array's
 * @author RailCab07_4
 */
public class Array {
    /**
     * This method simulates the php in_array function (http://www.php.net/in_array),
     * @param needle - Key to look for
     * @param haystack - Array to look in
     * @return boolean - Is the needle in the haystack?
     */
    public static final synchronized <Type> boolean in_array(Type needle, Type[] haystack)
    {
        for (Type key : haystack)
        {
            if (needle.equals (key))
            {
                return true;
            }
        }
        return false;
    }
    /**
     * Remove a specified key from a specified array
     * @param haystack - The array to search in
     * @param needle - The value to remove
     * @return T[] - Updated array
     */
    public static final synchronized <Type> Type[] array_unset(Type needle, Type[] haystack)
    {
        Stack<Type> tempArray  = new Stack<Type>();
        Type tempValue;
        
        if (in_array (needle, haystack) == false)
        {
            return haystack;
        }
        
        for (Type key : haystack)
        {
            tempValue = key;
            
            if (tempValue == null)
            {
                break;
            }
            
            if (tempValue.equals (needle))
            {
                continue;
            }
            
            tempArray.push (tempValue);
        }
        // Return the array in the correct type.
        return tempArray.toArray (haystack);
    }
}
