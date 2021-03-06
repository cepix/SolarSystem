package SolarSystem.Tests;

import SolarSystem.Application;
import SolarSystem.Models.WeatherRecord;
import SolarSystem.Repositories.WeatherRepository;
import SolarSystem.Utilities.WeatherDays;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;
import java.util.logging.LogRecord;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@DataMongoTest
@SpringBootTest(classes = Application.class)
public class MongoDBIntegrationTest {

    private String collectionName;
    LogRecord logRecToInsert;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private WeatherRepository weatherRepo;

    @Before
    public void before() {
        collectionName = "WeatherRecord";
        mongoTemplate.dropCollection(collectionName);
    }


    @Test
    public void checkMongoTemplate() {
        assertNotNull(mongoTemplate);
        MongoCollection<Document> createdCollection =  mongoTemplate.createCollection(collectionName);
        assertTrue(mongoTemplate.collectionExists(collectionName));
//        assertSame("WeatherRecord", createdCollection.);

    }

    @Test
    public void checkDocumentAndQuery() {
        WeatherRecord wr = new WeatherRecord(1, WeatherDays.MILD);
        weatherRepo.save(wr);
        Optional foundRecord = weatherRepo.findById(1);
        assert(foundRecord.isPresent());
        assertEquals(foundRecord.get(),wr);

    }

    @After

    public void after() {
        WeatherRecord wr = new WeatherRecord(1, WeatherDays.MILD);
        weatherRepo.save(wr);
        weatherRepo.delete(wr);
        Optional foundRecord = weatherRepo.findById(1);
        assertTrue(!foundRecord.isPresent());
    }

}