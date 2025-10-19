package dao;

import entity.Task;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class TaskDao {
  private final DataSource dataSource;

  public TaskDao(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public Task save(Task task) {
    String sql = "INSERT INTO task (title, finished, created_date) VALUES (?, ?, ?)";
    try(
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
    ) {

      // Нормализуем время и обновляем объект Task
      LocalDateTime normalizedTime = normalizeTime(task.getCreatedDate());
      task.setCreatedDate(normalizedTime); // ОБНОВЛЯЕМ объект!

      statement.setString(1, task.getTitle());
      statement.setBoolean(2, task.getFinished());
      statement.setTimestamp(3, Timestamp.valueOf(normalizedTime));
      statement.executeUpdate();

      try(ResultSet resultSet = statement.getGeneratedKeys()) {
        if (resultSet.next()) {
          task.setId(resultSet.getInt(1));
        }
      }

    } catch (SQLException throwables) {
      throw new RuntimeException(throwables);
    }

    return task;
  }

  public List<Task> findAll() {
    List<Task> tasks = new ArrayList<>();
    String sql = "SELECT task_id, title, finished, created_date FROM task ORDER BY task_id";
    try(Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql)
    ) {

      while(resultSet.next()) {
        tasks.add(mapToTask(resultSet));
      }

    } catch (SQLException throwables) {
      throw new RuntimeException(throwables);
    }

    return tasks;
  }

  public int deleteAll() {
    String sql = "DELETE FROM task";
    try (Connection connection = dataSource.getConnection();
         Statement statement = connection.createStatement()) {
      return statement.executeUpdate(sql);
    } catch (SQLException throwables) {
      throw new RuntimeException(throwables);
    }
  }

  public Task getById(Integer id) {
    String sql = "SELECT task_id, title, finished, created_date FROM task WHERE task_id = ?";
    try(Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)
    ) {
      statement.setInt(1, id);
      try (ResultSet resultSet = statement.executeQuery()) {
        if (!resultSet.next()) {
          return null;
        }
        return mapToTask(resultSet);
      }
    } catch (SQLException throwables) {
      throw new RuntimeException(throwables);
    }
  }

  public List<Task> findAllNotFinished() {
    List<Task> tasks = new ArrayList<>();
    String sql = "SELECT task_id, title, finished, created_date FROM task WHERE finished = false ORDER BY task_id";
    try(Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery()
    ) {
      while(resultSet.next()) {
        tasks.add(mapToTask(resultSet));
      }
    } catch (SQLException throwables) {
      throw new RuntimeException(throwables);
    }
    return tasks;
  }

  public List<Task> findNewestTasks(Integer numberOfNewestTasks) {
    List<Task> tasks = new ArrayList<>();
    String sql = "SELECT task_id, title, finished, created_date FROM task ORDER BY created_date DESC, task_id DESC LIMIT ?";
    try(Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)
    ) {
      statement.setInt(1, numberOfNewestTasks);
      try (ResultSet resultSet = statement.executeQuery()) {
        while(resultSet.next()) {
          tasks.add(mapToTask(resultSet));
        }
      }
    } catch (SQLException throwables) {
      throw new RuntimeException(throwables);
    }
    return tasks;
  }

  public Task finishTask(Task task) {
    String sql = "UPDATE task SET finished = true WHERE task_id = ?";
    try(Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)
    ) {
      statement.setInt(1, task.getId());
      statement.executeUpdate();
      task.setFinished(true);
    } catch (SQLException throwables) {
      throw new RuntimeException(throwables);
    }
    return task;
  }

  public void deleteById(Integer id) {
    String sql = "DELETE FROM task WHERE task_id = ?";
    try(Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)
    ) {
      statement.setInt(1, id);
      statement.executeUpdate();
    } catch (SQLException throwables) {
      throw new RuntimeException(throwables);
    }
  }

  private Task mapToTask(ResultSet resultSet) throws SQLException {
    // Нормализуем время при чтении
    LocalDateTime createdDate = normalizeTime(resultSet.getTimestamp("created_date").toLocalDateTime());
    Task task = new Task(
            resultSet.getString("title"),
            resultSet.getBoolean("finished"),
            createdDate
    );
    task.setId(resultSet.getInt("task_id"));
    return task;
  }

  // Метод для нормализации времени - убираем наносекунды
  private LocalDateTime normalizeTime(LocalDateTime dateTime) {
    return dateTime.withNano(0);
  }
}