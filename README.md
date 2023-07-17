## *Процедура запуска авто-тестов:*
1. Открыть проект в IntelliJ IDEA: склонировать репозиторий https://github.com/alexkv2602/QA-diploma.git
2. Запустить Docker Desktop
3. В терминале запустить контейнеры с базами данных командой: docker-compose up
4. После запуска контейнеров, в новом окне терминала (либо в терминале IntelliJ IDEA),запустить SUT командой java -jar artifacts/aqa-shop.jar
5. Для запуска SUT с базой данных MySQL: java -jar artifacts/aqa-shop.jar --spring.datasource.url=jdbc:mysql://localhost:3306/app --spring.datasource.username=app --spring.datasource.password=pass
6. Для запуска SUT с базой данных Postgres: java -jar artifacts/aqa-shop.jar --spring.datasource.url=jdbc:postgresql://localhost:5432/app --spring.datasource.username=app --spring.datasource.password=pass
7. Проверить доступность приложения в браузере по адресу  http://localhost:8080/
8. Запустить авто-тесты  командой ./gradlew clean test в терминале IntelliJ IDEA
9. Для запуска тестов с базой данных MySQL: ./gradlew clean test -Ddb=mysql
10. Для запуска тестов с базой данных PostgresSQL: ./gradlew clean test -Ddb=postgresql
11. После прохождения всех тестов для генерации отчета и автоматического открытия его в браузере следует ввести в терминале IntelliJ IDEA команду: ./gradlew allureServe

