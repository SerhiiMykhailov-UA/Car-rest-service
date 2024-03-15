insert into car_category (car_id, category_id) 
select temp_car.id, category.id from temp_car 
join category on category_name like '%' || category.name || '%'; 