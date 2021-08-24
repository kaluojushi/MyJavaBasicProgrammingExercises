import java.util.Scanner;

public class Main {
    public static int N;
    public static int[] numbers = new int[35];
    public static int interval = 0;

    public static String printItem(int[] array, int pos) {
        String result = N + "=";
        for (int i = 0; i <= pos; i++) {
            result += array[i];
            if (i < pos) result += "+";
        }
        if (interval % 4 == 0 || pos == 0) result += "\n";
        else result += ";";
        return result;
    }

    public static void dfs(int pos, int sum, int next) {
        if (sum + next > N) return;
        else {
            numbers[pos] = next;
            if (sum + next == N) {
                interval++;
                System.out.print(printItem(numbers, pos));
            } else if (sum + next < N) {
                sum += next;
                pos++;
                for (int i = next; i <= N - sum; i++) {
                    dfs(pos, sum, i);
                }
            }
        }
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        N = in.nextInt();
        for (int i = 1; i <= N / 2; i++) {
            dfs(0, 0, i);
        }
        dfs(0, 0, N);
    }
}