# java-explore-with-me
Template repository for ExploreWithMe project.

## Функциональность: рейтинги

### Public: Оценки
<span style="color:blue">**GET**</span>: **/ratings/events** - получение всех событий, с упорядочеванием от наибольшего рейтинга к наименьшему      
<span style="color:blue">**GET**</span>: **/ratings/users** - получение всех пользователей, с упорядочеванием от наибольшего рейтинга к наименьшему

### Private: Оценки
<span style="color:green">**POST**</span>: **/ratings/users/{userId}/events/{eventId}?mark={true/false}** - оценка от пользователя с id = userId событию с 
id = userId. true - событие понравилось, false - событие не понравилось <br>
<span style="color:orange">**PATCH**</span>: **/ratings/users/{userId}/events/{eventId}** - изменение оценки от пользователя с id = userId событию с id = userId. 
Найденная оценка будет инвертирована<br>
<span style="color:red">**DELETE**</span>: **/ratings/users/{userId}/events/{eventId}** - удаление оценки id = userId событию с id = userId.
