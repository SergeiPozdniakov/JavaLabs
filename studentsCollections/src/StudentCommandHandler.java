import java.util.Map;

public class StudentCommandHandler {

    private StudentStorage studentStorage = new StudentStorage();


    public void processCommand(Command command) {
        Action action = command.getAction();
        switch (action) {
            case CREATE -> {
                processCreateCommand(command);
                break;
            }
            case UPDATE -> {
                processUpdateCommand(command);
                break;
            }
            case DELETE -> {
                processDeleteCommand(command);
                break;
            }
            case STATS_BY_COURSE -> {
                processStatsByCourseCommand(command);
                break;
            }
            case STATS_BY_CITY -> {
                processByCityCommand(command);
                break;
            }
            case SEARCH -> {
                processSearchCommand(command);
                break;
            }
            default -> {
                System.out.println("Действие " + action + " не поддерживается");
            }
        }

        System.out.println("Обработка команды. "
        + "Действие: " + command.getAction().name() + ". данные: " + command.getData());
    }

    private void processSearchCommand(Command command) {
        String surname = command.getData();
        studentStorage.search(surname);
    }

    private void processStatsByCourseCommand(Command command) {
        Map<String, Long> data = studentStorage.getCountByCourse();
        studentStorage.printMap(data);
    }

    private void processByCityCommand(Command command) {
        Map<String, Long> data = studentStorage.getCountByCity();
        studentStorage.printMap(data);
    }

    private void processCreateCommand(Command command) {
        try {
            String data = command.getData();
            String[] dataArray = data.split(",");

            // Проверяем, что данных достаточно
            if (dataArray.length < 5) {
                System.out.println("Ошибка: недостаточно данных. Ожидается 5 значений: фамилия, имя, курс, город, возраст.");
                return;
            }

            // Убираем лишние пробелы из данных
            for (int i = 0; i < dataArray.length; i++) {
                dataArray[i] = dataArray[i].trim();
            }

            // Создаем студента
            Student student = new Student();
            student.setSurname(dataArray[0]);
            student.setName(dataArray[1]);
            student.setCourse(dataArray[2]);
            student.setCity(dataArray[3]);

            // Парсим возраст
            try {
                student.setAge(Integer.valueOf(dataArray[4]));
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: возраст должен быть числом.");
                return;
            }

            // Сохраняем студента
            studentStorage.createStudent(student);
            studentStorage.printAll();

        } catch (Exception e) {
            System.out.println("Ошибка при обработке команды: " + e.getMessage());
        }
    }

    public void processUpdateCommand(Command command) {
        try {
            String data = command.getData();

            // Проверяем, что данные не пустые
            if (data == null || data.isEmpty()) {
                System.out.println("Ошибка: данные не могут быть пустыми.");
                return;
            }

            String[] dataArray = data.split(",");

            // Проверяем, что данных достаточно
            if (dataArray.length < 6) {
                System.out.println("Ошибка: недостаточно данных. Ожидается 6 значений: id, фамилия, имя, курс, город, возраст.");
                return;
            }

            // Убираем лишние пробелы из данных
            for (int i = 0; i < dataArray.length; i++) {
                dataArray[i] = dataArray[i].trim();
            }

            // Парсим id
            Long id;
            try {
                id = Long.valueOf(dataArray[0]);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: id должен быть числом.");
                return;
            }

            // Парсим возраст
            int age;
            try {
                age = Integer.valueOf(dataArray[5]);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: возраст должен быть числом.");
                return;
            }

            // Создаем студента
            Student student = new Student();
            student.setSurname(dataArray[1]);
            student.setName(dataArray[2]);
            student.setCourse(dataArray[3]);
            student.setCity(dataArray[4]);
            student.setAge(age);

            // Обновляем данные студента
            boolean isUpdated = studentStorage.updateStudent(id, student);
            if (!isUpdated) {
                System.out.println("Ошибка: студент с id " + id + " не найден.");
            } else {
                System.out.println("Данные студента успешно обновлены.");
            }

        } catch (Exception e) {
            System.out.println("Ошибка при обработке команды: " + e.getMessage());
        }
    }

    // тут на удаление приходит только одна id. Читаем её как строку, потом переводим в int
    public void processDeleteCommand(Command command) {
        String data = command.getData();
        Long id = Long.valueOf(data);
        studentStorage.deleteStudent(id);
        studentStorage.printAll();
    }

}
