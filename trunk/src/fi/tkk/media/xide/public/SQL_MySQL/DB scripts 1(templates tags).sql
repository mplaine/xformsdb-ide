#component0.xml -> tpl_login_simple.xml
#component2.xml -> tpl_survey_simple.xml
#component1.xml -> tpl_login_email.xml
#component3.xml -> tpl_question_yes_no.xml
#component4.xml -> tpl_question_simple.xml
#component5.xml -> tpl_question_checkbox.xml
#component6.xml -> tpl_question_range.xml
#component7.xml -> tpl_blog.xml
#component8.xml -> tpl_survey_slot.xml 
#component_app_1_id -> tpl_app_event_manager
#component_app_2_id -> tpl_app_personal_page
#component_page_1_id -> tpl_page_personal_biogrphy
#------------------------------------- Tables creation ------------------------------------- 
#-- Stores templates information
CREATE TABLE templates (
	template_id varchar(200),	#-- ID of the template
	template_title varchar(200) NOT NULL, 			#-- title of the template
	description text, 				#-- template description
	author varchar(100),			#-- username of the template author
	creation_date timestamp, 			-- date of creation (publishing)
	template_type int,				#-- type of the template: 1 - Applicaiton; 2 - Page; 3 - Component
	folder_path varchar(500), 		# path to the folder which contains files related to the template
	do_work boolean			,		# indicates whether this template works and can be dragged
	PRIMARY KEY (template_id)
	);

#-- Stores tags description
CREATE TABLE tags (
	tag_id 			int AUTO_INCREMENT,		#-- ID of the tag
	title			varchar(200), 			#-- title of the tag
	description		text, 				#-- tag description
	PRIMARY KEY (tag_id)
);

#-- Stores many-to-many relationship between  tags and templates
CREATE TABLE used_tags_map (
	tag_id int REFERENCES tags,			#-- link to the tag id
	template_id varchar(200) REFERENCES templates		#-- link to the template id
);

CREATE TABLE templates_files (
	template_file_id 	int AUTO_INCREMENT,			#-- ID of the file
	template_id  		varchar(200) REFERENCES templates,	#-- link to the template
	source			varchar(500),				#-- path to the source code file
	file_type		int, 						# type of the file
	PRIMARY KEY (template_file_id)
);

# ------------------------------------Load--------------------------------------
   
#-- Login
INSERT INTO templates VALUES ('tpl_login_simple',
'Simple login component', 
'This is simple login component which offers user to enter his/her login and password. Authententification query is included',
'evgenias',
'2009-04-22 09:16:16',
3, 
'tpl_login_simple/');
#-- Login with email
INSERT INTO templates VALUES ('tpl_login_email',
'Login component with email', 
'This is advanced login component which offers user to enter his/her login, password and e-mail. Query is not included',
'evgenias',
'2009-04-23 18:56:02',
3, 
'tpl_login_email/');
#-- Surway
INSERT INTO templates VALUES ('tpl_survey_simple',
'Simple survey', 
'This is a simple survey component which has a slot for adding questions and a commint button to commit the survey answers. Require query modification!!! Basic Query is included',
'evgenias',
'2009-04-22 11:20:12',
3, 
'tpl_survey_simple/');
#--
INSERT INTO templates VALUES ('tpl_question_yes_no',
'Yes/No question', 
'Simple question with 2 variants of answer. Question and answers texts are designed as parameters',
'evgenias',
'2009-04-22 15:46:21',
3, 
'tpl_question_yes_no/');

INSERT INTO templates VALUES ('tpl_question_simple',
'Simple question', 
'Simple question and string input field to enter the answer. Question line is designed as parameter',
'evgenias',
'2009-04-24 12:15:32',
3, 
'tpl_question_simple/');

INSERT INTO templates VALUES ('tpl_question_checkbox',
'Check box question', 
'Simple question and check box field to select or unselect. Question line is designed as parameter',
'evgenias',
'2009-04-23 05:42:55',
3, 
'tpl_question_checkbox/');

INSERT INTO templates VALUES ('tpl_question_range',
'Range question', 
'Simple question and range field for answering. Question text, min and max range values and the step are parameters. Query is not included',
'evgenias',
'2009-04-23 16:25:25',
3, 
'tpl_question_range/');

INSERT INTO templates VALUES ('tpl_blog',
'Blog', 
'Blog component. Designed to be used in WSC system as a blog. Queries are included',
'evgenias',
'2009-04-24 17:37:59',
3, 
'tpl_blog/');


INSERT INTO templates VALUES ('tpl_app_event_manager',
'Event Manager', 
'This is simple system helps you to manage your events',
'evgenias',
'2009-04-22 11:19:15',
1, 
'tpl_app_event_manager/');

INSERT INTO templates VALUES ('tpl_app_personal_page',
'Personal Web Site', 
'Includes personal biography page, picture library page, simple forum',
'evgenias',
'2009-04-21 18:35:08',
1, 
'tpl_app_personal_page/');

INSERT INTO templates VALUES ('tpl_page_personal_biogrphy',
'Personal biography web page', 
'Includes layout and several necessary components which forms personal biography page',
'evgenias',
'2009-04-23 12:16:51',
2, 
'tpl_page_personal_biogrphy/');

