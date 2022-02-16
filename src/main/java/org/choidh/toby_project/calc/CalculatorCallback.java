package org.choidh.toby_project.calc;

import java.io.BufferedReader;
import java.io.IOException;

public interface CalculatorCallback {
    Integer doSomethingWithReader(BufferedReader reader) throws IOException;
}
