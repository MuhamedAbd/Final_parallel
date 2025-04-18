package parallel_sort;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Scanner;

public class SortingGUI extends JFrame {
    private JTextArea textArea;
    private JComboBox<String> algorithmSelector;
    private JRadioButton sequentialButton, parallelButton;
    private JButton uploadButton, sortButton;
    private File selectedFile;
    private int[] numbers;

    public SortingGUI() {
        setTitle("Sorting Algorithms GUI");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Text Area
        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        // Top Panel (Upload and Sort)
        JPanel topPanel = new JPanel();
        uploadButton = new JButton("Upload File");
        sortButton = new JButton("Sort");
        topPanel.add(uploadButton);
        topPanel.add(sortButton);
        add(topPanel, BorderLayout.NORTH);

        // Bottom Panel (Options)
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(2, 1));

        // Algorithm Selection Panel
        JPanel algoPanel = new JPanel();
        algorithmSelector = new JComboBox<>(new String[] {
            "Merge Sort", "Quick Sort"  // Removed "Bubble Sort"
        });
        algoPanel.add(new JLabel("Select Algorithm:"));
        algoPanel.add(algorithmSelector);

        // Mode Selection Panel (Sequential vs Parallel)
        JPanel modePanel = new JPanel();
        sequentialButton = new JRadioButton("Sequential", true);
        parallelButton = new JRadioButton("Parallel");
        ButtonGroup group = new ButtonGroup();
        group.add(sequentialButton);
        group.add(parallelButton);
        modePanel.add(sequentialButton);
        modePanel.add(parallelButton);

        bottomPanel.add(algoPanel);
        bottomPanel.add(modePanel);
        add(bottomPanel, BorderLayout.SOUTH);

        // Listeners
        uploadButton.addActionListener(e -> uploadFile());
        sortButton.addActionListener(e -> sortFile());

        setVisible(true);
    }

    private void uploadFile() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            try (Scanner scanner = new Scanner(selectedFile)) {
                StringBuilder sb = new StringBuilder();
                String[] tokens = scanner.nextLine().split(",");
                numbers = new int[tokens.length];
                for (int i = 0; i < tokens.length; i++) {
                    numbers[i] = Integer.parseInt(tokens[i].trim());
                }
                sb.append("File Loaded: \n");
                sb.append(java.util.Arrays.toString(numbers));
                textArea.setText(sb.toString());
            } catch (Exception ex) {
                textArea.setText("Error reading file: " + ex.getMessage());
            }
        }
    }

    private void sortFile() {
        if (numbers == null) {
            textArea.setText("Please upload a file first.");
            return;
        }
        int[] arrToSort = java.util.Arrays.copyOf(numbers, numbers.length);
        String algo = (String) algorithmSelector.getSelectedItem();
        boolean isParallel = parallelButton.isSelected();
        long startTime = System.currentTimeMillis();

        // Choose algorithm to sort based on selected options
        switch (algo) {
            case "Merge Sort":
                if (isParallel)
                    ParallelMergeSort.sort(arrToSort); // Call Parallel Merge Sort
                else
                    SequentialMergeSort.sort(arrToSort); // Call Sequential Merge Sort
                break;
            case "Quick Sort":
                if (isParallel)
                    ParallelQuickSort.sort(arrToSort); // Call Parallel Quick Sort
                else
                    SequentialQuickSort.sort(arrToSort); // Call Sequential Quick Sort
                break;
        }

        long endTime = System.currentTimeMillis();
        textArea.setText("Sorted Result:\n");
        textArea.append(java.util.Arrays.toString(arrToSort) + "\n");
        textArea.append("Time Taken: " + (endTime - startTime) + " ms");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SortingGUI::new);
    }
}
