package ua.sumdu.j2se.oleksandrStepanenko.tasks;


import java.io.Serializable;
import java.util.Iterator;

public abstract class TaskList implements Iterable<Task>, Serializable {

    protected int size;

    /* абстрактні методи*/

    abstract void add(Task task) throws CustomExсeption;

    abstract boolean remove(Task task) throws CustomExсeption;

    abstract int size();

    abstract Task getTask(int index) throws CustomExсeption;

    abstract void getTaskCHECK(int index) throws CustomExсeption;

    /* метод з тілом */

    public void taskNotNullCheck(Task task) throws IllegalStateException {
        if (task == null) throw new IllegalStateException("Task відсутня.");
    }

    /* Equals */
    @Override
    public boolean equals(Object taskList) {
        if (taskList == null) return false;
        if (this == taskList) return true;
        if (this.getClass() != taskList.getClass()) return false;

        TaskList other = (TaskList) taskList;

        Iterator<Task> iterator = this.iterator();

        if (this.size() != other.size()) return false;

        for (Task t : other) {
            if (!(iterator.next().equals(t))) return false;
        }
        return true;
    }

    /* HashCode для TaskLists */
    @Override
    public int hashCode() {
        int code = 1;
        for(Task a : this){
            code = code + a.hashCode();
        }
        return code;
    }
}
