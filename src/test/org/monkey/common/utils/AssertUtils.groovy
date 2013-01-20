package org.monkey.common.utils

import org.hamcrest.Matcher
import org.springframework.beans.BeanUtils

import java.beans.PropertyDescriptor
import java.lang.reflect.Field
import java.lang.reflect.Modifier

class AssertUtils {

    public static void assertProperties(Object target, Map<String, Matcher> matchers, List excludedFields = []) {
        List<String> excluded = ["class", "metaClass", "fields", "data"] + excludedFields
        Map<String, PropertyDescriptor> untested = properties(target, excluded)
        matchers.keySet().each { String fieldName ->
            PropertyDescriptor property = untested.remove(fieldName)
            assert property != null, "field ${fieldName} not found on ${target.class.simpleName}"
            assertProperty(target, matchers, fieldName, property)
        }
        assert untested.keySet().size() == 0, "${untested.size()} fields untested ${untested}"
    }

    private static void assertProperty(Object target, Map<String, Matcher> matchers, String fieldName, PropertyDescriptor property) {
        try {
            assert matchers.get(fieldName).matches(property.readMethod.invoke(target)), "property difference on ${fieldName}"
        } catch (Exception e) {
            throw new RuntimeException("unable to access property ${fieldName} from ${target.class.simpleName}", e)
        }
    }

    private static Map properties(Object target, List excluded) {
        BeanUtils.getPropertyDescriptors(target.class)
            .toList()
            .findAll { !excluded.contains(it.name) && it.readMethod }
        .collectEntries { [(it.name): it] }
    }

    private static Map publicFields(Object target, List excluded) {
        findAllPublicFields(target.class)
            .toList()
            .findAll { !excluded.contains(it.name) }
        .collectEntries { [(it.name): it] }
    }

    private static List<Field> findAllPublicFields(Class clazz) {
        def declaredFields = clazz.declaredFields
        List<Field> fields = declaredFields.findAll { Field field ->
            Modifier.isPublic(field.modifiers)
        }

        def superClass = clazz.superclass
        if (superClass != null) {
            fields.addAll(findAllPublicFields(superClass))
        }

        clazz.interfaces.each { Class anInterface ->
            fields.addAll(findAllPublicFields(anInterface))
        }

        fields
    }
}
