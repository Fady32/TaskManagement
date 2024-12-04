CREATE TABLE IF NOT EXISTS users
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    username     VARCHAR(255) NOT NULL UNIQUE,
    password     VARCHAR(255) NOT NULL,
    email        VARCHAR(255) NOT NULL,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP
);

CREATE TABLE IF NOT EXISTS user_roles
(
    user_id BIGINT,
    role    VARCHAR(255),
    PRIMARY KEY (user_id, role),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS tasks
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    title        VARCHAR(255) NOT NULL,
    description  TEXT,
    status       VARCHAR(50),
    priority     VARCHAR(50),
    due_date     TIMESTAMP,
    created_by   VARCHAR(255) NOT NULL,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP
);


-- Create the join table for the Many-to-Many relationship
CREATE TABLE IF NOT EXISTS task_users
(
    task_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    PRIMARY KEY (task_id, user_id),
    FOREIGN KEY (task_id) REFERENCES tasks (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

-- Create the `history` table if it does not exist
CREATE TABLE IF NOT EXISTS history
(
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    action            VARCHAR(255) NOT NULL,
    task_id           BIGINT,
    old_task_status   VARCHAR(255),
    new_task_status   VARCHAR(255),
    old_task_priority VARCHAR(255),
    new_task_priority VARCHAR(255),
    timestamp         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_id           BIGINT,
    FOREIGN KEY (task_id) REFERENCES tasks (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS notification
(
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    message   TEXT NOT NULL,
    is_sent   BOOLEAN   DEFAULT FALSE,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_id   BIGINT,
    FOREIGN KEY (user_id) REFERENCES users (id)
);

