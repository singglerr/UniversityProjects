package sample;
import sample.Interfaces.Observable;
import sample.Interfaces.Observer;
import sample.Interfaces.Saveable;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MyStorage<T extends Saveable> implements Observable{
    private List<Observer> observers = new ArrayList<>();

    static private class Node<T>{
        private T data;
        private Node<T> next;
        private Node<T> previous;

        Node(T data, Node<T> previous, Node<T> next){
            this.data = data;
            this.previous = previous;
            this.next = next;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

        public Node<T> getNext() {
            return next;
        }

        public void setNext(Node<T> next) {
            this.next = next;
        }

        public Node<T> getPrevious() {
            return previous;
        }

        public void setPrevious(Node<T> previous) {
            this.previous = previous;
        }
    }

    private Node<T> first, end;
    private Node<T> current;
    private int size = 0;

    public MyStorage(){
    }

    public void addEnd(T el){
        Node<T> newNode = new Node<>(el,null,null);
        size++;
        if (first == null){
            first = newNode;
            end = first;
            current = first;
        }
        else{
            end.setNext(newNode);
            newNode.setPrevious(end);
            end = newNode;
            current = newNode;
        }
        notifyObserver();
    }


    public void addAfterCurrent(T el){
        if (current == end || current == null){
            addEnd(el);
            return;
        }
        Node<T> newNode = new Node<>(el, current, current.getNext());
        current.getNext().setPrevious(newNode);
        current.setNext(newNode);
        current = newNode;
        notifyObserver();
    }

    public void addTop(T el){
        Node<T> newNode = new Node<>(el, null, first);
        if (first == null){
            first = newNode;
            end = newNode;
            current = newNode;
        }
        else {
            first.setPrevious(newNode);
            first = newNode;
            current = newNode;
        }
        notifyObserver();
    }

    public void addBeforeCurrent(T el){
        if (current == first || current == null){
            addTop(el);
            return;
        }
        Node<T> newNode = new Node<>(el, current.getPrevious(), current);
        current.getPrevious().setNext(newNode);
        current.setPrevious(newNode);
        current = newNode;
        notifyObserver();
    }

    public T remove(T el){
        if (el == null)
            return null;
        Node<T> oldCurrent = current;
        for (first(); !isEOL(); next()){
            if (current.getData().equals(el)){
                T res = current.getData();
                if (current == first) {
                    if (size > 1) {
                        current.getNext().setPrevious(null);
                        first = current.getNext();
                        if (current == oldCurrent)
                            current = first;
                        else current = oldCurrent;
                    }
                    else {
                        first = null;
                        current = null;
                        end = null;
                    }
                }
                else if (current == end){
                    current.getPrevious().setNext(null);
                    end = current.getPrevious();
                    if (current == oldCurrent)
                        current = end;
                    else current = oldCurrent;
                }
                else {
                    current.getPrevious().setNext(current.getNext());
                    current.getNext().setPrevious(current.getPrevious());
                    if (current == oldCurrent)
                        current = current.getPrevious();
                    else current = current.getPrevious();
                }
                size--;
                notifyObserver();
                return res;
            }
        }
        return  null;
    }

    public T remove(int index){
        int i = 1;
        if (index > size){
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        Node<T> oldCurrent = current;
        for (first(); i < index; i++, next());
        T res = current.getData();
        if (index == 1){
            if (size > 1) {
                current.getNext().setPrevious(null);
                first = current.getNext();
                if (oldCurrent == current)
                    current = first;
                else current = oldCurrent;
            }
            else {
                first = null;
                current = null;
                end = null;
            }
        }
        else if (index == size){
            current.getPrevious().setNext(null);
            end = current.getPrevious();
            if (oldCurrent == current)
                current = end;
            else current = oldCurrent;
        }
        else {
            current.getPrevious().setNext(current.getNext());
            current.getNext().setPrevious(current.getPrevious());
            if (oldCurrent == current)
                current = current.getPrevious();
            else current = oldCurrent;
        }
        size--;
        notifyObserver();
        return res;
    }

    public void removeAll(){
        for (first(); !isEOL(); next()){
            current.setData(null);
            if (current.getPrevious() != null){
                current.getPrevious().setNext(null);
            }
            current.setPrevious(null);
        }
        first = null;
        current = null;
        end = null;
        size = 0;
        notifyObserver();
    }

    public T getObject(){
        if (current != null)
            return current.getData();
        else
            return null;
    }

    public boolean contains(T el){
        Node<T> oldCurrent = current;
        for (first(); !isEOL(); next()){
            if (current.getData().equals(el)){
                current = oldCurrent;
                return true;
            }
        }
        current = oldCurrent;
        return false;
    }

    public void next(){
        if (!isEOL()){
            current = current.getNext();
        }
    }

    public void previous(){
        if (!isBOF()){
            current = current.getPrevious();
        }
    }

    public void end(){
        current = end;
    }

    public void setObject(T el){
        current.setData(el);
    }

    //Check if we are at the end of the list(storage)
    public boolean isEOL(){
        if (current == null){
            current = end;
            return true;
        }
        return false;
    }

    public void first(){
        current = first;
    }

    //Check if we are at the top of the list(storage)
    public boolean isBOF(){
        if (current == null){
            current = first;
            return true;
        }
        return  false;
    }

    public int getSize() {
        return size;
    }

    @Override
    public String toString() {
        Node<T> oldCurrent = current;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[ ");
        for (first(); !isEOL(); next()){
            if (current.getData() != null){
                StringBuilder s = new StringBuilder("    " + current.getData().toString() + "\n");
                if (current == oldCurrent){
                    s.replace(2,4, "->");
                }
                if (current == first){
                    s.delete(0,2);
                }
                stringBuilder.append(s);
            }
        }
        current = oldCurrent;
        stringBuilder.deleteCharAt(stringBuilder.length()-1);
        stringBuilder.append(" ]");
        return stringBuilder.toString();
    }

    public void saveToFile(File file){
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            Node<T> oldCurrent = current;
            for (first(); !isEOL(); next()){
                current.getData().save(writer);
            }
            current = oldCurrent;
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removerObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObserver() {
        for (Observer o : observers){
            o.handleEvent();
        }
    }

    public List<T> toList(){
        List<T> list = new LinkedList<>();
        for (first(); !isEOL(); next()){
            list.add(current.data);
        }
        return list;
    }
}
