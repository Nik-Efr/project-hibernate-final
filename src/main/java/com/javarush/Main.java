package com.javarush;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javarush.entity.City;
import com.javarush.entity.Country;
import com.javarush.entity.CountryLanguage;
import com.javarush.redis.CityCountry;
import com.javarush.redis.Language;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisStringCommands;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

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
        List<City> allCities = main.fetchData(main);
        List<CityCountry> cityCountries = main.transformData(allCities);
        RedisClient redisClient = main.getRedisClient();
        main.saveToRedis(cityCountries, redisClient);
        main.shutdown(redisClient);
    }

    private void saveToRedis(List<CityCountry> cityCountries, RedisClient redisClient) {
        ObjectMapper mapper = new ObjectMapper();
        try (StatefulRedisConnection<String, String> connection = redisClient.connect()) {
            RedisStringCommands<String,String> sync = connection.sync();
            for(CityCountry cc:cityCountries){
                sync.set(String.valueOf(cc.getId()),mapper.writeValueAsString(cc));
            }
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }
    }

    private RedisClient getRedisClient() {
        RedisClient redisClient = RedisClient.create(RedisURI.create("localhost", 6379));
        try (StatefulRedisConnection<String, String> connection = redisClient.connect()) {
            System.out.println("\nConnected to Redis!\n");
        }
        return redisClient;
    }

    private List<CityCountry> transformData(List<City> cities) {
        return cities.stream().map(city -> {
            CityCountry res = new CityCountry();
            res.setId(city.getId());
            res.setName(city.getName());
            res.setPopulation(city.getPopulation());
            res.setDistrict(city.getDistrict());

            Country country = city.getCountry();
            res.setAlternativeCountryCode(country.getCode2());
            res.setContinent(country.getContinent());
            res.setCountryCode(country.getCode());
            res.setCountryName(country.getName());
            res.setCountryPopulation(country.getPopulation());
            res.setCountryRegion(country.getRegion());
            res.setCountrySurfaceArea(country.getSurfaceArea());
            List<CountryLanguage> countryLanguages = country.getLanguages();
            List<Language> languages = countryLanguages.stream().map(cl -> {
                Language language = new Language();
                language.setLanguage(cl.getLanguage());
                language.setIsOfficial(cl.getIsOfficial());
                language.setPercentage(cl.getPercentage());
                return language;
            }).collect(Collectors.toList());
            res.setLanguages(languages);

            return res;
        }).collect(Collectors.toList());
    }

    private List<City> fetchData(Main main) {
        Session session = main.sessionFactory.openSession();
        List<Country> countries = session.createQuery("select c from Country c join fetch c.languages", Country.class).list();
        List<City> cities = new ArrayList<City>();
        try (session) {
            session.getTransaction().begin();

            Long count = session.createQuery("select count(c) from City c", Long.class).getSingleResult();
            Integer step = 500;
            for (int i = 0; i < count; i += step) {
                cities.addAll(session.createQuery("select c from City c", City.class).setFirstResult(i).setMaxResults(step).list());
            }
            session.getTransaction().commit();
        }
        return cities;
    }


    private void shutdown(RedisClient redisClient) {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
        if (redisClient!=null) {
            redisClient.shutdown();
        }
    }
}