INSERT INTO templates VALUES ('tpl_news_widget',
'News reader component', 
'Component reads rss news ad display them as a list. News address is given as a parameter.',
'evgenias',
'2009-05-24 17:37:59',
3, 
'tpl_news_widget/');

INSERT INTO templates VALUES ('tpl_multinews_widget',
'Multinews widget', 
'This is multinews widget made by Heikki',
'evgenias',
'2009-04-22 09:16:16',
3, 
'tpl_multinews_widget/');

#Markku s components for survey example

INSERT INTO templates VALUES ('tpl_survey_textarea',
'Survey Textarea Component',
'A component for inputting multiple rows of text.',
'Markku Laine',
NOW(),
3,
'tpl_survey_textarea/');

INSERT INTO templates VALUES ('tpl_survey_select',
'Survey Select Component',
'A component for selecting multiple options.',
'Markku Laine',
NOW(),
3,
'tpl_survey_select/');

INSERT INTO templates VALUES ('tpl_survey_range',
'Survey Range Component',
'A component for selecting a value using a slider.',
'Markku Laine',
NOW(),
3,
'components/tpl_survey_range/');

INSERT INTO templates VALUES ('tpl_survey_select1',
'Survey Select1 Component',
'A component for selecting one option.',
'Markku Laine',
NOW(),
3,
'tpl_survey_select1/');

INSERT INTO templates VALUES ('tpl_survey_input',
'Survey Input Component',
'A component for inputting one row of text.',
'Markku Laine',
NOW(),
3,
'tpl_survey_input/');
#------------------------------------- Loading data into tags table ------------------------------------- 

INSERT INTO tags(title, description) VALUES( 
'Atomic',
'no descr');

INSERT INTO tags(title, description) VALUES( 
'Component Template',
'no descr');

INSERT INTO tags(title, description) VALUES( 
'Application Template',
'no descr');
INSERT INTO tags(title, description) VALUES( 
'Complex',
'no descr');
INSERT INTO tags(title, description) VALUES( 
'Multi-Page',
'no descr');
INSERT INTO tags(title, description) VALUES( 
'Ready-To-Use',
'no descr');
INSERT INTO tags(title, description) VALUES( 
'Page template',
'no descr');

#------------------------------------- Loading data into used_tags_map table ------------------------------------- 
#-- atomic
INSERT INTO used_tags_map VALUES( 
1,
'tpl_question_yes_no');
INSERT INTO used_tags_map VALUES( 
1,
'tpl_question_simple');
INSERT INTO used_tags_map VALUES( 
1,
'tpl_question_checkbox');
INSERT INTO used_tags_map VALUES( 
1,
'tpl_question_range');

#-- component
INSERT INTO used_tags_map VALUES( 
2,
'tpl_login_simple');
INSERT INTO used_tags_map VALUES( 
2,
'tpl_login_email');
INSERT INTO used_tags_map VALUES( 
2,
'tpl_survey_simple');
INSERT INTO used_tags_map VALUES( 
2,
'tpl_question_yes_no');
INSERT INTO used_tags_map VALUES( 
2,
'tpl_question_simple');
INSERT INTO used_tags_map VALUES( 
2,
'tpl_question_checkbox');
INSERT INTO used_tags_map VALUES( 
2,
'tpl_question_range');
INSERT INTO used_tags_map VALUES( 
2,
'tpl_blog');
#-- application
INSERT INTO used_tags_map VALUES( 
3,
'tpl_app_event_manager');
INSERT INTO used_tags_map VALUES( 
3,
'tpl_app_personal_page');
#-- page
INSERT INTO used_tags_map VALUES( 
7,
'tpl_page_personal_biogrphy');

INSERT INTO used_tags_map VALUES( 
1,
'tpl_news_widget');

#------------------------------------- Loading data into templates_files table ------------------------------------- 
# source code 1
INSERT INTO templates_files (template_id, source, file_type) VALUES( 
'tpl_login_simple',
'components/tpl_login_simple/tpl_login_simple.xml',
1);

INSERT INTO templates_files (template_id, source, file_type) VALUES( 
'tpl_login_email',
'components/tpl_login_email/tpl_login_email.xml',
1);

INSERT INTO templates_files (template_id, source, file_type)  VALUES( 
'tpl_survey_simple',
'components/tpl_survey_simple/tpl_survey_simple.xml',
1);

INSERT INTO templates_files (template_id, source, file_type)  VALUES( 
'tpl_question_yes_no',
'components/tpl_question_yes_no/tpl_question_yes_no.xml',
1);

INSERT INTO templates_files (template_id, source, file_type)  VALUES( 
'tpl_question_simple',
'components/tpl_question_simple/tpl_question_simple.xml',
1);

INSERT INTO templates_files (template_id, source, file_type)  VALUES( 
'tpl_question_checkbox',
'components/tpl_question_checkbox/tpl_question_checkbox.xml',
1);

INSERT INTO templates_files (template_id, source, file_type)  VALUES( 
'tpl_question_range',
'components/tpl_question_range/tpl_question_range.xml',
1);

INSERT INTO templates_files (template_id, source, file_type)  VALUES( 
'tpl_blog',
'components/tpl_blog/tpl_blog.xml',
1);


