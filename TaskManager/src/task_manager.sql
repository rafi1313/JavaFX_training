-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema task_manager
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema task_manager
-- -----------------------------------------------------
USE `task_manager` ;
-- -----------------------------------------------------
-- Schema task_manager
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema task_manager
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `task_manager` DEFAULT CHARACTER SET utf8 ;
USE `task_manager` ;

-- -----------------------------------------------------
-- Table `task_manager`.`course`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `task_manager`.`course` (
  `id_c` INT NOT NULL AUTO_INCREMENT,
  `course_name` VARCHAR(100) NOT NULL,
  `course_agenda` TEXT NOT NULL,
  `course_category` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id_c`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `task_manager`.`edition`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `task_manager`.`edition` (
  `date` DATE NOT NULL,
  `course_id_c` INT NOT NULL,
  INDEX `fk_edition_course_idx` (`course_id_c` ASC),
  CONSTRAINT `fk_edition_course`
    FOREIGN KEY (`course_id_c`)
    REFERENCES `task_manager`.`course` (`id_c`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

USE `task_manager` ;

-- -----------------------------------------------------
-- Table `task_manager`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `task_manager`.`users` (
  `id_u` INT(11) NOT NULL AUTO_INCREMENT,
  `login` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `lastname` VARCHAR(45) NOT NULL,
  `gender` ENUM('kobieta', 'mężczyzna') NOT NULL,
  `permission` TINYINT(4) NOT NULL,
  PRIMARY KEY (`id_u`),
  UNIQUE INDEX `login_UNIQUE` (`login` ASC))
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `task_manager`.`submission`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `task_manager`.`submission` (
  `users_id_u` INT(11) NOT NULL,
  `course_id_c` INT NOT NULL,
  `feed` ENUM('normalne', 'wegetariańskie', 'bezglutenowe') NOT NULL,
  `fv_decision` TINYINT NOT NULL,
  `fv_details` TEXT NULL,
  `confirm` TINYINT NULL DEFAULT 0,
  
  INDEX `fk_users_has_course_course1_idx` (`course_id_c` ASC),
  INDEX `fk_users_has_course_users_idx` (`users_id_u` ASC),
  CONSTRAINT `fk_users_has_course_users`
    FOREIGN KEY (`users_id_u`)
    REFERENCES `task_manager`.`users` (`id_u`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_users_has_course_course1`
    FOREIGN KEY (`course_id_c`)
    REFERENCES `task_manager`.`course` (`id_c`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
