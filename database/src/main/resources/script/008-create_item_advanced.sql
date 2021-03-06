CREATE TABLE ITEM_ADVANCED (
  ITEM NUMBER(*) CONSTRAINT PK_06 PRIMARY KEY,
  IMAGE BLOB,
  PARENT NUMBER(*),
  SPECIALIST_COMPANY VARCHAR2(32),
  SPECIALIST_EMAIL VARCHAR2(32),
  SPECIALIST_PHONE NUMBER(10),
  SPECIALIST_COST NUMBER(10) CONSTRAINT CH_02 CHECK (SPECIALIST_COST >= 0),
  CONSTRAINT FK_05 FOREIGN KEY (ITEM) REFERENCES ITEM(ID),
  CONSTRAINT FK_06 FOREIGN KEY (PARENT) REFERENCES ITEM_ADVANCED(ITEM)
);

CREATE TABLE ADDITIONAL_DETAIL (
  ITEM_ADVANCED NUMBER(*) NOT NULL,
  NAME VARCHAR2(32),
  DESCRIPTION VARCHAR2(128),
  COST NUMBER(10) CONSTRAINT CH_03 CHECK (COST >= 0),
  CONSTRAINT FK_07 FOREIGN KEY (ITEM_ADVANCED) REFERENCES ITEM_ADVANCED(ITEM)
);