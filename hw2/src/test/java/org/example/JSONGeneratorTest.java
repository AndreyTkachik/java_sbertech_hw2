package org.example;

import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONAssert;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JSONGeneratorTest {

    @org.junit.jupiter.api.Test
    void serialize() throws InvocationTargetException, IllegalAccessException {
        String actualJSON = "{ \"author\": \"A\", \"title\": \"A\", \"pages\": 13, " +
                "\"genres\": [\"a\",null], \"tags\": [\"aaaa\",null], \"read\": false}";
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add(null);
        String[] Array = {"aaaa", null};
        BookTest book1 = new BookTest("A", "A", 13, list, Array, false);
        JSONGenerator test = new JSONGenerator();
        JSONAssert.assertEquals(actualJSON, test.generateJSON(book1), true);
    }

    @org.junit.jupiter.api.Test
    void serializeString() throws InvocationTargetException, IllegalAccessException {
        String actualJSON = "{ \"author\": \"A\", \"title\": null, \"pages\": 0, " +
                "\"genres\": null, \"tags\": null, \"read\": false}";
        BookTest book1 = new BookTest(null, "A", 0, null, null, false);
        JSONGenerator test = new JSONGenerator();
        JSONAssert.assertEquals(actualJSON, test.generateJSON(book1), true);
    }

    @org.junit.jupiter.api.Test
    void serialiseCollection() throws InvocationTargetException, IllegalAccessException {
        String actualJSON = "{ \"author\": null, \"title\": null, \"pages\": 0, " +
                "\"genres\": [\"a\",null], \"tags\": null, \"read\": false}";
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add(null);
        BookTest book1 = new BookTest(null, null, 0, list, null, false);
        JSONGenerator test = new JSONGenerator();
        JSONAssert.assertEquals(actualJSON, test.generateJSON(book1), true);
    }

    @org.junit.jupiter.api.Test
    void serialiseArray() throws InvocationTargetException, IllegalAccessException {
        String actualJSON = "{ \"author\": null, \"title\": null, \"pages\": 0, " +
                "\"genres\": null, \"tags\": [\"aaaa\",null], \"read\": false}";
        String[] Array = {"aaaa", null};
        BookTest book1 = new BookTest(null, null, 0, null, Array, false);
        JSONGenerator test = new JSONGenerator();
        JSONAssert.assertEquals(actualJSON, test.generateJSON(book1), true);
    }

    @org.junit.jupiter.api.Test
    void serialisePrimitive() throws InvocationTargetException, IllegalAccessException {
        String actualJSON = "{ \"author\": null, \"title\": null, \"pages\": 1234124, " +
                "\"genres\": null, \"tags\": null, \"read\": true}";
        BookTest book1 = new BookTest(null, null, 1234124, null, null, true);
        JSONGenerator test = new JSONGenerator();
        JSONAssert.assertEquals(actualJSON, test.generateJSON(book1), true);
    }

    @org.junit.jupiter.api.Test
    void serialiseObject() throws InvocationTargetException, IllegalAccessException {
        String actualJSON = "{ \"author\": \"A\", \"title\": \"A\", \"pages\": 13, " +
                "\"genres\": [\"a\",null], \"tags\": [\"aaaa\",null], \"read\": false}";
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add(null);
        String[] Array = {"aaaa", null};
        BookTest book1 = new BookTest("A", "A", 13, list, Array, false);
        JSONGenerator test = new JSONGenerator();
        JSONAssert.assertEquals(actualJSON, test.generateJSON(book1), true);
    }

    @org.junit.jupiter.api.Test
    void checkPrimitive() throws InvocationTargetException, IllegalAccessException {
        String actualJSON = "{ \"author\": \"A\", \"title\": \"A\", \"pages\": 13, " +
                "\"genres\": [\"a\",null], \"tags\": [\"aaaa\",null], \"read\": false}";
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add(null);
        String[] Array = {"aaaa", null};
        BookTest book1 = new BookTest("A", "A", 13, list, Array, false);
        JSONGenerator test = new JSONGenerator();
        JSONAssert.assertEquals(actualJSON, test.generateJSON(book1), true);
    }

    @org.junit.jupiter.api.Test
    void generateJSON() throws InvocationTargetException, IllegalAccessException {
        String actualJSON = "{ \"author\": \"A\", \"title\": \"A\", \"pages\": 13, " +
                "\"genres\": [\"a\",null], \"tags\": [\"aaaa\",null], \"read\": false}";
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add(null);
        String[] Array = {"aaaa", null};
        BookTest book1 = new BookTest("A", "A", 13, list, Array, false);
        JSONGenerator test = new JSONGenerator();
        JSONAssert.assertEquals(actualJSON, test.generateJSON(book1), true);
    }
}