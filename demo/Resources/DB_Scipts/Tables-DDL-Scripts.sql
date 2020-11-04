DROP TABLE purchase_order_det;
DROP TABLE vendor_receipt_map;
DROP TABLE vendor_det;
DROP TABLE product_igst_map;
DROP TABLE product_cgst_map;
DROP TABLE product_sgst_map;
DROP TABLE igst_rates;
DROP TABLE sgst_rates;
DROP TABLE cgst_rates;
DROP TABLE return_order;
DROP TABLE order_customer_map;
DROP TABLE pending_payment;
DROP TABLE customer_det;
DROP TABLE order_det;
DROP TABLE product_category_map;
DROP TABLE category_det;
DROP TABLE product_discount_map;
DROP TABLE product_det;

CREATE TABLE `product_det` (
  `PRODUCT_ID` INT NOT NULL,
  `PROUCT_NAME` VARCHAR(1000) NOT NULL,
  `DISP_NAME` VARCHAR(500) NOT NULL,
  `DESC` VARCHAR(1000) NULL,
  `RF1` INT NULL,
  `RF2` INT NULL,
  `RF3` INT NULL,
  `RF4` INT NULL,
  `RF5` INT NULL,
  PRIMARY KEY (`PRODUCT_ID`),
  UNIQUE INDEX `DISP_NAME_UNIQUE` (`DISP_NAME` ASC));


