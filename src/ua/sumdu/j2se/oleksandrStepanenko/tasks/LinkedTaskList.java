package ua.sumdu.j2se.oleksandrStepanenko.tasks;

import java.util.Iterator;

public class LinkedTaskList extends TaskList {


    private InnerTask first;
    private InnerTask last;
    private int size;

    // Конструктор
    public LinkedTaskList() {
    }

    // внутрішній клас
        private class InnerTask {

        private Task currTask;
        private InnerTask prevTask;
        private InnerTask nextTask;

        /*Конструктори*/
        private InnerTask(Task task) {
            this.currTask = task;
            this.prevTask = null;
            this.nextTask = null;
        }

        private InnerTask() {
        }
    }

    //метод, що додає до списку вказану задачу.
    @Override
    public void add(Task task) throws IllegalStateException {
        taskNotNullCheck(task);
        InnerTask taskToAdd = new InnerTask(task);
        if (size == 0) {
            first = taskToAdd;
            last = first;
            size++;
        } else {
            last.nextTask = taskToAdd;
            taskToAdd.prevTask = last;
            last = taskToAdd;
            size++;
        }
    }

     /*метод, що видаляє задачу із списку і повертає істину, якщо така задача була у списку. Якщо у списку було декілька
     таких задач, необхідно видалити одну будь-яку.*/

    @Override
    public boolean remove(Task task) throws IllegalStateException {
        taskNotNullCheck(task);
        InnerTask innTaskSearch;
        innTaskSearch = first;
        if (task == first.currTask) {
            first = first.nextTask;
            size--;
            return true;
        }
        if (task == last.currTask) {
            last = last.prevTask;
            last.nextTask = null;
            size--;
            return true;
        }
        else {
            for (int i = 0; i < size; i++){
                if (innTaskSearch.currTask.getTitle().equals(task.getTitle())) {
                    innTaskSearch.prevTask.nextTask = innTaskSearch.nextTask;
                    innTaskSearch.nextTask.prevTask = innTaskSearch.prevTask;
                    size--;
                    return true;
                }
                innTaskSearch = innTaskSearch.nextTask;
            }
        }
        System.out.println("Task " + task.getTitle() + " відсутня в LinkedTaskList.");
        return false;
    }

    /* повертає кількість задач у списку */
    @Override
    public int size() {
        return size;
    }

    /* повертає задачу по індексу */
    @Override
    public Task getTask(int index) throws IndexOutOfBoundsException {
        InnerTask inn = first;
        getTaskCHECK(index);
        for (int i = 0; i < index; i++) {
            inn = inn.nextTask;
        }
        return inn.currTask;
    }

    @Override
    public void getTaskCHECK(int index) throws IndexOutOfBoundsException {
        if (index >= size)
            throw new IndexOutOfBoundsException("LinkedList has " + size + " tasks (last has index " + (size - 1) + " from 0 in LinkedTaskList). Requested task is " + index + ".");
    }

    /* Ітератор */
    @Override
    public Iterator<Task> iterator() {
        return new Iterator<Task>() {

            InnerTask iterObserver = first;

            @Override
            public boolean hasNext() {
                return iterObserver != null;
            }

            @Override
            public Task next() {
                if (!hasNext()) throw new IllegalStateException("Не вдається знайти наступну Task.");
                Task task = iterObserver.currTask;
                iterObserver = iterObserver.nextTask;
                return task;
            }

            @Override
            public void remove() {
                if (iterObserver.prevTask == null) throw new IllegalStateException("Нічого не можна видалити.");
                LinkedTaskList.this.remove(iterObserver.prevTask.currTask);
            }
        };
    }
}
