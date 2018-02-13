package ua.sumdu.j2se.oleksandrStepanenko.tasks;

import java.util.*;

public class Tasks {

    /* Метод incoming у вигляді статичного методу. При цьому метод необхідно абстрагувати від зписків задач */
    public static Iterable<Task> incoming(Iterable<Task> tasks, Date start, Date end) throws CustomExсeption {

        TaskList arOfTasks = new ArrayTaskList();
        for (Task task : tasks) {
            Date date = task.nextTimeAfter(start);
            if (date != null && date.compareTo(end) <= 0) arOfTasks.add(task);
        }
        return arOfTasks;
    }

    /* Статичний метод, який буде будувати календар задач на заданий період – таблицю, де кожній даті відповідає множина
      * задач, що мають бути виконані в цей час, при чому одна задача може зустрічатись відповідно до декількох дат,
      * якщо вона має бути виконана декілька разів за вказаний період. */
    public static SortedMap<Date, Set<Task>> calendar (Iterable<Task> tasks, Date start, Date end) throws CustomExсeption {

        TreeMap<Date, Set<Task>> setOfTasks = new TreeMap<>();
        TaskList tempListOfTasks = (ArrayTaskList) incoming(tasks, start, end);

        for (Task task : tempListOfTasks){
            //сворюємо час, від якого будемо відштовхуватись та шукати Task
            Date date = task.nextTimeAfter(start);
            //якщо час відповідає умовам (тобто не виходить за рамки Календаря) - Task додається
            while (date.compareTo(end) <= 0){
                /*перевірка наявності ключа "Date", якщо такий ключ існує то до нього додається Task,
                 якщо ні - створюється новий ключ і Task додається до нього*/
                if (setOfTasks.containsKey(date)){
                    setOfTasks.get(date).add(task);
                }
                else {
                    HashSet innerSet = new HashSet();
                    innerSet.add(task);
                    setOfTasks.put(date, innerSet);
                }
                if (!task.isRepeated()){
                    break;
                }
                //змінюємо час за домопогою якого ми шукаємо Task
                if (task.nextTimeAfter(date) != null){
                    date = task.nextTimeAfter(date);
                }
                else {
                    break;
                }
            }
        }
        return setOfTasks;
    }
}