CREATE TABLE `product_discount_map` (
  `PRODUCT_ID` INT NOT NULL,
  `DISCOUNT` DOUBLE NULL,
  `EFF_DATE_SKEY` INT NOT NULL,
  `END_DATE_SKEY` INT NOT NULL,
  `LAST_UPDATE_TIME` DATETIME NULL,
  `RF1` INT NULL,
  `RF2` INT NULL,
  PRIMARY KEY (`PRODUCT_ID`, `EFF_DATE_SKEY`, `END_DATE_SKEY`),
  CONSTRAINT `FK2`
    FOREIGN KEY (`PRODUCT_ID`)
    REFERENCES `product_det` (`PRODUCT_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


CREATE TABLE `category_det` (
  `CATEGORY_ID` INT NOT NULL,
  `NAME` VARCHAR(500) NULL,
  `DISP_NAME` VARCHAR(500) NULL,
  `DESC` VARCHAR(1000) NULL,
  `RF1` INT NULL,
  `RF2` INT NULL,
  PRIMARY KEY (`CATEGORY_ID`),
  UNIQUE INDEX `NAME_UNIQUE` (`NAME` ASC));


CREATE TABLE `product_category_map` (
  `PRODUCT_ID` INT NOT NULL,
  `CATEGORY_ID` INT NOT NULL,
  `EFF_DATE_SKEY` INT NOT NULL,
  `END_DATE_SKEY` INT NOT NULL,
  `LAST_UPDATE_TIME` DATETIME NULL,
  `RF1` INT NULL,
  `RF2` INT NULL,
  `RF3` INT NULL,
  PRIMARY KEY (`PRODUCT_ID`, `CATEGORY_ID`, `EFF_DATE_SKEY`, `END_DATE_SKEY`),
  INDEX `FK5_idx` (`CATEGORY_ID` ASC) ,
  CONSTRAINT `FK4`
    FOREIGN KEY (`PRODUCT_ID`)
    REFERENCES `product_det` (`PRODUCT_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK5`
    FOREIGN KEY (`CATEGORY_ID`)
    REFERENCES `category_det` (`CATEGORY_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


CREATE TABLE `order_det` (
  `ORDER_ID` INT NOT NULL,
  `PRODUCT_ID` INT NOT NULL,
  `QUANTITY` INT NULL,
  `RF1` INT NULL,
  `RF2` INT NULL,
  `RF3` INT NULL,
  PRIMARY KEY (`ORDER_ID`, `PRODUCT_ID`),
  INDEX `FK7_idx` (`PRODUCT_ID` ASC),
  CONSTRAINT `FK7`
    FOREIGN KEY (`PRODUCT_ID`)
    REFERENCES `product_det` (`PRODUCT_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


CREATE TABLE `customer_det` (
  `CUSTOMER_ID` INT NOT NULL,
  `NAME` VARCHAR(500) NULL,
  `PHONE` VARCHAR(12) NULL,
  `EMAIL` VARCHAR(500) NULL,
  `ADDRESS` VARCHAR(500) NULL,
  `RF1` INT NULL,
  `RF2` INT NULL,
  `RF3` INT NULL,
  PRIMARY KEY (`CUSTOMER_ID`,`PHONE`));


CREATE TABLE `pending_payment` (
  `ORDER_ID` INT NOT NULL,
  `PAID` DOUBLE NULL,
  `UNPAID` DOUBLE NULL,
  `RF1` INT NULL,
  `RF2` INT NULL,
  PRIMARY KEY (`ORDER_ID`),
  CONSTRAINT `FK8`
    FOREIGN KEY (`ORDER_ID`)
    REFERENCES `order_det` (`ORDER_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


CREATE TABLE `order_customer_map` (
  `ORDER_ID` INT NOT NULL,
  `CUSTOMET_ID` INT NOT NULL,
  `DATE_SKEY` INT NOT NULL,
  `RF1` INT NULL,
  `RF2` INT NULL,
  PRIMARY KEY (`ORDER_ID`, `CUSTOMET_ID`, `DATE_SKEY`),
  INDEX `FK11_idx` (`CUSTOMET_ID` ASC),
  CONSTRAINT `FK10`
    FOREIGN KEY (`ORDER_ID`)
    REFERENCES `order_det` (`ORDER_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK11`
    FOREIGN KEY (`CUSTOMET_ID`)
    REFERENCES `customer_det` (`CUSTOMER_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


CREATE TABLE `return_order` (
  `NEW_ORDER_ID` INT NOT NULL,
  `RETURN_TYPE` CHAR NULL,
  `OLD_ORDER_ID` INT NOT NULL,
  `DATE_SKEY` INT NOT NULL,
  `RF1` INT NULL,
  `RF2` INT NULL,
  PRIMARY KEY (`NEW_ORDER_ID`, `DATE_SKEY`, `OLD_ORDER_ID`),
  INDEX `FK12_idx` (`NEW_ORDER_ID` ASC, `OLD_ORDER_ID` ASC) ,
  CONSTRAINT `FK12`
    FOREIGN KEY (`NEW_ORDER_ID`)
    REFERENCES `order_det` (`ORDER_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK13`
    FOREIGN KEY (`OLD_ORDER_ID`)
    REFERENCES `order_det` (`ORDER_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

ALTER TABLE product_det CHANGE `prouct_name` `PRODUCT_NAME` varchar(1000);
ALTER TABLE product_det CHANGE `DESC` `PRODUCT_DESC` varchar(1000);
ALTER TABLE category_det CHANGE `DESC` `CATEGORY_DESC` varchar(1000);


CREATE TABLE `cgst_rates` ( 
	`GST_RATE`	double NULL,
	`NAME`    	varchar(100) NULL,
	PRIMARY KEY(`GST_RATE`)
);


CREATE TABLE `sgst_rates` ( 
	`GST_RATE`	double NULL,
	`NAME`    	varchar(100) NULL,
	PRIMARY KEY(`GST_RATE`)
);


CREATE TABLE `igst_rates` ( 
	`GST_RATE`	double NULL,
	`NAME`    	varchar(100) NULL,
	PRIMARY KEY(`GST_RATE`)
);

ALTER TABLE product_det CHANGE `RF1` `UNIT` varchar(200);


CREATE TABLE `product_sgst_map` ( 
	`PRODUCT_ID`      	INT NULL,
	`GST_RATE`        	double NULL,
	`EFF_DATE_SKEY`   	integer(11) NULL,
	`END_DATE_SKEY`   	integer(11) NULL,
	`LAST_UPDATE_TIME`	datetime NULL,
	PRIMARY KEY(`PRODUCT_ID`,`GST_RATE`,`EFF_DATE_SKEY`,`END_DATE_SKEY`)
);
ALTER TABLE `product_sgst_map`
	ADD CONSTRAINT `FK16`
	FOREIGN KEY(`PRODUCT_ID`)
	REFERENCES `product_det`(`PRODUCT_ID`);


CREATE TABLE `product_cgst_map` ( 
	`PRODUCT_ID`      	INT NULL,
	`GST_RATE`        	double NULL,
	`EFF_DATE_SKEY`   	integer(11) NULL,
	`END_DATE_SKEY`   	integer(11) NULL,
	`LAST_UPDATE_TIME`	datetime NULL,
	PRIMARY KEY(`PRODUCT_ID`,`GST_RATE`,`EFF_DATE_SKEY`,`END_DATE_SKEY`)
);
ALTER TABLE `product_cgst_map`
	ADD CONSTRAINT `FK15`
	FOREIGN KEY(`PRODUCT_ID`)
	REFERENCES `product_det`(`PRODUCT_ID`);


CREATE TABLE `product_igst_map` ( 
	`PRODUCT_ID`      	INT NULL,
	`GST_RATE`        	double NULL,
	`EFF_DATE_SKEY`   	integer(11) NULL,
	`END_DATE_SKEY`   	integer(11) NULL,
	`LAST_UPDATE_TIME`	datetime NULL,
	PRIMARY KEY(`PRODUCT_ID`,`GST_RATE`,`EFF_DATE_SKEY`,`END_DATE_SKEY`)
);
ALTER TABLE `product_igst_map`
	ADD CONSTRAINT `FK`
	FOREIGN KEY(`PRODUCT_ID`)
	REFERENCES `product_det`(`PRODUCT_ID`);


CREATE TABLE `vendor_det` ( 
	`VENDOR_ID`  	INT NOT NULL,
	`VENDOR_NAME`	varchar(700) NOT NULL,
	`CITY`       	varchar(500) NULL,
	`DISTRICT`   	varchar(500) NULL,
	`STATE`      	varchar(500) NULL,
	`PHONE`      	varchar(12) NULL,
	`EMAIL`      	varchar(500) NULL,
	`GST_NO`     	integer(11) NULL,
	`RF1`        	integer(11) NULL,
	`RF2`        	integer(11) NULL,
	`RF3`        	integer(11) NULL,
	PRIMARY KEY(`VENDOR_ID`)
);


CREATE TABLE `vendor_receipt_map` ( 
	`VENDOR_ID` 	INT NULL,
	`INVOICE_ID`	varchar(200) NULL,
	`DATE_SKEY`  	integer(11) NULL,
	PRIMARY KEY(`VENDOR_ID`,`INVOICE_ID`,`DATE_SKEY`)
);


CREATE TABLE `purchase_order_det` ( 
	`PRODUCT_ID` 	INT NOT NULL,
	`INVOICE_ID` 	varchar(200) NULL,
	`BATCH_NO`   	varchar(200) NULL,
	`QUANTITY`   	integer(11) NULL,
	`PRICE`      	double NULL,
	`COST`       	double NULL,
	`GST`        	double NULL,
	`MFG_DATE`   	datetime NULL,
	`EXP_DATE`   	datetime NULL,
	`BEST_BEFORE`	varchar(300) NULL,
	`RF1`        	integer(11) NULL,
	`RF2`        	integer(11) NULL,
	`RF3`        	integer(11) NULL,
	`RF4`        	integer(11) NULL,
	`RF5`        	integer(11) NULL,
	PRIMARY KEY(`PRODUCT_ID`,`INVOICE_ID`,`BATCH_NO`)
);

ALTER TABLE `purchase_order_det`
	ADD CONSTRAINT `FK19`
	FOREIGN KEY(`PRODUCT_ID`)
	REFERENCES `product_det`(`PRODUCT_ID`);



