
CREATE TABLE users (
                         id bigint NOT NULL AUTO_INCREMENT,
                         created_at datetime(6) DEFAULT NULL,
                         email varchar(255) DEFAULT NULL,
                         name varchar(255) DEFAULT NULL,
                         provider enum('GOOGLE','NAVER','KAKAO') NOT NULL,
                         provider_id varchar(255) DEFAULT NULL,
                         role enum('ADMIN','USER') NOT NULL,
                         username varchar(255) DEFAULT NULL,
                         PRIMARY KEY (id)
);


CREATE TABLE persons (
                           id bigint NOT NULL AUTO_INCREMENT,
                           created_at datetime(6) DEFAULT NULL,
                           memo text,
                           name varchar(255) DEFAULT NULL,
                           relation varchar(255) DEFAULT NULL,
                           user_id bigint NOT NULL,
                           scheduleCount int DEFAULT NULL,
                           updated_at datetime(6) DEFAULT NULL,
                           PRIMARY KEY (id),
                           CONSTRAINT FKrp309masjisdm7mmqon63obpv FOREIGN KEY (user_id) REFERENCES users (id)
);



CREATE TABLE schedules (
                             id bigint NOT NULL AUTO_INCREMENT,
                             amount int DEFAULT NULL,
                             created_at datetime(6) DEFAULT NULL,
                             date datetime(6) NOT NULL,
                             memo text,
                             status enum('IMPORTANT','NORMAL') NOT NULL,
                             updated_at datetime(6) DEFAULT NULL,
                             person_id bigint NOT NULL,
                             event varchar(255) NOT NULL,
                             transaction enum('GIVING','RECEIVING') NOT NULL,
                             PRIMARY KEY (id),
                             CONSTRAINT FK94xfi2i2yu3p9qfsslnrf1lkx FOREIGN KEY (person_id) REFERENCES persons (id)
);

