INSERT INTO public.users ("name",email) VALUES
	 ('Иван Иванов','ivanov@ya.ru'),
	 ('Иван Петров','petrov@ya.ru'),
	 ('Юзер','user@user.ru'),
	 ('Салим Сидоров','salim@mail.ru'),
	 ('Гена Чебурашков','gena@mail.ru'),
	 ('Игорь Штольц','shtolz@gmail.com'),
	 ('Джон Джонсон','jhon@gmail.com'),
	 ('Француа Перес','peres@gmail.com'),
	 ('Олег Олегов','olegov@mail.com'),
	 ('Дим Димов','dimov@yandex.ru');

INSERT INTO public.categories ("name") VALUES
	 ('Экстремальный отдых'),
	 ('Театр'),
	 ('Спорт'),
	 ('Еда');

INSERT into public.locations (lat, lon) VALUES
    (51.722403, 85.756690),
    (55.750696, 37.611572),
    (55.740364, 37.620956),
    (61.133042, 47.293638);

INSERT INTO public.events (annotation,category,confirmed_requests,created_on,description,event_date,initiator,"location",paid,participant_limit,published_on,request_moderation,state,title,"views") VALUES
	 ('Гастрономический тур по ресторанам',4,0,'2023-04-16 02:59:11.752886','Гастрономический тур по лучшим ресторанам Москвы','2023-12-31 15:10:05',10,2,true,10,NULL,false,'PUBLISHED','Гастрономический тур по ресторанам',0),
	 ('Постановка современного режиссера',2,0,'2023-04-16 02:59:28.959982','Постановка современного режиссера старого спектакля','2023-12-31 15:10:05',1,3,true,10,NULL,false,'PUBLISHED','Поход в театр в пятницу вечером',0),
	 ('Современная постановка знаменитого спектакля',2,0,'2023-04-16 03:00:03.615151','Постановка современного режиссера старого спектакля','2023-12-31 15:10:05',5,4,true,10,NULL,false,'PUBLISHED','Поход на современный спектакль в выходные',0),
	 ('Сплав на байдарках по горной реке',1,0,'2023-04-16 02:58:16.335029','Сплав на байдарках похож на полет. На спокойной воде — это парение.','2023-12-31 15:10:05',1,1,true,10,'2024-04-16 03:00:23.534018',false,'PUBLISHED','Сплав на байдарках',0),
	 ('Сплав на байдарках в будущем',1,0,'2024-04-16 02:58:16.335029','Сплав на байдарках похож на полет. На спокойной воде — это парение.','2024-12-31 15:10:05',7,1,true,10,'2024-04-16 03:00:23.534018',false,'PENDING','Сплав на байдарках в будущем',0);

INSERT INTO public.requests (created,"event",requester,status) VALUES
	 ('2023-04-15 03:04:19.290274',2,1,'CONFIRMED'),
	 ('2023-04-15 03:04:22.940463',4,1,'CONFIRMED'),

	 ('2023-04-15 03:04:36.969415',1,2,'CONFIRMED'),
	 ('2023-04-15 03:04:40.144693',2,2,'CONFIRMED'),
	 ('2023-04-15 03:04:42.227707',3,2,'CONFIRMED'),
	 ('2023-04-15 03:04:45.684794',4,2,'CONFIRMED'),

	 ('2023-04-15 03:04:58.114206',1,3,'CONFIRMED'),
	 ('2023-04-15 03:05:00.14545',2,3,'CONFIRMED'),
	 ('2023-04-15 03:05:03.04819',3,3,'CONFIRMED'),
	 ('2023-04-15 03:05:05.29275',4,3,'CONFIRMED'),

	 ('2023-04-15 03:05:12.673216',1,4,'CONFIRMED'),
	 ('2023-04-15 03:05:14.73326',2,4,'CONFIRMED'),
	 ('2023-04-15 03:05:16.930011',3,4,'CONFIRMED'),
	 ('2023-04-15 03:05:19.701869',4,4,'CONFIRMED'),

	 ('2023-04-15 03:05:31.389011',1,5,'CONFIRMED'),
	 ('2023-04-15 03:05:33.430376',2,5,'CONFIRMED'),
	 ('2023-04-15 03:05:35.615402',3,5,'CONFIRMED'),

	 ('2023-04-15 03:05:45.161505',1,6,'CONFIRMED'),
	 ('2023-04-15 03:05:47.864018',2,6,'CONFIRMED'),
	 ('2023-04-15 03:05:49.7383',3,6,'CONFIRMED'),
	 ('2023-04-15 03:05:51.587382',4,6,'CONFIRMED'),

	 ('2023-04-15 03:05:57.765393',1,7,'CONFIRMED'),
	 ('2023-04-15 03:06:04.563588',2,7,'CONFIRMED'),
	 ('2023-04-15 03:06:07.357973',3,7,'CONFIRMED'),
	 ('2023-04-15 03:06:10.434729',4,7,'CONFIRMED'),

	 ('2023-04-15 03:06:17.945956',1,8,'CONFIRMED'),
	 ('2023-04-15 03:06:19.985943',2,8,'CONFIRMED'),
	 ('2023-04-15 03:06:22.575241',3,8,'CONFIRMED'),
	 ('2023-04-15 03:06:25.073874',4,8,'CONFIRMED'),

	 ('2023-04-15 03:06:33.252844',1,9,'CONFIRMED'),
	 ('2023-04-15 03:06:36.03098',2,9,'CONFIRMED'),
	 ('2023-04-15 03:06:38.471261',3,9,'CONFIRMED'),
	 ('2023-04-15 03:06:40.518593',4,9,'CONFIRMED'),
	 ('2024-04-16 03:06:40.518593',5,9,'CONFIRMED');



INSERT INTO public.ratings(event, initiator, rater, rate) VALUES(1,10,1,true);
INSERT INTO public.ratings(event, initiator, rater, rate) VALUES(1,10,2,true);
INSERT INTO public.ratings(event, initiator, rater, rate) VALUES(1,10,3,true);

INSERT INTO public.ratings(event, initiator, rater, rate) VALUES(2,1,2,true);
INSERT INTO public.ratings(event, initiator, rater, rate) VALUES(2,1,3,true);
INSERT INTO public.ratings(event, initiator, rater, rate) VALUES(2,1,5,false);

INSERT INTO public.ratings(event, initiator, rater, rate) VALUES(3,5,1,true);
INSERT INTO public.ratings(event, initiator, rater, rate) VALUES(3,5,2,true);
INSERT INTO public.ratings(event, initiator, rater, rate) VALUES(3,5,3,false);
INSERT INTO public.ratings(event, initiator, rater, rate) VALUES(3,5,4,false);

INSERT INTO public.ratings(event, initiator, rater, rate) VALUES(4,1,1,true);
INSERT INTO public.ratings(event, initiator, rater, rate) VALUES(4,1,2,true);
INSERT INTO public.ratings(event, initiator, rater, rate) VALUES(4,1,3,true);
INSERT INTO public.ratings(event, initiator, rater, rate) VALUES(4,1,4,false);