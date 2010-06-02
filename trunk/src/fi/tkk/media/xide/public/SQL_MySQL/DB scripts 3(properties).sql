
#------------------------------------- Tables creation ------------------------------------- 

#-- Stores properties information
CREATE TABLE properties_info (
	prop_id 		int AUTO_INCREMENT,		#-- ID of the property
	prop_title		varchar(200), 			#-- name of the propery
	description		text, 				#-- property description
	is_obligatory		boolean,			#-- is this property obligatory for the elelemt
	is_hidden		boolean,			#-- is this property a hidden property (never showed for a user)
	is_never_editable	boolean,			#-- is this property a never edited by user property ( like ID or date created)
	table_name		varchar(200),			#-- name of the table where property is stored
	column_name		varchar(200),			#-- name of the column where property is stored
	prop_code		varchar(200),			#-- property code for the XIDE application
	data_type		int,					#-- property data type 
	PRIMARY KEY (prop_id)
	);
#-- Data types are:
#--	0 - text
#-- 1 - boolean
#-- 2 - textarea
#-- 3 - system (like template_type: shouldn't be shown to a user)
#-- 4 - array (not really necessary in thie table)
#-- 5 - data




CREATE TABLE used_properties_map (
	prop_id 		int REFERENCES properties_info,	#-- ID of the property
	user_id			varchar(200)			#-- ID of the user
);

#------------------------------------- Loading data into propeties_info table ------------------------------------- 

#------------------------------------- Templates ------------------------------------- 

INSERT INTO properties_info (prop_title,	description, is_obligatory, is_hidden, 
							is_never_editable, table_name, column_name, prop_code, data_type) VALUES (
'Template ID',
'ID of the temlate',
true, false, true,
'templates',
'template_id',
'ID', 0);

INSERT INTO properties_info (prop_title,	description, is_obligatory, is_hidden, 
							is_never_editable, table_name, column_name, prop_code, data_type) 
							VALUES (
'Template title',
'Title of the temlate',
true, false, false,
'templates',
'template_title',
'Title', 0);

INSERT INTO properties_info (prop_title,	description, is_obligatory, is_hidden, 
							is_never_editable, table_name, column_name, prop_code, data_type) VALUES (
'Template description',
'Description of the temlate',
false, false, false,
'templates',
'description',
'Description', 0);

INSERT INTO properties_info  (prop_title,	description, is_obligatory, is_hidden, 
							is_never_editable, table_name, column_name, prop_code, data_type) VALUES (
'Template author',
'Author of the temlate',
false, false, true,
'templates',
'author',
'Author', 0);

INSERT INTO properties_info (prop_title,	description, is_obligatory, is_hidden, 
							is_never_editable, table_name, column_name, prop_code, data_type)  VALUES (
'Template creation date',
'Author of the temlate',
false, false, true,
'templates',
'creation_date',
'Date_Creation', 5);

INSERT INTO properties_info  (prop_title,	description, is_obligatory, is_hidden, 
							is_never_editable, table_name, column_name, prop_code, data_type) VALUES (
'Template type',
'Type of the temlate',
false, true, true,
'templates',
'template_type',
'Type', 3);

INSERT INTO properties_info  (prop_title,	description, is_obligatory, is_hidden, 
							is_never_editable, table_name, column_name, prop_code, data_type) VALUES (
'Do work',
'Shows is this a working template (can be dragged)',
false, false, false,
'templates',
'do_work',
'Do_Work', 1);

#------------------------------------------Applications
INSERT INTO properties_info  (prop_title,	description, is_obligatory, is_hidden, 
							is_never_editable, table_name, column_name, prop_code, data_type) VALUES (
'Application ID',
'ID of the Application',
true, true, true,
'applications',
'application_id',
'ID', 0);

INSERT INTO properties_info (prop_title,	description, is_obligatory, is_hidden, 
							is_never_editable, table_name, column_name, prop_code, data_type)  VALUES (
'Application title',
'Title of the Application',
true, false, false,
'applications',
'application_title',
'Title', 0);


INSERT INTO properties_info  (prop_title,	description, is_obligatory, is_hidden, 
							is_never_editable, table_name, column_name, prop_code, data_type) VALUES (
'Application description',
'Description of the Application',
false, false, false,
'applications',
'description',
'Description', 0);

INSERT INTO properties_info  (prop_title,	description, is_obligatory, is_hidden, 
							is_never_editable, table_name, column_name, prop_code, data_type) VALUES (
'Application author',
'Author of the Application',
false, false, true,
'applications',
'author',
'Author', 0);

INSERT INTO properties_info (prop_title,	description, is_obligatory, is_hidden, 
							is_never_editable, table_name, column_name, prop_code, data_type)  VALUES (
'Application url',
'url of the Application',
false, false, true,
'applications',
'url',
'URL', 0);

