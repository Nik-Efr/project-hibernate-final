package com.javarush;

import com.javarush.entity.City;
import com.javarush.entity.Country;
import com.javarush.entity.CountryLanguage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import java.util.Properties;

public class Main {
    private final SessionFactory sessionFactory;

    public Main() {
        Properties properties = new Properties();
        properties.put(Environment.DIALECT, "org.hibernate.dialect.MySQL8Dialect");
        properties.put(Environment.DRIVER, "com.p6spy.engine.spy.P6SpyDriver");
        properties.put(Environment.URL, "jdbc:p6spy:mysql://localhost:3306/world");
        properties.put(Environment.SHOW_SQL, "true");
        properties.put(Environment.USER, "root");
        properties.put(Environment.PASS, "mysql");
        properties.put(Environment.HBM2DDL_AUTO, "validate");
        sessionFactory = new Configuration().
                addProperties(properties).
                addAnnotatedClass(City.class).
                addAnnotatedClass(Country.class).
                addAnnotatedClass(CountryLanguage.class).
                buildSessionFactory();
    }

    public static void main(String[] args) {
        Main main = new Main();
        Transaction tx = null;
        try(Session session = main.sessionFactory.openSession()){
            tx = session.beginTransaction();
            try{
                System.out.println(session.get(City.class,1));
                System.out.println(session.get(Country.class,1));
                System.out.println(session.get(CountryLanguage.class,1));
                tx.commit();
            }catch (Exception e){
                tx.rollback();
                e.printStackTrace();
            }
        }


    }
}