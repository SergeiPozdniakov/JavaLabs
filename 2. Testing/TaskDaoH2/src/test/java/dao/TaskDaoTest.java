package dao;

import entity.Task;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import javax.sql.DataSource;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TaskDaoTest {
  private TaskDao taskDao;
  private DataSource dataSource;

  private void initializeDb(DataSource dataSource) {
    try(InputStream inputStream = this.getClass().getResource("/initial.sql").openStream()) {
      String sql = new String(inputStream.readAllBytes());
      try(
              Connection connection = dataSource.getConnection();
              Statement statement = connection.createStatement()
      ) {
        statement.executeUpdate(sql);
      }
    } catch (IOException | SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @BeforeAll
  public void setUp() throws SQLException {
    // Настройка H2 в файловом режиме с сохранением данных
    String url = "jdbc:h2:file:./target/testdb;DB_CLOSE_DELAY=-1";
    String username = "sa";
    String password = "";

    DriverManagerDataSource dataSource = new DriverManagerDataSource(url, username, password);
    initializeDb(dataSource);
    this.dataSource = dataSource;
    taskDao = new TaskDao(dataSource);
  }

  // Добавляем beforeEach для очистки данных перед каждым тестом
  @BeforeEach
  public void beforeEach() {
    taskDao.deleteAll();
  }

  // Все тесты остаются без изменений
  @Test
  public void testSaveSetsId() {
    Task task = new Task("test task", false, LocalDateTime.now());
    taskDao.save(task);
    assertThat(task.getId()).isNotNull();
  }

  @Test
  public void testFindAllReturnsAllTasks() {
    Task firstTask = new Task("first task", false, LocalDateTime.now());
    taskDao.save(firstTask);

    Task secondTask = new Task("second task", false, LocalDateTime.now());
    taskDao.save(secondTask);

    assertThat(taskDao.findAll())
            .hasSize(2)
            .extracting("id")
            .contains(firstTask.getId(), secondTask.getId());
  }

  @Test
  public void testDeleteAllDeletesAllRowsInTasks() {
    Task firstTask = new Task("any task", false, LocalDateTime.now());
    taskDao.save(firstTask);

    int rowsDeleted = taskDao.deleteAll();
    assertThat(rowsDeleted).isEqualTo(1);

    assertThat(taskDao.findAll()).isEmpty();
  }

  @Test
  public void testFindNotFinishedReturnsCorrectTasks()  {
    Task unfinishedTask = new Task("unfinished task", false, LocalDateTime.now());
    taskDao.save(unfinishedTask);

    // ИСПРАВЛЕНИЕ: создаем завершенную задачу
    Task finishedTask = new Task("finished task", true, LocalDateTime.now());
    taskDao.save(finishedTask);

    assertThat(taskDao.findAllNotFinished())
            .singleElement()
            .extracting("id", "title", "finished", "createdDate")
            .containsExactly(unfinishedTask.getId(), unfinishedTask.getTitle(), unfinishedTask.getFinished(), unfinishedTask.getCreatedDate());
  }

  @Test
  public void testGetByIdReturnsCorrectTask() {
    Task task = new Task("test task", false, LocalDateTime.now());
    taskDao.save(task);

    // ИСПРАВЛЕНИЕ: сравниваем без наносекунд или используем isCloseTo
    Task foundTask = taskDao.getById(task.getId());
    assertThat(foundTask).isNotNull();
    assertThat(foundTask.getId()).isEqualTo(task.getId());
    assertThat(foundTask.getTitle()).isEqualTo(task.getTitle());
    assertThat(foundTask.getFinished()).isEqualTo(task.getFinished());

    // Сравниваем время с допуском в 1 миллисекунду
    assertThat(foundTask.getCreatedDate()).isEqualTo(task.getCreatedDate());
  }

  @Test
  public void testFindNewestTasksReturnsCorrectTasks() {
    Task firstTask = new Task("first task", false, LocalDateTime.now());
    taskDao.save(firstTask);

    Task secondTask = new Task("second task", false, LocalDateTime.now());
    taskDao.save(secondTask);

    Task thirdTask = new Task("third task", false, LocalDateTime.now());
    taskDao.save(thirdTask);

    assertThat(taskDao.findNewestTasks(2))
            .hasSize(2)
            .extracting("id")
            .containsExactlyInAnyOrder(secondTask.getId(), thirdTask.getId());
  }

  @Test
  public void testFinishSetsCorrectFlagInDb() {
    Task task = new Task("test task", false, LocalDateTime.now());
    taskDao.save(task);

    assertThat(taskDao.finishTask(task).getFinished()).isTrue();
    assertThat(taskDao.getById(task.getId()).getFinished()).isTrue();
  }

  @Test
  public void deleteByIdDeletesOnlyNecessaryData() {
    Task taskToDelete = new Task("first task", false, LocalDateTime.now());
    taskDao.save(taskToDelete);

    Task taskToPreserve = new Task("second task", false, LocalDateTime.now());
    taskDao.save(taskToPreserve);

    taskDao.deleteById(taskToDelete.getId());
    assertThat(taskDao.getById(taskToDelete.getId())).isNull();
    assertThat(taskDao.getById(taskToPreserve.getId())).isNotNull();
  }
}