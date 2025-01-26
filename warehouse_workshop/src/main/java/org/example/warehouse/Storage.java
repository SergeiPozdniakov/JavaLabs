package org.example.warehouse;

/*
[ ] Добавление товара на склад (идентификатор, название, количество, категория, место хранения)
[ ] Добавление списком
[ ] Удаление товара со склада
[ ] Поиск товара по идентификатору
[ ] Поиск по одному
[ ] Поиск всех колес по модели по списку отсортированном в алфавитном порядке
[ ]Вывод всех товаров на складе
[ ] Создание отчета по остаткам товаров на складе
[ ] Вывод всех категорий товаров
[ ] Вывод количества товаров по категории и месту хранения
[ ] Вывод общего количества товаров на складе
*/

import org.example.warehouse.exception.ItemNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public interface Storage {
    void putItem(Wheel wheel);

    Wheel getItem(String id) throws ItemNotFoundException;

    boolean containsItem(String id);

    Wheel removeItem(String id) throws ItemNotFoundException;

    void putAllItem(List<Wheel> items);

    Map<String, Wheel> getAllItems();

    List<Wheel> getAllItemsSorted(Predicate<Wheel> predicate);

}
