package org.example;

import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONAssert;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class JSONGeneratorTest {

    @Test
    void serialize() {
        String actualJSON = "{ \"author\": \"A\", \"title\": \"A\", \"pages\": 13, " +
                "\"genres\": [\"a\",null], \"tags\": [\"aaaa\",null], \"read\": false}";
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add(null);
        String[] Array = {"aaaa", null};
        BookTest book1 = new BookTest("A", "A", 13, list, Array, false);
        JSONGenerator test = new JSONGenerator();
        assertDoesNotThrow(() -> JSONAssert.assertEquals(actualJSON, test.generateJSON(book1), true));
    }

    @Test
    void serializeString() {
        String actualJSON = "{ \"author\": \"A\", \"title\": null, \"pages\": 0, " +
                "\"genres\": null, \"tags\": null, \"read\": false}";
        BookTest book1 = new BookTest(null, "A", 0, null, null, false);
        JSONGenerator test = new JSONGenerator();
        assertDoesNotThrow(() -> JSONAssert.assertEquals(actualJSON, test.generateJSON(book1), true));
    }

    @Test
    void serialiseCollection() {
        String actualJSON = "{ \"author\": null, \"title\": null, \"pages\": 0, " +
                "\"genres\": [\"a\",null], \"tags\": null, \"read\": false}";
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add(null);
        BookTest book1 = new BookTest(null, null, 0, list, null, false);
        JSONGenerator test = new JSONGenerator();
        assertDoesNotThrow(() -> JSONAssert.assertEquals(actualJSON, test.generateJSON(book1), true));
    }

    @Test
    void serialiseArray() {
        String actualJSON = "{ \"author\": null, \"title\": null, \"pages\": 0, " +
                "\"genres\": null, \"tags\": [\"aaaa\",null], \"read\": false}";
        String[] Array = {"aaaa", null};
        BookTest book1 = new BookTest(null, null, 0, null, Array, false);
        JSONGenerator test = new JSONGenerator();
        assertDoesNotThrow(() ->  JSONAssert.assertEquals(actualJSON, test.generateJSON(book1), true));
    }

    @Test
    void serialisePrimitive() {
        String actualJSON = "{ \"author\": null, \"title\": null, \"pages\": 1234124, " +
                "\"genres\": null, \"tags\": null, \"read\": true}";
        BookTest book1 = new BookTest(null, null, 1234124, null, null, true);
        JSONGenerator test = new JSONGenerator();
        assertDoesNotThrow(() -> JSONAssert.assertEquals(actualJSON, test.generateJSON(book1), true));
    }

    @Test
    void serialiseObject() {
        String actualJSON = "{ \"author\": \"A\", \"title\": \"A\", \"pages\": 13, " +
                "\"genres\": [\"a\",null], \"tags\": [\"aaaa\",null], \"read\": false}";
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add(null);
        String[] Array = {"aaaa", null};
        BookTest book1 = new BookTest("A", "A", 13, list, Array, false);
        JSONGenerator test = new JSONGenerator();
        assertDoesNotThrow(() -> JSONAssert.assertEquals(actualJSON, test.generateJSON(book1), true));
    }

    @Test
    void checkPrimitive() {
        String actualJSON = "{ \"author\": \"A\", \"title\": \"A\", \"pages\": 13, " +
                "\"genres\": [\"a\",null], \"tags\": [\"aaaa\",null], \"read\": false}";
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add(null);
        String[] Array = {"aaaa", null};
        BookTest book1 = new BookTest("A", "A", 13, list, Array, false);
        JSONGenerator test = new JSONGenerator();
        assertDoesNotThrow(() -> JSONAssert.assertEquals(actualJSON, test.generateJSON(book1), true));
    }

    @Test
    void generateJSON() {
        String actualJSON = "{ \"author\": \"A\", \"title\": \"A\", \"pages\": 13, " +
                "\"genres\": [\"a\",null], \"tags\": [\"aaaa\",null], \"read\": false}";
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add(null);
        String[] Array = {"aaaa", null};
        BookTest book1 = new BookTest("A", "A", 13, list, Array, false);
        JSONGenerator test = new JSONGenerator();
        assertDoesNotThrow(() -> JSONAssert.assertEquals(actualJSON, test.generateJSON(book1), true));
    }
}