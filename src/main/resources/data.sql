


-- Insert Users
INSERT INTO users (id, username, email, password, created_date, updated_date)
SELECT 98761, 'admin', 'admin@test.com', '$2a$10$fZszCN9bENMfzITDkNiKse.SwgWtAUZ6GG6/YEg3aJb1SxAZ65/Je', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin');

INSERT INTO users (id, username, email, password, created_date, updated_date)
SELECT 98762, 'user1', 'user@test.com', '$2a$10$fZszCN9bENMfzITDkNiKse.SwgWtAUZ6GG6/YEg3aJb1SxAZ65/Je', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'user1');

INSERT INTO users (id, username, email, password, created_date, updated_date)
SELECT 98763, 'user2', 'fady@test.com', '$2a$10$fZszCN9bENMfzITDkNiKse.SwgWtAUZ6GG6/YEg3aJb1SxAZ65/Je', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'user2');


-- Insert User Roles
INSERT INTO user_roles (user_id, role)
SELECT (SELECT id FROM users WHERE username = 'admin'), 'ADMIN'
WHERE NOT EXISTS (
    SELECT 1 FROM user_roles ur
    JOIN users u ON ur.user_id = u.id
    WHERE u.username = 'admin' AND ur.role = 'ADMIN'
);

INSERT INTO user_roles (user_id, role)
SELECT (SELECT id FROM users WHERE username = 'user1'), 'USER'
WHERE NOT EXISTS (
    SELECT 1 FROM user_roles ur
    JOIN users u ON ur.user_id = u.id
    WHERE u.username = 'user1' AND ur.role = 'USER'
);

INSERT INTO user_roles (user_id, role)
SELECT (SELECT id FROM users WHERE username = 'user2'), 'ADMIN'
WHERE NOT EXISTS (
    SELECT 1 FROM user_roles ur
    JOIN users u ON ur.user_id = u.id
    WHERE u.username = 'user2' AND ur.role = 'ADMIN'
);


-- Insert Tasks
INSERT INTO tasks (id, title, description, status, priority, due_date, created_by, created_date, updated_date)
SELECT 98761, 'Fix Login Issue', 'Resolve the issue with user login functionality.', 'TODO', 'HIGH', '2024-12-01 12:00:00', 'admin' , CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM tasks WHERE id = 98761);

INSERT INTO tasks (id, title, description, status, priority, due_date,created_by, created_date, updated_date)
SELECT 98762, 'Database Backup', 'Perform regular database backup.', 'IN_PROGRESS', 'MEDIUM', '2024-12-05 15:00:00', 'user1' , CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM tasks WHERE id = 98762);

INSERT INTO tasks (id, title, description, status, priority, due_date, created_by, created_date, updated_date)
SELECT 98763, 'Email Notifications', 'Setup email notifications for tasks.', 'DONE', 'LOW', '2024-12-10 10:00:00' ,'user2' , CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM tasks WHERE id = 98763);




INSERT INTO history (action, task_id, old_task_status, new_task_status, old_task_priority, new_task_priority, user_id, timestamp)
SELECT 'Task Created', 98761, 'Not Started', 'In Progress', 'Low', 'Medium', 98761, CURRENT_TIMESTAMP
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM history WHERE task_id = 98761 AND user_id = 98761 AND timestamp = '2024-11-30 08:00:00'
);

-- Insert Notifications
INSERT INTO notification (message, is_sent, user_id, timestamp )
SELECT 'Your task deadline is approaching', FALSE, 98761, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM notification WHERE message = 'Your task deadline is approaching' AND user_id = 98761
);

INSERT INTO notification (message, is_sent, user_id, timestamp)
SELECT 'A new task has been assigned to you', FALSE, 98762 , CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM notification WHERE message = 'A new task has been assigned to you' AND user_id = 98762
);