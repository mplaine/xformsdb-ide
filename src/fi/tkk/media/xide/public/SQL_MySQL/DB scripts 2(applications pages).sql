#------------------------------------------------------Second volume: applications, pages, files-----------------------------------------

#DROP TABLE page_files;
#ALTER TABLE pages ADD COLUMN ref_url varchar(100);
#UPDATE pages SET ref_url = '';
#------------------------------------- Tables creation ------------------------------------- 

#-- Stores application information
CREATE TABLE applications (
	application_id 		int AUTO_INCREMENT,		#-- ID of the application
	application_title	varchar(200), 			#-- title of the application
	description		text, 				#-- application description
	author			varchar(100),			#-- username of the application author
	url			varchar(200),			#-- URL where the application is published
	rel_url 		varchar(100), 			#-- relative url for the application (modified by user)
	creation_date		timestamp, 			#-- date of creation 
	modification_date	timestamp, 			#-- date of last modification 
	publishing_date		timestamp, 			#-- date of publishing (last publishing)
	is_public		boolean, 			#-- shows should application be displayable in the search list
	status			boolean,				#-- shows is application published (true) or not (false)
	folder_path varchar(500), 		        # path to the folder which contains files related to the application
	main_page int,						# id of the page which is the main page
	is_demo			boolean,			# indicates whether this applciation is a demo application (showed to everybody, cannot be edited)
	PRIMARY KEY (application_id)
);


#-- Stores page information
CREATE TABLE pages (
	page_id 		int AUTO_INCREMENT ,		#-- ID of the page
	page_title		varchar(200), 			#-- title of the page
	description		text, 				#-- page description
	creation_date		timestamp, 			#-- date of creation 
	modification_date	timestamp, 			#-- date of last modification 
	rel_url 		varchar(100), 			#-- relative url for the page (modified by user)
	application_id 		int REFERENCES applications,				#-- link to the application
	PRIMARY KEY (page_id)
);

#-- Stores page information
CREATE TABLE page_files (
	file_id 		int AUTO_INCREMENT,		#-- ID of the file
	page_id 		int, 				#-- link to the page
	source			varchar(300),			#-- link to the source where file is saved
	type			int,				#-- type of the file content: 1 - Source code; 2 - CSS; 3 - Query
	PRIMARY KEY (file_id)
);

#------------------------------------- Loading data into applications table ------------------------------------- 

INSERT INTO applications (application_title, description, author, url, rel_url,
creation_date, modification_date, publishing_date, is_public, status, folder_path) 
VALUES(
'News reader',
'Shows news from the given feed',
'jk',
'', '',
'2009-07-28 05:42:55', 
0,
0,
true,
false,
'users/markku/news_widget');

#INSERT INTO applications (application_title, description, author, url, rel_url,
#creation_date, modification_date, publishing_date, is_public, status, folder_path) VALUES (
#'Contact management',
#'Manage your contacts!',
#'EvgeniaS',
#'','',
#'2009-04-24 17:37:59',
#0,
#0,
#true,
#false, 
#'users/markku/contacts/');

#INSERT INTO applications (application_title, description, author, url, rel_url,
#creation_date, modification_date, publishing_date, is_public, status, folder_path) VALUES (
#'Blog archive',
#'Famous blog application',
#'EvgeniaS',
#'','',
#'2009-04-23 16:25:25', 
#0,
#0,
#true,
#false,
#'users/markku/blog/');

#INSERT INTO applications (application_title, description, author, url, rel_url,
#creation_date, modification_date, publishing_date, is_public, status, folder_path) VALUES (
#'News reader',
#'Simple RSS reader',
#'EvgeniaS',
#'','',
#'2009-06-04 16:25:25', 
#0,
#0,
#true,
#false,
#'users/markku/news_widget/' 
#);

#------------------------------------- Loading data into pages table ------------------------------------- 

#-- Test app
INSERT INTO pages (page_title, description, creation_date, modification_date, rel_url, application_id) 
VALUES( 		
'News',
'Main page representing the whole functionality',
'2009-07-26 16:25:25', 
0,
'index',
1);

#INSERT INTO pages (page_title, description, creation_date, modification_date, rel_url, application_id) VALUES(
#'page 2',
#'page 2 descr',
#'2009-04-23 16:25:25', 
#0,
#'',
#1);

#INSERT INTO pages (page_title, description, creation_date, modification_date, rel_url, application_id) VALUES(
#'page 3',
#'page 3 descr',
#'2009-04-23 16:25:25', 
#0,
#'',
#1);

#-- Contact management
#INSERT INTO pages (page_title, description, creation_date, modification_date, rel_url, application_id) VALUES(
#'Login page',
#'Login page with login and password fields',
#'2009-04-23 16:25:25', 
#0,
#'',
#2);

#INSERT INTO pages (page_title, description, creation_date, modification_date, rel_url, application_id) VALUES(
#'Main page',
#'View contacts here',
#'2009-04-23 16:25:25', 
#0,
#'',
#2);

#-- Blog app
#INSERT INTO pages (page_title, description, creation_date, modification_date, rel_url, application_id) VALUES(
#'Main page',
#'Main page of the blog app',
#'2009-04-23 16:25:25', 
#0,
#'',
#3);

#-- Blog app
#INSERT INTO pages (page_title, description, creation_date, modification_date, rel_url, application_id) VALUES(
#'MAin news page',
#'Main page of the News Reader app',
#'2009-06-04 16:25:25', 
#0,
#'',
#4);
