package org.choidh.toby_project.calc;

public interface LineCallback<T> {
    T doSomethingWithLine(String line, T value);
}
