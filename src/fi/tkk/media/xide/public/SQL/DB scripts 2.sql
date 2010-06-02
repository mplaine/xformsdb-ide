------------------------------------------------------Second volume: applications, pages, files-----------------------------------------

DROP TABLE page_files;
ALTER TABLE pages ADD COLUMN ref_url varchar(100);
UPDATE pages SET ref_url = '';
------------------------------------- Tables creation ------------------------------------- 

-- Stores application information
CREATE TABLE applications (
	application_id 		int PRIMARY KEY,		-- ID of the application
	application_title	varchar(200), 			-- title of the application
	description		text, 				-- application description
	author			varchar(100),			-- username of the application author
	url			varchar(200),			-- URL where the application is published
	rel_url 		varchar(100), 			-- relative url for the application (modified by user)
	creation_date		timestamp, 			-- date of creation 
	modification_date	timestamp, 			-- date of last modification 
	publishing_date		timestamp, 			-- date of publishing (last publishing)
	is_public		boolean, 			-- shows should application be displayable in the search list
	status			boolean				-- shows is application published (true) or not (false)
);


-- Stores page information
CREATE TABLE pages (
	page_id 		int PRIMARY KEY,		-- ID of the page
	page_title		varchar(200), 			-- title of the page
	description		text, 				-- page description
	creation_date		timestamp, 			-- date of creation 
	modification_date	timestamp, 			-- date of last modification 
	rel_url 		varchar(100), 			-- relative url for the page (modified by user)
	application_id 		int				-- link to the application
);

-- Stores page information
CREATE TABLE page_files (
	file_id 		int PRIMARY KEY,		-- ID of the file
	page_id 		int, 				-- link to the page
	source			varchar(300),			-- link to the source where file is saved
	type			int				-- type of the file content: 1 - Source code; 2 - CSS; 3 - Query
								--
);

------------------------------------- Loading data into applications table ------------------------------------- 

INSERT INTO applications VALUES (0,
'Test application',
'Just testing functionality',
'EvgeniaS',
'', '',
'2009-04-23 05:42:55', 
'2009-04-23 05:42:55',
null,
true,
false);

INSERT INTO applications VALUES (1,
'Contact management',
'Manage your contacts!',
'EvgeniaS',
'','',
'2009-04-24 17:37:59',
'2009-04-24 17:37:59',
null,
true,
false);

INSERT INTO applications VALUES (2,
'Blog archive',
'Famous blog application',
'EvgeniaS',
'','',
'2009-04-23 16:25:25', 
'2009-04-23 16:25:25',
null,
true,
false);

------------------------------------- Loading data into pages table ------------------------------------- 

-- Test app
INSERT INTO pages VALUES(0,
'page 1',
'page 1 descr',
'2009-04-23 16:25:25', 
'2009-04-23 16:25:25',
'',
0);

INSERT INTO pages VALUES(1,
'page 2',
'page 2 descr',
'2009-04-23 16:25:25', 
'2009-04-23 16:25:25',
'',
0);

INSERT INTO pages VALUES(2,
'page 3',
'page 3 descr',
'2009-04-23 16:25:25', 
'2009-04-23 16:25:25',
'',
0);

-- Contact management
INSERT INTO pages VALUES(3,
'Login page',
'Login page with login and password fields',
'2009-04-23 16:25:25', 
'2009-04-23 16:25:25',
'',
1);

INSERT INTO pages VALUES(4,
'Main page',
'View contacts here',
'2009-04-23 16:25:25', 
'2009-04-23 16:25:25',
'',
1);

-- Blog app
INSERT INTO pages VALUES(5,
'Main page',
'Main page of the blog app',
'2009-04-23 16:25:25', 
'2009-04-23 16:25:25',
'',
2);
