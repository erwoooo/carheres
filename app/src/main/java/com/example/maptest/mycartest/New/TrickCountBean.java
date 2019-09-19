package com.example.maptest.mycartest.New;

import java.util.Arrays;

/**
 * Created by ${Author} on 2018/3/17.
 * Use to
 */

public class TrickCountBean {
    private String content[];
    private boolean first;
    private boolean last;
    private int number;
    private int numberOfElements;
    private int size;
    private int totalElements;
    private int totalPages;

    @Override
    public String toString() {
        return "TrickCountBean{" +
                "content=" + Arrays.toString(content) +
                ", first=" + first +
                ", last=" + last +
                ", number=" + number +
                ", numberOfElements=" + numberOfElements +
                ", size=" + size +
                ", totalElements=" + totalElements +
                ", totalPages=" + totalPages +
                '}';
    }

    public String[] getContent() {
        return content;
    }

    public void setContent(String[] content) {
        this.content = content;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public TrickCountBean() {
    }

    public TrickCountBean(String[] content, boolean first, boolean last, int number, int numberOfElements, int size, int totalElements, int totalPages) {
        this.content = content;
        this.first = first;
        this.last = last;
        this.number = number;
        this.numberOfElements = numberOfElements;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }
}
