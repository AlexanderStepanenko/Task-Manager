package ua.sumdu.j2se.oleksandrStepanenko.tasks;


import java.util.Date;

public class Main {

    public static void main(String[] args) throws CustomExсeption {

        Task task1 = new Task("Нова Task", new Date(2018, 12, 1), new Date(2018, 12, 12), 3);
        task1.setActive(true);

        System.out.println(task1.toString());

        ArrayTaskList arrayTaskList = new ArrayTaskList();
        arrayTaskList.add(task1);
        arrayTaskList.remove(task1);
        arrayTaskList.size();


    }
}
