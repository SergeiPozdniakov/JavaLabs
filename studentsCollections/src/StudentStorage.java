import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class StudentStorage {
    private Map<Long, Student> studentStorageMap = new HashMap<>();

    private StudentSurnameStorage studentSurnameStorage = new StudentSurnameStorage();

    private Long currentId = 0L;

    /**
    * Создание данных о студенте
    * @param student данные о студенте
    * @return сгенерированный уникальный идентификатор студента
    * */
    public Long createStudent(Student student) {
        Long nextId = getNextId();
        studentStorageMap.put(nextId, student);
        studentSurnameStorage.studentCreated(nextId, student.getSurname());
        return nextId;
    }

 /**
 * Обновление данных о студенте
 * @param id идентификатор студента
 * @param student данные студента
 * @return true если данные будут обновлены, false если студент не найден
 */
    public boolean updateStudent(Long id, Student student) {
        if (studentStorageMap.containsKey(id)) {
            return false;
        } else {
            String newSurname = student.getSurname();
            String oldSurname = studentStorageMap.get(id).getSurname();
            studentSurnameStorage.studentUpdated(id, oldSurname, newSurname);

            studentStorageMap.put(id, student);
        }
        return true;
    }

/**
 * Удаляет данные о студенте
 * @param id идентификатор студента
 * @return true если студент был удален
 * false если студент не был найден по id
 * */
    public boolean deleteStudent(Long id) {
        Student removed = studentStorageMap.remove(id);
        if (removed != null) {
            String surname = removed.getSurname();
            studentSurnameStorage.studentDeleted(id, surname);
        }
        return removed != null;
    }

    public void search(String input) {
        if (input == null || input.trim().isEmpty()) {
            // Если введена пустая строка, выводим всех студентов
            studentStorageMap.values().forEach(System.out::println);
            return;
        }

        String[] surnames = input.split(",");
        if (surnames.length == 1) {
            // Если введена одна фамилия, выполняем точный поиск
            String surname = surnames[0].trim();
            Set<Long> studentIds = studentSurnameStorage.getStudentBySurnamesLessOrEqual(surname);
            studentIds.stream()
                    .map(studentStorageMap::get)
                    .filter(student -> student.getSurname().equalsIgnoreCase(surname))
                    .forEach(System.out::println);
        } else if (surnames.length == 2) {
            // Если введены две фамилии, выполняем поиск в диапазоне
            String startSurname = surnames[0].trim();
            String endSurname = surnames[1].trim();
            Set<Long> studentIds = studentSurnameStorage.getStudentBySurnamesLessOrEqual(endSurname);
            studentIds.stream()
                    .map(studentStorageMap::get)
                    .filter(student -> student.getSurname().compareToIgnoreCase(startSurname) >= 0 &&
                            student.getSurname().compareToIgnoreCase(endSurname) <= 0)
                    .forEach(System.out::println);
        } else {
            // Если ввод не соответствует ни одному из вариантов, выводим сообщение об ошибке
            System.out.println("Ошибка: некорректный ввод. Введите одну фамилию или две фамилии, разделенные запятой.");
        }
    }

    public Long getNextId() {
        currentId = currentId + 1;
        return currentId;
    }

    public void printAll() {
        System.out.println(studentStorageMap);
    }

    public void printMap (Map<String, Long> data) {
        data.forEach((key, value) -> System.out.println(key + " - " + value));
    }

    public Map<String, Long> getCountByCourse() {
        Map<String, Long> res;
        res = studentStorageMap.values().stream().collect(Collectors.toMap(student -> student.getCourse(), student -> 1L,
                (count1, count2) -> count1 + count2));


        // Реализация перебора с использованием "for"
        /*Map<String, Long> res = new HashMap<>();
        for (Student student : studentStorageMap.values()) {
            String key = student.getCourse();
            Long count = res.getOrDefault(key, 0L);
            count++;
            res.put(key, count);
        }  */
        return res;
    }

    public Map<String, Long> getCountByCity() {
        Map<String, Long> res;
        res = studentStorageMap.values().stream().collect(Collectors.toMap(student -> student.getCity(), student -> 1L,
                (count1, count2) -> count1 + count2));
        return res;
    }


}
