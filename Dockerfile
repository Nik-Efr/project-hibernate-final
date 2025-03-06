FROM mysql:8.0

ENV MYSQL_ROOT_PASSWORD=mysql
ENV MYSQL_DATABASE=world

COPY dump-hibernate-final.sql /docker-entrypoint-initdb.d/

RUN chmod 644 /docker-entrypoint-initdb.d/dump-hibernate-final.sql

EXPOSE 3306

CMD ["mysqld"]
