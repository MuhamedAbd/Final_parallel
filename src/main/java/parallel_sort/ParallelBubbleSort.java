package parallel_sort;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ForkJoinPool;

public class ParallelBubbleSort {
    private static final int THRESHOLD = 100;

    public static void sort(int[] arr) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        forkJoinPool.invoke(new BubbleSortTask(arr, 0, arr.length - 1));
    }

    private static class BubbleSortTask extends RecursiveAction {
        private int[] arr;
        private int left, right;

        public BubbleSortTask(int[] arr, int left, int right) {
            this.arr = arr;
            this.left = left;
            this.right = right;
        }

        @Override
        protected void compute() {
            if (right - left < THRESHOLD) {
                // Sorting sequentially
                for (int i = left; i <= right; i++) {
                    for (int j = left; j < right - (i - left); j++) {
                        if (arr[j] > arr[j + 1]) {
                            int temp = arr[j];
                            arr[j] = arr[j + 1];
                            arr[j + 1] = temp;
                        }
                    }
                }
            } else {
                int mid = (left + right) / 2;
                BubbleSortTask leftTask = new BubbleSortTask(arr, left, mid);
                BubbleSortTask rightTask = new BubbleSortTask(arr, mid + 1, right);
                invokeAll(leftTask, rightTask);
                merge(arr, left, mid, right);
            }
        }

        private void merge(int[] arr, int left, int mid, int right) {
            int[] temp = new int[right - left + 1];
            int i = left, j = mid + 1, k = 0;
            while (i <= mid && j <= right) {
                if (arr[i] <= arr[j]) {
                    temp[k++] = arr[i++];
                } else {
                    temp[k++] = arr[j++];
                }
            }
            while (i <= mid) temp[k++] = arr[i++];
            while (j <= right) temp[k++] = arr[j++];
            System.arraycopy(temp, 0, arr, left, temp.length);
        }
    }
}
