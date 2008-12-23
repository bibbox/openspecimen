--Rename old column in  DYEXTN_CONTAINER
EXEC sp_rename 

    @objname = 'DYEXTN_CONTAINER.ENTITY_ID', 

    @newname = 'ABSTRACT_ENTITY_ID', 

    @objtype = 'COLUMN'

ALTER TABLE DYEXTN_CONTAINER ALTER COLUMN ABSTRACT_ENTITY_ID numeric(20);

--Add new column to  DYEXTN_CONTAINER
ALTER TABLE DYEXTN_CONTAINER ADD ADD_CAPTION NUMERIC (1) DEFAULT  NULL; 

--Update column set all value to 1
UPDATE   DYEXTN_CONTAINER SET ADD_CAPTION = 1;

--Insert data in  new table  DYEXTN_ABSTRACT_ENTITY
INSERT   INTO DYEXTN_ABSTRACT_ENTITY(ID) SELECT  IDENTIFIER ID FROM DYEXTN_ENTITY; 

--Insert data in  new table DYEXTN_USERDEF_DE_VALUE_REL
INSERT INTO DYEXTN_USERDEF_DE_VALUE_REL(USER_DEF_DE_ID,PERMISSIBLE_VALUE_ID) SELECT USER_DEF_DE_ID ,IDENTIFIER  PERMISSIBLE_VALUE_ID FROM
DYEXTN_PERMISSIBLE_VALUE WHERE USER_DEF_DE_ID IS NOT NULL AND IDENTIFIER  IS NOT NULL ; 

--Drop this column as  per new model
ALTER TABLE DYEXTN_PERMISSIBLE_VALUE DROP COLUMN USER_DEF_DE_ID;  

--Add column as per new model
ALTER TABLE DYEXTN_PERMISSIBLE_VALUE ADD CATEGORY_ATTRIBUTE_ID numeric(20) default NULL; 

--Add column as per new model
ALTER TABLE DYEXTN_ENTITY ADD ENTITY_GROUP_ID numeric(20) default NULL;

--populate DYEXTN_ENTITY table with entity_group_id record as per table DYEXTN_ENTITY_GROUP_REL
UPDATE DYEXTN_ENTITY  SET ENTITY_GROUP_ID = (  SELECT ENTITY_GROUP_ID    FROM DYEXTN_ENTITY_GROUP_REL   WHERE DYEXTN_ENTITY_GROUP_REL.ENTITY_ID = DYEXTN_ENTITY.IDENTIFIER ) ;
 
--drop unnecessary table DYEXTN_ENTITY_GROUP_REL as its not in new model.
DROP TABLE DYEXTN_ENTITY_GROUP_REL;

--ALTER TABLE ADD TWO COLUMN
ALTER TABLE DYEXTN_COLUMN_PROPERTIES ADD CATEGORY_ATTRIBUTE_ID numeric(20),CONSTRAINT_NAME varchar(255);

--ALTER TABLE ADD THREE COLUMN
ALTER TABLE DYEXTN_CONSTRAINT_PROPERTIES ADD SRC_CONSTRAINT_NAME varchar(255),TARGET_CONSTRAINT_NAME varchar(255),CATEGORY_ASSOCIATION_ID numeric(20);

INSERT   INTO DYEXTN_BASE_ABSTRACT_ATTRIBUTE(IDENTIFIER)   SELECT IDENTIFIER  FROM DYEXTN_ATTRIBUTE;

--Rename column as per new model
EXEC sp_rename 

    @objname = 'DYEXTN_CONTROL.ABSTRACT_ATTRIBUTE_ID', 

    @newname = 'BASE_ABST_ATR_ID', 

    @objtype = 'COLUMN'

ALTER TABLE DYEXTN_CONTROL ALTER COLUMN BASE_ABST_ATR_ID numeric(20);

--ADD COLUMN TO DYEXTN_DATA_ELEMENT
ALTER TABLE DYEXTN_DATA_ELEMENT ADD CATEGORY_ATTRIBUTE_ID numeric(20) default NULL;

--ALTER COLUMN RENAME IT
EXEC sp_rename 

    @objname = 'DYEXTN_TABLE_PROPERTIES.ENTITY_ID', 

    @newname = 'ABSTRACT_ENTITY_ID', 

    @objtype = 'COLUMN'

ALTER TABLE DYEXTN_TABLE_PROPERTIES ALTER COLUMN ABSTRACT_ENTITY_ID numeric(20);

--ADD COLUMN TO DYEXTN_TABLE_PROPERTIES
ALTER TABLE DYEXTN_TABLE_PROPERTIES ADD CONSTRAINT_NAME varchar(255);

--Insert data from DYEXTN_CONTAINMENT_CONTROL to DYEXTN_ABSTR_CONTAIN_CTR as per new model
INSERT   INTO  DYEXTN_ABSTR_CONTAIN_CTR(IDENTIFIER,CONTAINER_ID)   SELECT IDENTIFIER ,DISPLAY_CONTAINER_ID CONTAINER_ID FROM
DYEXTN_CONTAINMENT_CONTROL WHERE IDENTIFIER IS NOT NULL AND DISPLAY_CONTAINER_ID  IS NOT NULL ;

--DROP UNREQUIRED COLUMN FROM  DYEXTN_CONTAINMENT_CONTROL
ALTER TABLE DYEXTN_CONTAINMENT_CONTROL DROP COLUMN  DISPLAY_CONTAINER_ID;

--Add column to table DYEXTN_DATEPICKER
ALTER TABLE DYEXTN_DATEPICKER ADD DATE_VALUE_TYPE varchar(255) default NULL;

--Add column to table DYEXTN_CONTROL
ALTER TABLE DYEXTN_CONTROL ADD READ_ONLY numeric(1) default NULL;

--Insert into id generator starting count value
INSERT INTO DYEXTN_ID_GENERATOR( ID,NEXT_AVAILABLE_ID) VALUES(1,1900);

--Insert into dyextn_rule column CATEGORY_ATTR_ID
ALTER TABLE DYEXTN_RULE ADD CATEGORY_ATTR_ID numeric(20) default NULL;

--Insert into dyextn_rule column IS_IMPLICIT
ALTER TABLE DYEXTN_RULE ADD IS_IMPLICIT BIT;  

ALTER TABLE DE_FILE_ATTR_RECORD_VALUES ALTER COLUMN FILE_CONTENT IMAGE;  

ALTER TABLE DE_OBJECT_ATTR_RECORD_VALUES ALTER COLUMN OBJECT_CONTENT IMAGE; 

ALTER TABLE DYEXTN_PRIMITIVE_ATTRIBUTE DROP COLUMN  IS_COLLECTION; 

ALTER TABLE DYEXTN_ASSOCIATION ADD IS_COLLECTION BIT default 0;

ALTER TABLE DYEXTN_LIST_BOX ADD NO_OF_ROWS integer;

ALTER TABLE DYEXTN_STRING_CONCEPT_VALUE ALTER COLUMN VALUE varchar (4000); 

ALTER TABLE DYEXTN_CONTAINER MODIFY CAPTION varchar(800);
ALTER TABLE DYEXTN_CONTROL MODIFY CAPTION varchar(800);
ALTER TABLE DYEXTN_CONTROL ADD HEADING varchar(255);

ALTER TABLE DYEXTN_USERDEFINED_DE ADD IS_ORDERED BIT default 1;
