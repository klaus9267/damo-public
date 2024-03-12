
CREATE TABLE users (
                       id bigint NOT NULL AUTO_INCREMENT,
                       created_at datetime(6) DEFAULT NULL,
                       email varchar(255) NOT NULL,
                       name varchar(255) NOT NULL,
                       profile_image_url varchar(255) DEFAULT NULL,
                       provider_id varchar(255) NOT NULL,
                       provider_type enum('GOOGLE','NAVER','KAKAO') NOT NULL,
                       role enum('ADMIN','USER') NOT NULL,
                       username varchar(255) NOT NULL,
                       PRIMARY KEY (id)
);

CREATE TABLE persons (
                         id bigint NOT NULL AUTO_INCREMENT,
                         contact varchar(255) DEFAULT NULL,
                         created_at datetime(6) DEFAULT NULL,
                         memo text,
                         name varchar(255) NOT NULL,
                         relation enum('FAMILY','RELATIVE','FRIEND','ACQUAINTANCE','COMPANY','ETC') NOT NULL,
                         updated_at datetime(6) DEFAULT NULL,
                         user_id bigint NOT NULL,
                         PRIMARY KEY (id),
                         FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE transactions (
                              id bigint NOT NULL AUTO_INCREMENT,
                              category enum('CASH','GIFT','MOBILE_GIFTS','ETC') DEFAULT NULL,
                              created_at datetime(6) DEFAULT NULL,
                              memo text,
                              action enum('TOTAL','GIVING','RECEIVING') DEFAULT NULL,
                              amount bigint DEFAULT NULL,
                              updated_at datetime(6) DEFAULT NULL,
                              person_id bigint NOT NULL,
                              user_id bigint NOT NULL,
                              PRIMARY KEY (id),
                              FOREIGN KEY (person_id) REFERENCES persons (id),
                              FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE schedules (
                           id bigint NOT NULL AUTO_INCREMENT,
                           created_at datetime(6) DEFAULT NULL,
                           event varchar(255) NOT NULL,
                           event_date datetime(6) NOT NULL,
                           memo text,
                           status enum('IMPORTANT','NORMAL') DEFAULT NULL,
                           updated_at datetime(6) DEFAULT NULL,
                           transaction_id bigint DEFAULT NULL,
                           user_id bigint DEFAULT NULL,
                           PRIMARY KEY (id),
                           FOREIGN KEY (transaction_id) REFERENCES transactions (id),
                           FOREIGN KEY (user_id) REFERENCES users (id)
);


CREATE TABLE refresh_tokens (
                                id bigint NOT NULL AUTO_INCREMENT,
                                refresh_token varchar(500) NOT NULL,
                                username varchar(255) DEFAULT NULL UNIQUE,
                                PRIMARY KEY (id)
);
