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

CREATE TABLE IF NOT EXISTS `parksinsa`.`member` (
  `id` VARCHAR(100) NOT NULL,
  `password` VARCHAR(512) NULL,
  `name` VARCHAR(100) NULL,
  `email` VARCHAR(200) NULL,
  `created_dtm` DATETIME NULL,
  `create_id` VARCHAR(45) NULL,
  `modified_dtm` DATETIME NULL,
  `modify_id` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `parksinsa`.`orders` (
  `id` VARCHAR(50) NOT NULL,
  `member_id` VARCHAR(100) NOT NULL,
  `address` VARCHAR(1000) NULL,
  `status` VARCHAR(3) NULL,
  `pay_type` VARCHAR(3) NULL,
  `pay_amount` INT NULL,
  `pay_date` DATETIME NULL,
  `cancel_yn` CHAR(2) NULL,
  `cancel_date` DATETIME NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Orders_member1_idx` (`member_id` ASC) VISIBLE,
  CONSTRAINT `fk_Orders_member1`
    FOREIGN KEY (`member_id`)
    REFERENCES `parksinsa`.`member` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `parksinsa`.`order_details` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `orders_id` VARCHAR(50) NOT NULL,
  `product_id` INT NOT NULL,
  `product_name` VARCHAR(200) NULL,
  `price` INT NULL,
  `quantity` INT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_order_details_product1_idx` (`product_id` ASC) VISIBLE,
  INDEX `fk_order_details_orders1_idx` (`orders_id` ASC) VISIBLE,
  CONSTRAINT `fk_order_details_product1`
    FOREIGN KEY (`product_id`)
    REFERENCES `parksinsa`.`product` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_order_details_orders1`
    FOREIGN KEY (`orders_id`)
    REFERENCES `parksinsa`.`orders` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

ALTER TABLE parksinsa.orders ADD receipt_id VARCHAR(200) NOT NULL;