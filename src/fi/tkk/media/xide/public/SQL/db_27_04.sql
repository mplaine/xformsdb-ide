Select templates.* from (
SELECT used_tags_map.template_id as template_id, count(*) as c FROM 
	(SELECT tag_id from tags where title in ('Component Template') ) as tags, used_tags_map 
WHERE tags.tag_id = used_tags_map.tag_id 
group by used_tags_map.template_id) as template_ids, templates where c = 1 and template_ids.template_id = templates.template_id and templates.type = 3
and( templates.template_title ~* 'question' or description ~* 'question')
	 	
select tags.title from tags, used_tags_map 
where tags.tag_id = used_tags_map.tag_id and template_id = 'component_6_id'

INSERT INTO used_tags_map SELECT tag_id, 'component_0_id' from tags where tags.title = 'Component Template'
DELETE from used_tags_map where template_id = 'component_0_id'
UPDATE  templates SET 
	template_title = 'Range Question',
	description = 'Simple question and range field for answering. Question text, min and max range values and the step are parameters. Query is not included',
	where template_id = 'component_6_id';

	SELECT templates.* FROM (SELECT used_tags_map.template_id AS template_id, count(*) AS c FROM
	(SELECT tag_id from tags where title in ('Component Template', 'Atomic') ) as tags, 
	used_tags_map 
	WHERE tags.tag_id = used_tags_map.tag_id 
	group by used_tags_map.template_id) as template_ids, templates 
	where c = 2 and template_ids.template_id = templates.template_id and templates.type =3

	select * from used_tags_map