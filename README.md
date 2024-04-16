# java-explore-with-me

## Проект состоит из двух сервисов основного (ewm-main-service) и сервиса статистики (ewm-stats-service).
### - [Спецификация основного сервиса (ewm-main-service) в формате Json](https://raw.githubusercontent.com/yandex-praktikum/java-explore-with-me/main/ewm-main-service-spec.json) <br>
### - [Спецификация сервиса статистики (ewm-stats-service) в формате Json](https://raw.githubusercontent.com/yandex-praktikum/java-explore-with-me/main/ewm-stats-service-spec.json) <br>

## Функциональность: рейтинги. <br>
### [Ссылка на Pull Request](https://github.com/dimapolt/java-explore-with-me/pull/3)
Описание:
В подсчёте рейтинга используется доверительный интервал биномиального распределения по методу Уилсона. <br>
![Wilson](ewm-main-service/src/main/resources/wilson.png)

## Эндпоинты функциональности
### Public: Оценки
<span style="color:blue">**GET**</span>: **/ratings/events** - получение всех событий, с упорядочеванием от наибольшего рейтинга к наименьшему      
<span style="color:blue">**GET**</span>: **/ratings/users** - получение всех пользователей, с упорядочеванием от наибольшего рейтинга к наименьшему
### Private: Оценки
<span style="color:green">**POST**</span>: **/ratings/users/{userId}/events/{eventId}?mark={true/false}** - оценка от пользователя с id = userId событию с
id = userId. true - событие понравилось, false - событие не понравилось <br>
<span style="color:orange">**PATCH**</span>: **/ratings/users/{userId}/events/{eventId}** - изменение оценки от пользователя с id = userId событию с id = userId.
Найденная оценка будет инвертирована<br>
<span style="color:red">**DELETE**</span>: **/ratings/users/{userId}/events/{eventId}** - удаление оценки id = userId событию с id = userId.

### Структура базы данных основного сервиса
![ER - диаграмма EWM-main-service](ewm-main-service/src/main/resources/ewm_main.png)<br>
### Структура базы данных сервиса статистики
![ER - диаграмма EWM-stats-service](ewm-stats-service/stats-server/src/main/resources/ewm_stats.png) <br>