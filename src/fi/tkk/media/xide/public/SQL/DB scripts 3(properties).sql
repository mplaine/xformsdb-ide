------------------------------------------------------Second volume: applications, pages, files-----------------------------------------

DROP TABLE properties_info;
DROP TABLE used_properties_map;
------------------------------------- Tables creation ------------------------------------- 

-- Stores properties information
CREATE TABLE properties_info (
	prop_id 		int PRIMARY KEY,		-- ID of the property
	prop_title		varchar(200), 			-- name of the propery
	description		text, 				-- property description
	is_obligatory		boolean,			-- is this property obligatory for the elelemt
	is_hidden		boolean,			-- is this property a hidden property (never showed for a user)
	is_never_editable	boolean,			-- is this property a never edited by user property ( like ID or date created)
	table_name		varchar(200),			-- name of the table where property is stored
	column_name		varchar(200),			-- name of the column where property is stored
	prop_code		varchar(200)			-- property code for the XIDE application
);


CREATE TABLE used_properties_map (
	prop_id 		int REFERENCES properties_info,	-- ID of the property
	user_id			varchar(200)			-- ID of the user
);

------------------------------------- Loading data into propeties_info table ------------------------------------- 

------------------------------------- Templates ------------------------------------- 
INSERT INTO properties_info VALUES (
0,
'Template id',
'ID of the temlate',
true, false, true,
'templates',
'template_id',
'ID');

INSERT INTO properties_info VALUES (
1,
'Template title',
'Title of the temlate',
true, false, false,
'templates',
'template_title',
'Title');

INSERT INTO properties_info VALUES (
2,
'Template description',
'Description of the temlate',
false, false, false,
'templates',
'description',
'Description');

INSERT INTO properties_info VALUES (
3,
'Template author',
'Author of the temlate',
false, false, true,
'templates',
'author',
'Author');

INSERT INTO properties_info VALUES (
4,
'Template creation date',
'Author of the temlate',
false, false, true,
'templates',
'creation_date',
'Date_Creation');

INSERT INTO properties_info VALUES (
5,
'Template type',
'Type of the temlate',
false, true, true,
'templates',
'type',
'Type');

------------------------------------------Applications
INSERT INTO properties_info VALUES (
6,
'Application id',
'ID of the Application',
true, true, true,
'applications',
'application_id',
'ID');

INSERT INTO properties_info VALUES (
7,
'Application title',
'Title of the Application',
true, false, false,
'applications',
'application_title',
'Title');


INSERT INTO properties_info VALUES (
8,
'Application descr',
'Descr of the Application',
false, false, false,
'applications',
'description',
'Description');

INSERT INTO properties_info VALUES (
9,
'Application author',
'Author of the Application',
false, false, true,
'applications',
'author',
'Author');

INSERT INTO properties_info VALUES (
10,
'Application url',
'url of the Application',
false, false, true,
'applications',
'url',
'URL');

INSERT INTO properties_info VALUES (
11,
'Application creation date',
'Creation date of the Application',
false, false, true,
'applications',
'creation_date',
'Date_Creation');

INSERT INTO properties_info VALUES (
12,
'Application modification date',
'Modification date of the Application',
false, false, true,
'applications',
'modification_date',
'Date_Modification');

INSERT INTO properties_info VALUES (
13,
'Application publishing date',
'publishing date of the Application',
false, false, true,
'applications',
'publishing_date',
'Date_Publication');

INSERT INTO properties_info VALUES (
14,
'Is public application',
'Is public  Application (shown to others)',
false, false, false,
'applications',
'is_public',
'Is_shown');

INSERT INTO properties_info VALUES (
15,
'Application status',
'Application status',
false, false, true,
'applications',
'status',
'Is_published');

INSERT INTO properties_info VALUES (
16,
'Reference URL',
'Reference URL for the application',
false, false, false,
'applications',
'ref_url',
'Reference_URL');
------------------------------------------------ Pages

INSERT INTO properties_info VALUES (
17,
'page id',
'ID of the page',
true, true, true,
'pages',
'page_id',
'ID');

INSERT INTO properties_info VALUES (
18,
'page title',
'Title of the page',
true, false, false,
'pages',
'page_title',
'Title');


INSERT INTO properties_info VALUES (
19,
'page descr',
'Descr of the page',
false, false, false,
'pages',
'description',
'Description');

INSERT INTO properties_info VALUES (
20,
'page creation date',
'Creation date of the page',
false, false, true,
'pages',
'creation_date',
'Date_Creation');

INSERT INTO properties_info VALUES (
21,
'page modification date',
'Modification date of the page',
false, false, true,
'pages',
'modification_date',
'Date_Modification');

INSERT INTO properties_info VALUES (
22,
'Application ID',
'Application ID',
false, true, true,
'pages',
'application_id',
'Application_id');


INSERT INTO properties_info VALUES (
23,
'Reference URL',
'Reference URL for the page',
false, false, false,
'pages',
'ref_url',
'Reference_URL');