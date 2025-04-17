package parallel_sort;

import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinPool;

public class ParallelQuickSort {
    public static void sort(int[] arr) {
        ForkJoinPool forkJoinPool = new ForkJoinPool(6);
        forkJoinPool.invoke(new QuickSortTask(arr, 0, arr.length - 1));
    }

    private static class QuickSortTask extends RecursiveTask<Void> {
        private int[] arr;
        private int low, high;

        public QuickSortTask(int[] arr, int low, int high) {
            this.arr = arr;
            this.low = low;
            this.high = high;
        }

        @Override
        protected Void compute() {
            if (low < high) {
                int pivotIndex = partition(arr, low, high);
                QuickSortTask leftTask = new QuickSortTask(arr, low, pivotIndex - 1);
                QuickSortTask rightTask = new QuickSortTask(arr, pivotIndex + 1, high);

                leftTask.fork();
                rightTask.fork();

                leftTask.join();
                rightTask.join();
            }
            return null;
        }

        private int partition(int[] arr, int low, int high) {
            int pivot = arr[high];
            int i = low - 1;
            for (int j = low; j < high; j++) {
                if (arr[j] <= pivot) {
                    i++;
                    int temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
            }
            int temp = arr[i + 1];
            arr[i + 1] = arr[high];
            arr[high] = temp;
            return i + 1;
        }
    }
}