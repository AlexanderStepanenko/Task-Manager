package ua.sumdu.j2se.oleksandrStepanenko.tasks;


import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;


public class TaskIO {

    /* Метод, який записує задачі із списку у потік у бінарному форматі, описаному нижче */
    public static void write (TaskList tasks, OutputStream out) throws IOException{
        try
                (DataOutputStream dataStreamOut = new DataOutputStream(out)) {
            //1) указуємо кількість Task
            dataStreamOut.writeInt(tasks.size());
            //2. для всього списка задач записуємо:
            for (Task task : tasks){
                //2.1 довжина назви
                dataStreamOut.writeInt(task.getTitle().length());
                //2.2 назва
                dataStreamOut.writeUTF(task.getTitle());
                //2.3 активність
                dataStreamOut.writeBoolean(task.isActive());
                //2.4 інтервал повторення
                dataStreamOut.writeInt(task.getRepeatInterval());
                //3) якщо повторюється або не повторюється
                if (task.isRepeated()){
                    //3.1 повторюється - час початку, час кінця
                    dataStreamOut.writeLong(task.getStartTime().getTime());
                    dataStreamOut.writeLong(task.getEndTime().getTime());
                }
                //3.2 не повторюється - час Task
                else dataStreamOut.writeLong(task.getTime().getTime());
            }
        }
    }

    /* Метод, який зчитує задачі із потоку у даний список задач. */
    public static void read (TaskList tasks, InputStream in) throws Exception{
        //аналогично к write
        try (DataInputStream dataStreamIn = new DataInputStream(in)){
            //зчитуємо кількість Task у списку
            int size = dataStreamIn.readInt();
            //для кожної Task зчитуємо її поля:
            for (int i = 0; i < size; i++){
                //2.1 довжина назви:
                int lenght = dataStreamIn.readInt();
                //2.2 назва:
                String title = dataStreamIn.readUTF();
                //2.3 активність:
                boolean active = dataStreamIn.readBoolean();
                //2.4 інтервал повторення:
                int interval = dataStreamIn.readInt();
                //повторюється або не повторюється
                long timeOrStart = dataStreamIn.readLong();
                //створюємо Task та додаємо у список:
                if (interval > 0){
                    long end = dataStreamIn.readLong();
                    Task task = new Task(title, new Date(timeOrStart), new Date(end), interval, active);
                    task.setActive(active);
                    tasks.add(task);
                }
                else {
                    Task task = new Task(title, new Date(timeOrStart),active);
                    task.setActive(active);
                    tasks.add(task);
                }
            }
        }
    }

    /* Метод, який записує задачі із списку у файл. */
    public static void writeBinary (TaskList tasks, File file) throws IOException{
        try (BufferedOutputStream dataOutStream = new BufferedOutputStream(new FileOutputStream(file))){
            write(tasks, dataOutStream);
        }
    }

    /* Метод, який зчитує задачі із файлу у список задач. */
    public static void readBinary (TaskList tasks, File file) throws Exception{
        try (BufferedInputStream dataInnStream = new BufferedInputStream(new FileInputStream(file))){
            read(tasks,dataInnStream);
        }
    }

    // СТАТИЧНІ МЕТОДИ:

    /* Метод, який записує задачі зі списку у потік в текстовому форматі */
    public static void write(TaskList tasks, Writer out) throws IOException {
        try (BufferedWriter dataOut = new BufferedWriter(out)) {
            int count = 0;
            for (Task task : tasks) {
                dataOut.append(task.toString());
                //Після кожної задачі іде точка із комою, після останньої – крапка.
                dataOut.append(count < tasks.size()-1 ? ";" : ".");
                //Кожна задача розміщується на окремому рядку.
                dataOut.newLine();
                count++;
            }
        }
    }

    /* Метод, який зчитує задачі із потоку у список. */
    public static void read(TaskList tasks, Reader in) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("[y-MM-dd HH:mm:ss.S]");
        BufferedReader dataIn = new BufferedReader(in);
        String line = "";
        while ((line = dataIn.readLine()) != null) {
            int start = line.indexOf('\"');
            int finish = line.lastIndexOf('\"');
            String title = line.substring(start + 1, finish);
            boolean active = !line.contains("inactive");

            if (line.contains(" at [")) {
                String stringDate = line.substring(line.indexOf("["), line.indexOf("]") + 1);
                Date dateStart = dateFormat.parse(stringDate);
                Task task = new Task(title, new Date(dateStart.getTime()));
                task.setActive(active);
                tasks.add(task);
            } else {
                String stringStart = line.substring(line.indexOf("["), line.indexOf("]") + 1);
                Date dateStart = dateFormat.parse(stringStart);
                line = line.substring(line.indexOf("]") + 1);
                String stringEnd = line.substring(line.indexOf("["), line.indexOf("]") + 1);
                Date dateEnd = dateFormat.parse(stringEnd);
                line = line.substring(line.indexOf("]") + 1);
                String interval = line.substring(line.lastIndexOf("[", line.indexOf("[") + 1) + 1,
                        line.lastIndexOf("]", line.indexOf("]") + 1));
                String[] forInterval = interval.split(" ");
                long days;
                long hours;
                long minutes;
                long seconds;
                int theInterval = 0;

                switch (forInterval.length) {
                    case 4:
                        minutes = Long.parseLong(forInterval[0]);
                        seconds = Long.parseLong(forInterval[2]);
                        theInterval = (int) (seconds * 1000 + minutes * 60 * 1000) / 1000;
                        break;
                    case 6:
                        hours = Long.parseLong(forInterval[0]);
                        minutes = Long.parseLong(forInterval[2]);
                        seconds = Long.parseLong(forInterval[4]);
                        theInterval = (int) (seconds * 1000 + minutes * 60 * 1000 + hours * 3600 * 1000) / 1000;
                        break;
                    case 8:
                        days = Long.parseLong(forInterval[0]);
                        hours = Long.parseLong(forInterval[2]);
                        minutes = Long.parseLong(forInterval[4]);
                        seconds = Long.parseLong(forInterval[6]);
                        theInterval = (int) (seconds * 1000 + minutes * 60 * 1000 + hours * 3600 * 1000 + days * 8640 * 1000) / 1000;
                        break;
                }
                Task task = new Task(title, new Date(dateStart.getTime()), new Date(dateEnd.getTime()), theInterval);
                task.setActive(active);
                tasks.add(task);
            }
        }
    }

    /* Метод, який записує задачі у файл у текстовому форматі */
    public static void writeText (TaskList tasks, File file) throws IOException{
        try (PrintWriter textDataOut = new PrintWriter(new BufferedWriter(new FileWriter(file)))){
            write(tasks,textDataOut);
        }
    }

    /* Метод, який зчитує задачі із файлу у текстовому вигляді. */
    public static void readText (TaskList tasks, File file) throws Exception{
        try (BufferedReader textDataIn = new BufferedReader(new FileReader(file))){
            read(tasks, textDataIn);
        }
    }
}
