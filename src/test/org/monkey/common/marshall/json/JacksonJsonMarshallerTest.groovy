package org.monkey.common.marshall.json

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import org.apache.commons.lang.builder.EqualsBuilder
import org.apache.commons.lang.builder.HashCodeBuilder
import org.hamcrest.Matchers
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.LocalDate
import org.junit.Before
import org.junit.Test
import org.monkey.common.utils.AssertUtils


class JacksonJsonMarshallerTest {

    JacksonJsonMarshaller marshaller

    @Before
    public void before() {
        marshaller = new JacksonJsonMarshaller()
    }

    @Test
    public void canMarshallAndUnmarshallAPojo() {
        def pojo = newPojoWithPrimitives()
        def marshalled = marshaller.marshall(pojo)

        assert marshalled == """{"value":"someString","intValue":1,"longValue":1,"floatValue":1.0,"doubleValue":1.0,"boolValue":true,"object":null,"bdValue":1}"""
        assert marshaller.unmarshall(marshalled, MyPojoWithPrimitives) == pojo
    }

    @Test
    public void canMarshallAndUnmarshallAPojoWithPrimitivesUsingOutputOrInputStreams() {
        def pojo = newPojoWithPrimitives()
        def outputStream = new ByteArrayOutputStream()
        marshaller.marshall(outputStream, pojo)
        assert outputStream.toString() == """{"value":"someString","intValue":1,"longValue":1,"floatValue":1.0,"doubleValue":1.0,"boolValue":true,"object":null,"bdValue":1}"""

        marshaller.unmarshall(outputStream.toString(), MyPojoWithPrimitives) == pojo
    }

    @Test
    public void canMarshallAndUnmarshallAPojoWithDates() {
        def pojo = newPojoWithDates(new DateTime(2013, 1, 19, 11, 22, 33, 0, DateTimeZone.UTC), new LocalDate("2013-01-18"))
        def json1 = marshaller.marshall(pojo)
        assert json1 == """{"dateTime":"2013-01-19T11:22:33Z","localDate":"2013-01-18"}"""

        def pojoWithNull = newPojoWithDates(null, null)
        def json2 = marshaller.marshall(pojoWithNull)
        assert json2 == """{"dateTime":null,"localDate":null}"""

        assert marshaller.unmarshall(json1, MyPojoWithDates) == pojo
        assert marshaller.unmarshall(json2, MyPojoWithDates) == pojoWithNull
    }

    @Test
    public void canMarshallAndUnmarshallANonUTCDateTimeToJson() {
        def time1 = new DateTime(2012, 12, 14, 0, 1, 2, DateTimeZone.forID("Asia/Shanghai"))
        def time2 = new DateTime(2012, 12, 14, 0, 1, 2, DateTimeZone.forOffsetHours(1))

        def time1Json = "\"2012-12-14T00:01:02+08:00\""
        def time2Json = "\"2012-12-14T00:01:02+01:00\""
        assert marshaller.marshall(time1) == time1Json
        assert marshaller.marshall(time2) == time2Json

        assert marshaller.unmarshall(time1Json, DateTime) == time1
        assert marshaller.unmarshall(time2Json, DateTime) == time2
    }

    @Test
    public void canMarshallAndUnmarshallAPojoWithImplicitlyExcludedFields() {
        def pojo = new MyPojoWithImplicitlyExcludedFields()
        pojo.value = "string1"
        def json = marshaller.marshall(pojo)
        assert json == """{"value":"string1"}"""

        assert marshaller.unmarshall(json, MyPojoWithImplicitlyExcludedFields) == pojo
    }

    @Test
    public void canMarshallAndUnmarshallAPojoWithFinalFields() {
        def pojo = new MyPojoWithFinalFields("finalValue")
        pojo.value = "string1"
        def json = """{"customField":"finalValue","value":"string1","defaultFinalField":"default-final-field"}"""
        assert marshaller.marshall(pojo) == json

        assert marshaller.unmarshall(json, MyPojoWithFinalFields) == pojo
    }

    @Test
    public void canMarshalAndUnmarshallAPojoWithExplicitlyExcludedFields() {
        def pojo = newPojoWithExplicitlyExcludedFields()
        def json = """{"value":"string1"}"""
        assert marshaller.marshall(pojo) == json
        assert marshaller.unmarshall(json, MyPojoWithExplicitlyExcludedFields) == pojo
        assert marshaller.unmarshall("""{"value":"string1","excludedField":"to-be-ignored"}""", MyPojoWithExplicitlyExcludedFields) == pojo
    }

    @Test
    public void canMarshalAndUnmarshallAListOfPojos() {
        def pojos = [newPojo("string1"), newPojo("string2"), newPojo("string3")]
        def json = """[{"value":"string1"},{"value":"string2"},{"value":"string3"}]"""
        assert marshaller.marshall(pojos) == json
        assert marshaller.unmarshall(json, List, MyPojo) == pojos
    }

    @Test
    public void canMarshallAndUnmarshallAListOfPojoContainingNull() {
        def pojos = [newPojo("string1"), newPojo("string2"), null, newPojo("string3")]
        def json = """[{"value":"string1"},{"value":"string2"},null,{"value":"string3"}]"""
        assert marshaller.marshall(pojos) == json
        assert marshaller.unmarshall(json, List, MyPojo) == pojos
    }

    @Test
    public void canMarshallAndUnmarshallAMapOfPojos() {
        def map = [key1: newPojo("string1"), key2: newPojo("string2"), key3: newPojo("string3")]
        def json = """{"key1":{"value":"string1"},"key2":{"value":"string2"},"key3":{"value":"string3"}}"""
        assert marshaller.marshall(map) == json
        assert marshaller.unmarshall(json, Map, String, MyPojo) == map
    }

