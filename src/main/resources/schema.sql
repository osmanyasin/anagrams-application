CREATE TABLE IF NOT EXISTS word_table (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    content VARCHAR(50) NOT NULL,
    length SMALLINT NOT NULL,
    sorted_key VARCHAR(50) NOT NULL
);
