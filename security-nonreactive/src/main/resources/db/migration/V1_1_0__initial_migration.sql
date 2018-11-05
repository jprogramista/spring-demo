CREATE TABLE `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created` datetime(6) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `enabled` bit(1) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `oauth_service_type` varchar(255) DEFAULT NULL,
  `oauth_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `users` VALUES (null, NOW(), 'test@example.com', TRUE, '$2a$10$LGnaUUfQAnQyzxqqLY5AnOkBsoms3eBKgYyYDXAx.WjF6UwWidC3e', null, null);
INSERT INTO `users` VALUES (null, NOW(), 'test2@example.com', FALSE, '$2a$10$/J1XlAk43vKI64iFiLzGFuxyYzB4MBulrapzc/IBJb.sO813PiqB2', null, null);

CREATE TABLE `authorities` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `user_id` bigint(20) NOT NULL,
  `role` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_authorities_users` (`email`),
  CONSTRAINT `fk_authorities_users` FOREIGN KEY (`email`) REFERENCES `users` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into authorities(email, user_id, role) values('test@example.com', 1, 'ROLE_USER');
insert into authorities(email, user_id, role) values('test2@example.com', 2, 'ROLE_USER');

CREATE TABLE `details` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5fhnw08l4phx5dshclpkuu5b9` (`user_id`),
  CONSTRAINT `FK5fhnw08l4phx5dshclpkuu5b9` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `details` VALUES (null, 'languages', 'Java, JavaScript', (select id from users where email = 'test@example.com'));
INSERT INTO `details` VALUES (null, 'databases', 'MySQL, H2', (select id from users where email = 'test@example.com'));
INSERT INTO `details` VALUES (null, 'frameworks', 'Spring, Hibernate', (select id from users where email = 'test@example.com'));

create table persistent_logins(
     series varchar(64) not null primary key,
     username varchar(75) not null,
     token varchar(100) not null,
     last_used timestamp not null
);