    @Test
    public void canMarshallAPojoNestingAnotherPojo() {
        def pojo = newNestingPojo("string1", newNestedPojo("nested1"))
        def json = """{"value":"string1","nested":{"value":"nested1"}}"""
        assert marshaller.marshall(pojo) == json
        assert marshaller.unmarshall(json, MyNestingPojo) == pojo
    }

    @Test
    public void canMarshallAPojoWithLists() {
        def pojo = newPojoWithLists()
        def json = """{"strings":["string1","string2",null],"longs":[1,2,null],"doubles":[0.1,null,-0.32],"bigDecimals":[1,3,null],"booleans":[true,false,null],"nesteds":[{"value":"nested1"},null],"localDates":[null,"2013-01-19"],"nullList":null}"""
        assert marshaller.marshall(pojo) == json

        AssertUtils.assertProperties(marshaller.unmarshall(json, MyPojoWithLists), [
            strings: Matchers.is(["string1", "string2", null]),
            longs: Matchers.is([1L, 2L, null]),
            doubles: Matchers.is([0.1d, null, -0.32d]),
            bigDecimals: Matchers.is([BigDecimal.ONE, BigDecimal.valueOf(3L), null]),
            booleans: Matchers.is([true, false, null]),
            nesteds: Matchers.is([newNestedPojo("nested1"), null]),
            localDates: Matchers.is([null, new LocalDate("2013-01-19")]),
            nullList: Matchers.nullValue()
        ])
    }

/*
 * setup pojo classes for test
 */

    def newPojo(String value) {
        def pojo = new MyPojo()
        pojo.value = value
        return pojo
    }

    def newNestingPojo(String value, MyNestedPojo nested) {
        def pojo = new MyNestingPojo()
        pojo.value = value
        pojo.nested = nested
        return pojo
    }

    def newNestedPojo(String value) {
        def pojo = new MyNestedPojo()
        pojo.value = value
        return pojo
    }

    def newPojoWithPrimitives() {
        def pojo = new MyPojoWithPrimitives()
        pojo.value = "someString"
        pojo.intValue = 1
        pojo.longValue = 1
        pojo.floatValue = 1
        pojo.doubleValue = 1
        pojo.boolValue = true
        pojo.object = null
        pojo.bdValue = 1
        return pojo
    }

    def newPojoWithLists() {
        def pojo = new MyPojoWithLists()
        pojo.strings = ["string1", "string2", null]
        pojo.longs = [1L, 2L, null]
        pojo.doubles = [0.1d, null, -0.32d]
        pojo.bigDecimals = [1L, 3L, null]
        pojo.booleans = [true, false, null]
        pojo.nesteds = [newNestedPojo("nested1"), null]
        pojo.localDates = [null, new LocalDate("2013-01-19")]
        return pojo
    }

    def newPojoWithDates(DateTime dateTime, LocalDate localDate) {
        def pojo = new MyPojoWithDates()
        pojo.dateTime = dateTime
        pojo.localDate = localDate
        return pojo
    }

    def newPojoWithImplicitlyExcludedFields() {
        def pojo = new MyPojoWithImplicitlyExcludedFields()
        pojo.value = "string1"
        return pojo
    }

    def newPojoWithFinalFields() {
        def pojo = new MyPojoWithFinalFields("final1")
        pojo.value = "string1"
        return pojo
    }

    def newPojoWithExplicitlyExcludedFields() {
        def pojo = new MyPojoWithExplicitlyExcludedFields()
        pojo.value = "string1"
        return pojo
    }

    static class MyPojo {
        String value;

        @Override
        boolean equals(Object obj) {
            return EqualsBuilder.reflectionEquals(this, obj)
        }

        @Override
        int hashCode() {
            return HashCodeBuilder.reflectionHashCode(this)
        }
    }

    static class MyPojoWithDates {
        DateTime dateTime
        LocalDate localDate

        @Override
        boolean equals(Object obj) {
            return EqualsBuilder.reflectionEquals(this, obj)
        }

        @Override
        int hashCode() {
            return HashCodeBuilder.reflectionHashCode(this)
        }
    }

    static class MyPojoWithPrimitives extends MyPojo {
        int intValue
        long longValue
        float floatValue
        double doubleValue
        boolean boolValue
        Object object
        BigDecimal bdValue
    }

    static class MyPojoWithLists {
        List<String> strings
        List<Long> longs
        List<Double> doubles
        List<BigDecimal> bigDecimals
        List<Boolean> booleans
        List<MyNestedPojo> nesteds
        List<LocalDate> localDates
        List<String> nullList = null

        @Override
        boolean equals(Object obj) {
            return EqualsBuilder.reflectionEquals(this, obj)
        }

        @Override
        int hashCode() {
            return HashCodeBuilder.reflectionHashCode(this)
        }
    }

    static class MyNestingPojo extends MyPojo {
        MyNestedPojo nested
    }

    static class MyNestedPojo extends MyPojo {
    }

    static class MyPojoWithImplicitlyExcludedFields extends MyPojo {
        static String staticField = "static-field"
        transient String transientField = "transient-field"
    }

    static class MyPojoWithFinalFields extends MyPojo {
        final String defaultFinalField = "default-final-field"
        final String customField = "custom-field"

        @JsonCreator MyPojoWithFinalFields(@JsonProperty("customField") String finalValue) {
            this.customField = finalValue
        }
    }

    static class MyPojoWithExplicitlyExcludedFields extends MyPojo {
        @JsonIgnore
        String excludedField = "excluded-field"
    }


}
