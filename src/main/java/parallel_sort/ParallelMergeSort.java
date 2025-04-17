package parallel_sort;

import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinPool;

public class ParallelMergeSort {
    public static void sort(int[] arr) {
        ForkJoinPool forkJoinPool = new ForkJoinPool(6);
        forkJoinPool.invoke(new MergeSortTask(arr, 0, arr.length - 1));
    }

    private static class MergeSortTask extends RecursiveTask<Void> {
        private int[] arr;
        private int left, right;

        public MergeSortTask(int[] arr, int left, int right) {
            this.arr = arr;
            this.left = left;
            this.right = right;
        }

        @Override
        protected Void compute() {
            if (right - left < 2) {
                if (arr[left] > arr[right]) {
                    int temp = arr[left];
                    arr[left] = arr[right];
                    arr[right] = temp;
                }
                return null;
            }
            int middle = (left + right) / 2;
            MergeSortTask leftTask = new MergeSortTask(arr, left, middle);
            MergeSortTask rightTask = new MergeSortTask(arr, middle + 1, right);

            leftTask.fork();
            rightTask.fork();

            leftTask.join();
            rightTask.join();

            merge(arr, left, middle, right);
            return null;
        }

        private void merge(int[] arr, int left, int middle, int right) {
            int[] temp = new int[right - left + 1];
            int i = left, j = middle + 1, k = 0;

            while (i <= middle && j <= right) {
                if (arr[i] < arr[j]) {
                    temp[k++] = arr[i++];
                } else {
                    temp[k++] = arr[j++];
                }
            }
            while (i <= middle) {
                temp[k++] = arr[i++];
            }
            while (j <= right) {
                temp[k++] = arr[j++];
            }
            System.arraycopy(temp, 0, arr, left, temp.length);
        }
    }
}