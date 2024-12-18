## Задание №6

__Краткое описание__: Переписать приложение "Библиотека" на ORM.

### Цель:
Полноценно работать с JPA + Hibernate для подключения к реляционным БД посредством ORM-фреймворка.

__Результат:__  Высокоуровневое приложение с JPA-маппингом сущностей.

### Описание задания:

Домашнее задание выполняется переписыванием предыдущего на JPA.

__Требования:__

1. Использовать JPA, Hibernate только в качестве JPA-провайдера.
2. Spring Data пока использовать нельзя.
3. Загрузка связей сущностей не должна приводить к большому количеству запросов к БД или избыточному по объему набору 
данных (проблема N+1 и проблема произведения таблиц).
4. Добавить сущность "комментария к книге", реализовать CRUD для новой сущности. Получение всех комментариев делать 
не нужно. Только конкретного комментария по id и всех комментариев по конкретной книге, по ее id.
5. DDL через Hibernate должно быть отключено.
6. LAZY-связи не должны присутствовать в equals/hashCode/toString. В т.ч. за счет @Data.
7. Аннотация @Transactional должна присутствовать только на методах сервиса.
8. Покрыть репозитории тестами, используя H2 базу данных и @DataJpaTest.
9. Написать интеграционные тесты сервисов книг и комментариев, которые будут проверять работу с БД. 
Транзакционность в этих тестах должна быть отключена, чтобы не влияла на транзакции в сервисах. Проверить, что доступ 
к связям, которые используются снаружи сервисов не вызывают LazyInitialzationException. Не забыть учесть кэширование 
контекста в тестах. 
10. Добавить в решение тесты из заготовки. Для приема работы тесты должны проходить

__Заготовка для выполнения работы:__ https://github.com/OtusTeam/Spring/tree/master/templates/hw06-jpa