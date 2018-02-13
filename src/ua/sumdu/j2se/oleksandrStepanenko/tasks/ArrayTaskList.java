package ua.sumdu.j2se.oleksandrStepanenko.tasks;

import java.util.Arrays;
import java.util.Iterator;

public class ArrayTaskList extends TaskList {

    protected Task[] arrayTaskList = new Task[1];


    /* конструктори */
    public ArrayTaskList(int len) {
        this.arrayTaskList = new Task[len];
    }

    public ArrayTaskList() {
    }

    //метод, що додає до списку вказану задачу.
    @Override
    public void add(Task task) throws IllegalStateException {
        taskNotNullCheck(task);
        if (size <= arrayTaskList.length - 1) {
            arrayTaskList[size++] = task;
        } else {
            arrayTaskList = Arrays.copyOf(arrayTaskList, arrayTaskList.length + 10);
            arrayTaskList[size++] = task;
        }
    }

    /*метод, що видаляє задачу із списку і повертає істину, якщо така задача була у списку. Якщо у списку було декілька
     таких задач, необхідно видалити одну будь-яку.*/
    @Override
    public boolean remove(Task task) throws IllegalStateException {
        taskNotNullCheck(task);
        for (int i = 0; i < size; i++) {
            if (arrayTaskList[i].equals(task)) {
                for (int j = i; j < size - 1; j++) {
                    System.out.println("TURNING \'" + arrayTaskList[j].getTitle() + "\' -> INTO -> \'" + arrayTaskList[j + 1].getTitle() + "\'");
                    arrayTaskList[j] = arrayTaskList[j + 1];
                }
                arrayTaskList[--size] = null;
                return true;
            }
        }
        System.out.println("Task " + task.getTitle() + " відсутня в ArrayTaskList.");
        return false;
    }

    //метод, що повертає кількість задач у списку.
    @Override
    public int size() {
        return size;
    }

    //метод, що повертає задачу по індексу
    @Override
    public Task getTask(int index) throws IndexOutOfBoundsException {
        getTaskCHECK(index);
        return arrayTaskList[index];
    }

    /* check for task list */
    @Override
    public void getTaskCHECK(int index) throws IndexOutOfBoundsException {
        if (index > arrayTaskList.length)
            throw new IndexOutOfBoundsException("Task in " + index + " cell is out of Array bounds. Current Array size is " + arrayTaskList.length + ".");
        if (arrayTaskList[index] == null)
            throw new IndexOutOfBoundsException("Task in " + index + " cell is null");
    }

    /*Ітератор*/
    @Override
    public Iterator<Task> iterator() {
        return new Iterator<Task>() {
            int current = 0;

            @Override
            public boolean hasNext() {
                return size() > current;
            }

            @Override
            public Task next() {
                if (!hasNext()) throw new IllegalStateException	("Не вдається знайти наступну Task.");
                return getTask(current++);
            }

            @Override
            public void remove() {
                if(current == 0) throw new IllegalStateException("TaskList порожній. Нічого не можна видалити.");
                ArrayTaskList.this.remove(arrayTaskList[--current]);
            }
        };
    }
}
