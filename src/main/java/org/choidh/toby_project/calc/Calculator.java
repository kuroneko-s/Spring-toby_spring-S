package org.choidh.toby_project.calc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {

    public int fileReadTemplate(String path, CalculatorCallback callback) throws IOException {
        try (
                BufferedReader reader = new BufferedReader(new FileReader(path))
        ) {
            return callback.doSomethingWithReader(reader);
        } catch (IOException err) {
            System.out.println(err.getMessage());
            throw err;
        }
    }

    public int calcSum(String filePath) throws IOException {
        CalculatorCallback callback = reader -> {
            int sum = 0;
            String line = "";
            while ((line = reader.readLine()) != null) sum += Integer.valueOf(line);
            return sum;
        };

        return this.fileReadTemplate(filePath, callback);
    }

    public int calsMultiply(String filePath) throws IOException {
        CalculatorCallback callback = reader -> {
            int sum = 1;
            String line = "";
            while ((line = reader.readLine()) != null) sum *= Integer.valueOf(line);
            return sum;
        };

        return this.fileReadTemplate(filePath, callback);
    }

    public <T> T lineReadTemplate(String filePath, LineCallback<T> callback, T initVal) throws IOException {
        try (
                BufferedReader reader = new BufferedReader(new FileReader(filePath))
        ) {
            T result = initVal;
            String line = "";
            while ((line = reader.readLine()) != null) result = callback.doSomethingWithLine(line, result);

            return result;
        } catch (IOException err) {
            System.out.println(err.getMessage());
            throw err;
        }
    }

    public String concatenate(String filePath) throws IOException {
        return lineReadTemplate(filePath, (line, value) -> value + line, "");
    }
}
