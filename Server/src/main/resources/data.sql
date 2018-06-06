
insert into USER values('Jacek', 'Wiktorska 83', '987-654-321', 'root');
insert into USER values('Tomek', 'Wiktorska 83', '123-456-789', 'root');
insert into USER values('Bartek', 'Warynskiego 12', '354-345-876', 'root');
insert into USER values('Michal', 'Warynskiego 12', '123-897-345', 'root');

insert into POST values(10000, 'Mysliwiecka 4a', '987-654-321', sysdate(), 'basic', 20, 'Jacek', NULL);
insert into POST values(10001, 'Koncertowa 8a', '123-456-789', sysdate(), 'advanced', 50, 'Bartek', NULL);
insert into POST values(10002, 'Inspektowa 1', '123-456-789', sysdate(), 'basic', 35, 'Tomek', NULL);
