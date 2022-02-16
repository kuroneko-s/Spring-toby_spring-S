package org.choidh.toby_project;

import org.choidh.toby_project.calc.Calculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalcSumTest {
    Calculator calculator;
    final String filePath = "C:\\project\\workspace\\toby_project\\numbers.txt";

    @BeforeEach
    public void setUp() {
        calculator = new Calculator();
    }

    @Test
    public void calcSumTest() throws Exception {
        assertEquals(calculator.calcSum(filePath), 10);
    }

    @Test
    public void calcMultiTest() throws Exception {
        assertEquals(calculator.calsMultiply(filePath), 24);
    }

    @Test
    public void calcLineTemplateSumTest() throws Exception {
        assertEquals(calculator.lineReadTemplate(filePath, (line, value) -> value + Integer.valueOf(line), 0), 10);
    }

    @Test
    public void calcLineTemplateMultiplyTest() throws Exception {
        assertEquals(calculator.lineReadTemplate(filePath, (line, value) -> value * Integer.valueOf(line), 1), 24);
    }

    @Test
    public void concatenateTest() throws Exception {
        System.out.println(
                calculator.concatenate(filePath)
        );
    }
}
