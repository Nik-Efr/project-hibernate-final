1. Проект: Финальный проект по Hibernate с кэшированием Redis
   Демонстрирующее продвинутые возможности Hibernate ORM в сочетании с Redis для кэширования.
   Включает оптимизацию производительности, работу с большими объемами данных и сравнительное тестирование
   производительности.

2. Собрать и запустить
   mvn clean package
   docker run --name mysql-world -e MYSQL_ROOT_PASSWORD=mysql -e MYSQL_DATABASE=world -p 3306:3306 -d mysql:8.0
   Проинициализировать базу init.sql
   docker run -d --name redis-stack -p 6379:6379 -p 8001:8001 redis/redis-stack:latest
   java -jar target/project-hibernate-final-1.0-SNAPSHOT-jar-with-dependencies.jar

3. Функциональность
   Работа с большими объемами данных через Hibernate 
   Интеграция с Redis для высокопроизводительного кэширования 
   Сравнительный анализ производительности с кэшем и без 
   Инструменты для нагрузочного тестирования

4. Тестирование производительности
   Redis benchmark:
   redis-benchmark -h 127.0.0.1 -p 6379 -c 50 -n 10000 -t get -d 256 --csv
   MySQL benchmark:
   mysqlslap --user=root --password="mysql" --host=127.0.0.1 --port=3306 --concurrency=50 --iterations=10 --auto-generate-sql --verbose
