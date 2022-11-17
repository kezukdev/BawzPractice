package kezuk.practice.utils;

import java.util.Arrays;
import java.util.List;

public class MathUtils {
	
    public static int[] removeElementUsingCollection(int[] arr, int index ){
        List<int[]> tempList = Arrays.asList(arr);
        tempList.remove(index);
        return tempList.toArray();
    }

}
