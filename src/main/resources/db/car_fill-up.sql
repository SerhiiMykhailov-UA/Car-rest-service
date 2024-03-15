insert into car (objectId, model, year, maker_id) 
select objectid, model, year, maker.id 
from temp_car 
inner join maker on temp_car.maker = maker.name;