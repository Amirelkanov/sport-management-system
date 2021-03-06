# Система для проведения спортивных соревнований

## Предназначение:

Данная программа помогает организаторам спортивных соревнований.
Оно может: 
 * Собирать данные о мероприятии воедино.
 * Быстро обрабатывать их, формируя стартовый протокол и сохраняя его в отдельный файл.
 * Проводить жеребьёвку.
 * Сохранять все полученные от пользователя данные и не переспрашивать их при повторном запуске.
 * По показателям контрольных пунктов определять результаты соревнований.
 * Выявлять нарушителей и людей, сошедших с дистанции.
 * Сохранять результаты группового зачёта. 
 * Сохранять результаты зачёта коллективов.
 * Создавать произвольное соревнование и автоматически генерировать для него случайные (но корректные) данные.


## Использование:

* Пользоваться программой очень просто - сначала нужно вести данные про группы, коллективы, мероприятие и дистанции.<br>
![startmenu.jpg](docs-images/startmenu.jpg)

* Можно выбирать готовые файлы:<br>
![choosefile.jpg](docs-images/choosefile.jpg)

* Или ввести их самому:<br>
![groups.jpg](docs-images/groups.jpg)
![distances.jpg](docs-images/distances.jpg)

* После того как все данные введены нужно нажать draw. После этого программа выдаст стартовый протокол.
![startprotocol.jpg](docs-images/startprotocol.jpg)

* Все протоколы выдаваемые программой можно сохранить:<br>
![startprotocolsaver.jpg](docs-images/startprotocolsaver.jpg)

* После выдачи стартового протокола программа попросит вести протокол прохождения пунктов. Она поддерживает два типа:<br>
![pointsloading.jpg](docs-images/pointsloading.jpg)

* Пример финишного протокола:<br>
![finish.jpg](docs-images/finish.jpg)

* Пример финишного протокола для коллективов:<br>
![collectivepoints.jpg](docs-images/collectivepoints.jpg)

Программа поддерживает консольный режим:<br>
![img.png](docs-images/img.png)

 * Ответив на первый вопрос "да" мы сгенерировали случные данные для соревнований, которые потом и передали программе.
 * После ввода данных в папке start-protocol сформировались стартовые протоколы для всех заявленных групп. Вот пример одного из них:<br>
![img_1.png](docs-images/img_1.png)

 * После этого мы выбрали формат файла с дистанциями. 
 * Дальше указали пути до файлов, куда сохранили результаты, вот пример файла с результатами групп:<br>
![img_2.png](docs-images/img_2.png)
