package ca.uottawa.csi5318.util;

public class FibonacciSequence {
    public static int get(int n) {
        if (n == 0) return 0;
        if (n == 1) return 1;
        return get(n - 1) + get(n - 2);
    }
}
