import java.util.Arrays;

class Solution {
    public int numRescueBoats(int[] people, int limit) {
        Arrays.sort(people);
        int boat = 0;
        int left = 0;
        int right = people.length - 1;
        while (left <= right) {
            if (people[left] + people[right] <= limit) {
                left++;
            }
            right--;
            boat++;
        }
        return boat;
    }
}

public class Main {
    public static void main(String[] args) {
        int[] people = {3, 2, 2, 1};
        int limit = 3;
        Solution solution = new Solution();
        System.out.println(solution.numRescueBoats(people, limit));
    }
}