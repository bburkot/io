alter table trip add column duration_in_seconds integer; 
update trip as T set duration_in_seconds = (select count(*) * 15 from trip_point as TP where TP.trip_id = T.id);
-- update trip as T set duration_in_seconds = duration_in_seconds + 15;

update trip set day_type = 0 where date_trunc('day', timestamp) IN ('2013-08-15', '2013-10-05', '2013-11-01', '2013-12-01', '2013-12-08', '2013-12-25', '2014-01-01', '2014-02-14', '2014-03-03', '2014-04-18', '2014-04-20', '2014-04-21', '2014-04-25', '2014-05-01', '2014-05-04', '2014-05-29', '2014-06-10', '2014-06-13', '2014-06-19', '2014-06-24', '2014-06-29');
update trip set day_type = 1 where date_trunc('day', timestamp) IN ('2013-08-14', '2013-10-04', '2013-10-31', '2013-11-30', '2013-12-07', '2013-12-24', '2013-12-31', '2014-02-13', '2014-03-02', '2014-04-17', '2014-04-19', '2014-04-24', '2014-04-30', '2014-05-03', '2014-05-28', '2014-06-09', '2014-06-12', '2014-06-18', '2014-06-23', '2014-06-28');


ALTER TABLE trip ADD COLUMN start_district integer;
ALTER TABLE trip ADD COLUMN end_district integer;

update trip 
set start_district = (
	select ceil((latitude + 9.137097) / 0.00701627) * 200 
		+ ceil((longitude - 38.715066) / 0.01695735)
	from point where id = trip.start_point_id
), end_district = (
	select ceil((latitude + 9.137097) / 0.00701627) * 200
		+ ceil((longitude - 38.715066) / 0.01695735)
	from point where id = trip.end_point_id
)


bayes.png

