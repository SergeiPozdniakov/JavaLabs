create table if not exists task (
    task_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255),
    finished BOOLEAN,
    created_date TIMESTAMP
);