INSERT INTO properties_info (prop_title,	description, is_obligatory, is_hidden, 
							is_never_editable, table_name, column_name, prop_code, data_type)  VALUES (
'Application creation date',
'Creation date of the Application',
false, false, true,
'applications',
'creation_date',
'Date_Creation', 5);

INSERT INTO properties_info (prop_title,	description, is_obligatory, is_hidden, 
							is_never_editable, table_name, column_name, prop_code, data_type)  VALUES (
'Application modification date',
'Modification date of the Application',
false, false, true,
'applications',
'modification_date',
'Date_Modification', 5);

INSERT INTO properties_info (prop_title,	description, is_obligatory, is_hidden, 
							is_never_editable, table_name, column_name, prop_code, data_type)  VALUES (
'Application publishing date',
'publishing date of the Application',
false, false, true,
'applications',
'publishing_date',
'Date_Publication', 5);

INSERT INTO properties_info (prop_title,	description, is_obligatory, is_hidden, 
							is_never_editable, table_name, column_name, prop_code, data_type)  VALUES (
'Is public application',
'Is public  Application (shown to others)',
false, false, false,
'applications',
'is_public',
'Is_shown', 1);

INSERT INTO properties_info (prop_title,	description, is_obligatory, is_hidden, 
							is_never_editable, table_name, column_name, prop_code, data_type)  VALUES (
'Application is published',
'Application is published and can be accessed online',
false, false, true,
'applications',
'status',
'Is_published', 1);

INSERT INTO properties_info (prop_title,	description, is_obligatory, is_hidden, 
							is_never_editable, table_name, column_name, prop_code, data_type)  VALUES (
'Related URL for application',
'Related URL for the application',
true, false, true,
'applications',
'rel_url',
'Related_URL', 0);

INSERT INTO properties_info (prop_title,	description, is_obligatory, is_hidden, 
							is_never_editable, table_name, column_name, prop_code, data_type)  VALUES (
'Main page',
'Main page of the application',
false, false, false,
'applications',
'main_page',
'Main_Page', 3);

INSERT INTO properties_info (prop_title,	description, is_obligatory, is_hidden, 
							is_never_editable, table_name, column_name, prop_code, data_type)  VALUES (
'Is demo application',
'Is this applciation a demo application (shown to others, cannot be edited)',
false, false, true,
'applications',
'is_demo',
'Is_Demo', 1);
#------------------------------------------------ Pages

INSERT INTO properties_info (prop_title,	description, is_obligatory, is_hidden, 
							is_never_editable, table_name, column_name, prop_code, data_type)  VALUES (
'Page ID',
'ID of the page',
true, true, true,
'pages',
'page_id',
'ID', 0);

INSERT INTO properties_info (prop_title,	description, is_obligatory, is_hidden, 
							is_never_editable, table_name, column_name, prop_code, data_type)  VALUES (
'Page title',
'Title of the page',
true, false, false,
'pages',
'page_title',
'Title', 0);


INSERT INTO properties_info  (prop_title,	description, is_obligatory, is_hidden, 
							is_never_editable, table_name, column_name, prop_code, data_type) VALUES (
'Page Descrescription',
'Descr of the page',
false, false, false,
'pages',
'description',
'Description', 0);

INSERT INTO properties_info  (prop_title,	description, is_obligatory, is_hidden, 
							is_never_editable, table_name, column_name, prop_code, data_type) VALUES (
'Page creation date',
'Creation date of the page',
false, false, true,
'pages',
'creation_date',
'Date_Creation', 5);

INSERT INTO properties_info (prop_title,	description, is_obligatory, is_hidden, 
							is_never_editable, table_name, column_name, prop_code, data_type)  VALUES (
'Page modification date',
'Modification date of the page',
false, false, true,
'pages',
'modification_date',
'Date_Modification', 5);

INSERT INTO properties_info (prop_title,	description, is_obligatory, is_hidden, 
							is_never_editable, table_name, column_name, prop_code, data_type)  VALUES (
'Application ID',
'Application ID',
false, true, true,
'pages',
'application_id',
'Application_id', 0);


INSERT INTO properties_info (prop_title,	description, is_obligatory, is_hidden, 
							is_never_editable, table_name, column_name, prop_code, data_type)  VALUES (
'Related URL',
'Related URL for the page',
true, false, true,
'pages',
'rel_url',
'Related_URL', 0);

INSERT INTO properties_info (prop_title,	description, is_obligatory, is_hidden, 
							is_never_editable, table_name, column_name, prop_code, data_type)  VALUES (
'Folder path',
'Path of the folder which represents files of this application on a server',
true, true, true,
'applications',
'folder_path',
'Folder_Path', 0);

INSERT INTO properties_info (prop_title,	description, is_obligatory, is_hidden, 
							is_never_editable, table_name, column_name, prop_code, data_type)  VALUES (
'Folder path',
'Path of the folder which represents files of this template on a server',
true, true, true,
'templates',
'folder_path',
'Folder_Path', 0);