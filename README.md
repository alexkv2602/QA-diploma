## *Процедура запуска авто-тестов:*
1. Открыть проект в IntelliJ IDEA: склонировать репозиторий https://github.com/alexkv2602/QA-diploma.git
2. Запустить Docker Desktop
3. В терминале запустить контейнеры с базами данных командой: docker-compose up
4. После запуска контейнеров, в новом окне терминала (либо в терминале IntelliJ IDEA),запустить SUT командой java -jar artifacts/aqa-shop.jar -port=8080
5. Для запуска приложения на базе данных mysql  java -jar artifacts/aqa-shop.jar -port=8080 --spring.profiles.active=mysql
6. Для запуска приложения на базе данных postgres java -jar artifacts/aqa-shop.jar -port=8080 --spring.profiles.active=postgresql
7. Проверить доступность приложения в браузере по адресу  http://localhost:8080/
8. Запустить авто-тесты  командой ./gradlew clean test в терминале IntelliJ IDEA
9. После прохождения всех тестов для генерации отчета и автоматического открытия его в браузере следует ввести в терминале IntelliJ IDEA команду: ./gradlew allureServe

