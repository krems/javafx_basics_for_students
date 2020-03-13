package sample;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;

import java.math.BigInteger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class Controller {

    private final ExecutorService executor = Executors.newFixedThreadPool(2);
    @FXML
    private TextField factorialNumber;
    @FXML
    private ProgressBar progressBar;

    public void print() {
        System.out.println("Привет");
    }

    public void factorial() {
        String number = factorialNumber.getText();
        if (number == null || number.isBlank()) {
            return;
        }
        Task<BigInteger> task = new Task<>() {
            @Override
            protected BigInteger call() throws Exception {
                int num = Integer.parseInt(number);

                BigInteger factorial = factorial(num, p -> updateProgress(p, 1d));
                return factorial;
            }
        };
        executor.submit(task);
        progressBar.progressProperty().bind(task.progressProperty());
        task.setOnSucceeded(e -> {
            System.out.println(e.getSource().getValue());
        });
    }

    private static BigInteger factorial(int num, Consumer<Double> progress) {
        return factorial(BigInteger.valueOf(num), progress);
    }

    private static BigInteger factorial(BigInteger num, Consumer<Double> progress) {
        BigInteger initial = num;
        BigInteger current = BigInteger.valueOf(1);
        while (!num.equals(BigInteger.ONE)) {
            current = current.multiply(num);
            num = num.subtract(BigInteger.ONE);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            progress.accept(initial.subtract(current).divide(initial).doubleValue());
        }
        progress.accept(1d);
        return current;
    }
}
