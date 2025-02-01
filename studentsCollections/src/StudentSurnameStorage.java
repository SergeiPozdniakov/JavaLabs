import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class StudentSurnameStorage {
    private TreeMap<String, Set<Long>> surnamesTreeMap = new TreeMap<>();

    public void studentCreated(Long id, String surname) {
        Set<Long> existingIds = surnamesTreeMap.getOrDefault(surname, new HashSet<>());
        existingIds.add(id);
        surnamesTreeMap.put(surname, existingIds);
    }

    public void studentDeleted(Long id, String surname) {
        surnamesTreeMap.get(surname).remove(id);
    }

    public void studentUpdated(Long id, String oldSurname, String newSurname) {
        studentDeleted(id, oldSurname);
        studentCreated(id, newSurname);
    }

    /**
     * Данный метод возвращает уникальные идентификаторы студентов,
     * чьи фамилии меньше или равны переданной
     * @return set
     * */
    public Set<Long> getStudentBySurnamesLessOrEqual(String surname) {
        Set<Long> res = surnamesTreeMap.headMap(surname, true)  // вернет Map с ключами меньше (либо равный), чем переданный
                .values().stream()
                .flatMap(longs -> longs.stream())   // Преобразует элементы Set в единый stream лонгов/longs, т.е. распаковывает коллекцию на поток элементов
                .collect(Collectors.toSet());
        return res;
    }
}
