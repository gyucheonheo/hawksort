package cs553.team13.InternalSort;

import java.util.ArrayList;
import java.util.List;

public class MergeSort <String extends Comparable<String>>{
    /* Reference : https://leetcode.com/problems/sort-an-array/discuss/329672/merge-sort */
    
    public void sort(List<String> values){
        mergeSort(0, values.size() - 1, values, new ArrayList<>(values));
    }

        private void mergeSort(int low, int high, List<String> target, List<String> destination) {
            if(low < high){
                int mid = low + (high - low) / 2;
                mergeSort(low, mid, target, destination);
                mergeSort(mid+1, high, target, destination);
                merge(low, mid, high, target, destination);
            }
        }

        private void merge(int low, int mid, int high, List<String> target, List<String> destination) {

            int left = low;
            int right = mid + 1;

            for(int i = low; i <= high; i++){
                destination.set(i, target.get(i));
            }

            while(left <= mid && right <= high){
                if(destination.get(left).compareTo(destination.get(right)) < 0){
                    target.set(low, destination.get(left));
                    left++;
                } else {
                    target.set(low, destination.get(right));
                    right++;
                }
                low++;
            }

            while(left <= mid){
                target.set(low, destination.get(left));
                low++;
                left++;
            }
        }
}
