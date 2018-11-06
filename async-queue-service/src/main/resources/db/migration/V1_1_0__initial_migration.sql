CREATE TABLE `activation_tokens` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `token` varchar(255) NOT NULL,
  `created` datetime(6) NOT NULL,
  `expired` bit(1) NULL DEFAULT FALSE,
  `used` bit(1) NULL DEFAULT FALSE,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

