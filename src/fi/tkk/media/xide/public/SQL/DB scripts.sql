------------------------------------- Help ------------------------------------- 

SELECT * FROM templates;

UPDATE templates
    SET type = 1
    WHERE template_id = 'component_app_2_id';
    
------------------------------------- Database creation ------------------------------------- 

CREATE DATABASE xide_db
  WITH OWNER = postgres
       ENCODING = 'UTF8';
COMMENT ON DATABASE xide_db IS 'Database contains information needed for XIDE system';

------------------------------------- Tables creation ------------------------------------- 

-- Stores templates information
CREATE TABLE templates (
	template_id 		varchar(200) PRIMARY KEY,	-- ID of the template
	template_title		varchar(200), 			-- title of the template
	description		text, 				-- template description
	author			varchar(100),			-- username of the template author
	creation_date		timestamp, 			-- date of creation (publishing)
	type			int				-- type of the template: 1 - Applicaiton; 2 - Page; 3 - Component
);

-- Stores tags description
CREATE TABLE tags (
	tag_id 			int PRIMARY KEY,		-- ID of the tag
	title			varchar(200), 			-- title of the tag
	description		text 				-- tag description
);

-- Stores many-to-many relationship between  tags and templates
CREATE TABLE used_tags_map (
	tag_id int REFERENCES tags,			-- link to the tag id
	template_id varchar(200) REFERENCES templates		-- link to the template id
);

CREATE TABLE templates_files (
	template_file_id 	int PRIMARY KEY,			-- ID of the file
	template_id  		varchar(200) REFERENCES templates,	-- link to the template
	source			varchar(500)				-- path to the source code file
);


------------------------------------- Loading data into templates table ------------------------------------- 

-- Login
INSERT INTO templates VALUES ('component_0_id',
'Simple login component', 
'This is simple login component which offers user to enter his/her login and password. Authententification query is included',
'evgenias',
'2009-04-22 09:16:16',
3);
-- Login with email
INSERT INTO templates VALUES ('component_1_id',
'Login component with email', 
'This is advanced login component which offers user to enter his/her login, password and e-mail. Query is not included',
'evgenias',
'2009-04-23 18:56:02',
3);
-- Surway
INSERT INTO templates VALUES ('component_2_id',
'Simple survey', 
'This is a simple survey component which has a slot for adding questions and a commint button to commit the survey answers. Require query modification!!! Basic Query is included',
'evgenias',
'2009-04-22 11:20:12',
3);
--
INSERT INTO templates VALUES ('component_3_id',
'Yes/No question', 
'Simple question with 2 variants of answer. Question and answers texts are designed as parameters',
'evgenias',
'2009-04-22 15:46:21',
3);

INSERT INTO templates VALUES ('component_4_id',
'Simple question', 
'Simple question and string input field to enter the answer. Question line is designed as parameter',
'evgenias',
'2009-04-24 12:15:32',
3);

INSERT INTO templates VALUES ('component_5_id',
'Check box question', 
'Simple question and check box field to select or unselect. Question line is designed as parameter',
'evgenias',
'2009-04-23 05:42:55',
3);

INSERT INTO templates VALUES ('component_6_id',
'Range question', 
'Simple question and range field for answering. Question text, min and max range values and the step are parameters. Query is not included',
'evgenias',
'2009-04-23 16:25:25',
3);

INSERT INTO templates VALUES ('component_7_id',
'Blog', 
'Blog component. Designed to be used in WSC system as a blog. Queries are included',
'evgenias',
'2009-04-24 17:37:59',
3);


INSERT INTO templates VALUES ('component_app_1_id',
'Event Manager', 
'This is simple system helps you to manage your events',
'evgenias',
'2009-04-22 11:19:15',
1);

INSERT INTO templates VALUES ('component_app_2_id',
'Personal Web Site', 
'Includes personal biography page, picture library page, simple forum',
'evgenias',
'2009-04-21 18:35:08',
1);

INSERT INTO templates VALUES ('component_page_1_id',
'Personal biography web page', 
'Includes layout and several necessary components which forms personal biography page',
'evgenias',
'2009-04-23 12:16:51',
2);

------------------------------------- Loading data into tags table ------------------------------------- 

INSERT INTO tags VALUES( 
0,
'Atomic',
'no descr');

INSERT INTO tags VALUES( 
1,
'Component Template',
'no descr');

INSERT INTO tags VALUES( 
2,
'Application Template',
'no descr');
INSERT INTO tags VALUES( 
3,
'Complex',
'no descr');
INSERT INTO tags VALUES( 
4,
'Multi-Page',
'no descr');
INSERT INTO tags VALUES( 
5,
'Ready-To-Use',
'no descr');
INSERT INTO tags VALUES( 
6,
'Page template',
'no descr');

------------------------------------- Loading data into used_tags_map table ------------------------------------- 
-- atomic
INSERT INTO used_tags_map VALUES( 
0,
'component_3_id');
INSERT INTO used_tags_map VALUES( 
0,
'component_4_id');
INSERT INTO used_tags_map VALUES( 
0,
'component_5_id');
INSERT INTO used_tags_map VALUES( 
0,
'component_6_id');

-- component
INSERT INTO used_tags_map VALUES( 
1,
'component_0_id');
INSERT INTO used_tags_map VALUES( 
1,
'component_1_id');
INSERT INTO used_tags_map VALUES( 
1,
'component_2_id');
INSERT INTO used_tags_map VALUES( 
1,
'component_3_id');
INSERT INTO used_tags_map VALUES( 
1,
'component_4_id');
INSERT INTO used_tags_map VALUES( 
1,
'component_5_id');
INSERT INTO used_tags_map VALUES( 
1,
'component_6_id');
INSERT INTO used_tags_map VALUES( 
1,
'component_7_id');
-- application
INSERT INTO used_tags_map VALUES( 
2,
'component_app_1_id');
INSERT INTO used_tags_map VALUES( 
2,
'component_app_2_id');
-- page
INSERT INTO used_tags_map VALUES( 
6,
'component_page_1_id');
select * from used_tags_map;

------------------------------------- Loading data into templates_files table ------------------------------------- 
INSERT INTO templates_files VALUES( 
0,
'component_0_id',
'sourcecodes/component0.xml');

INSERT INTO templates_files VALUES( 
1,
'component_1_id',
'sourcecodes/component1.xml');

INSERT INTO templates_files VALUES( 
2,
'component_2_id',
'sourcecodes/component2.xml');

INSERT INTO templates_files VALUES( 
3,
'component_3_id',
'sourcecodes/component3.xml');

INSERT INTO templates_files VALUES( 
4,
'component_4_id',
'sourcecodes/component4.xml');

INSERT INTO templates_files VALUES( 
5,
'component_5_id',
'sourcecodes/component5.xml');

INSERT INTO templates_files VALUES( 
6,
'component_6_id',
'sourcecodes/component6.xml');

