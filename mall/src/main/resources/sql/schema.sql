-- 초기 DB 테이블 구축

CREATE TABLE IF NOT EXISTS `parksinsa`.`product` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(200) NOT NULL,
  `price` INT NULL,
  `created_dtm` DATETIME NULL,
  `create_id` VARCHAR(45) NULL,
  `modified_dtm` DATETIME NULL,
  `modify_id` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  INDEX `IDX_NAME` (`name` ASC) VISIBLE)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `parksinsa`.`product_img` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `product_id` INT NOT NULL,
  `main_img_name` VARCHAR(200) NULL,
  `main_img_path` VARCHAR(1024) NULL,
  `desc_img_name` VARCHAR(200) NULL,
  `desc_img_path` VARCHAR(1024) NULL,
  `created_dtm` DATETIME NULL,
  `create_id` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `parksinsa`.`admin` (
  `id` VARCHAR(100) NOT NULL,
  `password` VARCHAR(512) NULL,
  `name` VARCHAR(100) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;