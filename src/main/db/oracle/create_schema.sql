
--
-- CREATES IMPORT HISTORY TABLE
--
CREATE TABLE ref_import_history (
   objid 					integer 			not null,
   imported_filename 		varchar2(128) 		not null,
   import_date 				date	 			not null,
   custom_1 				varchar2(30)				,
   custom_2 				varchar2(30)				,
   custom_3 				varchar2(30)
);

ALTER TABLE ref_import_history ADD CONSTRAINT ref_import_history_pk PRIMARY KEY (objid);

--
-- CREATES CARRIER COMPANIES TABLE
--
CREATE TABLE ref_carrier_company (
   objid 					integer 			not null,
   import_id 				integer 			not null,
   country_code 			varchar2(5) 		not null,
   national_code 			varchar2(5) 		not null,
   marketing_name 			varchar2(64)				,
   institutional_name 		varchar2(128)				,
   registration_number 		varchar2(32)				,
   custom_1 				varchar2(30)				,
   custom_2 				varchar2(30)				,
   custom_3 				varchar2(30)
);

ALTER TABLE ref_carrier_company ADD CONSTRAINT ref_carrier_company_pk  PRIMARY KEY (objid);
ALTER TABLE ref_carrier_company ADD CONSTRAINT ref_carrier_company_fk1 FOREIGN KEY (import_id) REFERENCES ref_import_history (objid);
ALTER TABLE ref_carrier_company ADD CONSTRAINT ref_carrier_company_uk1 UNIQUE (country_code, national_code);

--
-- CREATES CARRIERS ADDRESSES TABLE
--
CREATE TABLE ref_carrier_address (
   objid 					integer 			not null,
   import_id 				integer				not null,
   carrier_id 				integer				not null,
   street_name 				varchar2(128)				,
   street_number 			varchar2(20)				,
   complement_1 			varchar2(128)				,
   complement_2 			varchar2(128)				,
   city_name 				varchar2(64)				,
   state_code 				varchar2(10)				,
   country_name 			varchar2(32)				,
   zip_code 				varchar2(20)				,
   custom_1 				varchar2(30)				,
   custom_2 				varchar2(30)				,
   custom_3 				varchar2(30)
);

ALTER TABLE ref_carrier_address ADD CONSTRAINT ref_carrier_address_pk  PRIMARY KEY (objid);
ALTER TABLE ref_carrier_address ADD CONSTRAINT ref_carrier_address_fk1 FOREIGN KEY (import_id) REFERENCES ref_import_history (objid);
ALTER TABLE ref_carrier_address ADD CONSTRAINT ref_carrier_address_fk2 FOREIGN KEY (carrier_id) REFERENCES ref_carrier_company (objid);

--
-- CREATES FISCAL OPERATION CODES TABLE
--
CREATE TABLE ref_fiscal_opcode (
   objid 					integer 			not null,
   import_id 				integer 			not null,
   operation_code 			varchar2(10) 		not null,
   operation_description 	varchar2(64)				,
   custom_1 				varchar2(30)				,
   custom_2 				varchar2(30)				,
   custom_3 				varchar2(30)
);

ALTER TABLE ref_fiscal_opcode ADD CONSTRAINT ref_fiscal_opcode_pk  PRIMARY KEY (objid);
ALTER TABLE ref_fiscal_opcode ADD CONSTRAINT ref_fiscal_opcode_fk1 FOREIGN KEY (import_id) REFERENCES ref_import_history (objid);
ALTER TABLE ref_fiscal_opcode ADD CONSTRAINT ref_fiscal_opcode_uk1 UNIQUE (operation_code);

--
-- CREATES FISCAL OPERATION CODES x CARRIERS RELATIONSHIP TABLE
--
CREATE TABLE ref_carrier_opcodes_allowed (
   carrier_id 				integer 			not null,
   fiscal_opcode_id 		integer				not null
);

ALTER TABLE ref_carrier_opcodes_allowed ADD CONSTRAINT ref_carrier_opcodes_allowed_pk  PRIMARY KEY (carrier_id, fiscal_opcode_id);
ALTER TABLE ref_carrier_opcodes_allowed ADD CONSTRAINT ref_carrier_opcodes_allowed_fk1 FOREIGN KEY (carrier_id) REFERENCES ref_carrier_company (objid);
ALTER TABLE ref_carrier_opcodes_allowed ADD CONSTRAINT ref_carrier_opcodes_allowed_fk2 FOREIGN KEY (fiscal_opcode_id) REFERENCES ref_fiscal_opcode (objid);
