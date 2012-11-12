package org.monkey.common.utils;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;

public class CollectionUtils {

    public static <F, T> List<T> transformNow(Iterable<F> from, Function<? super F, ? extends T> function) {
        return newArrayList(Iterables.transform(from, function));
    }

    public static <F, T> Set<T> transformUniqueNow(Iterable<F> from, Function<? super F, ? extends T> function) {
        return newHashSet(Iterables.transform(from, function));
    }

    public static <T> List<T> reverseNow(List<T> list) {
        return newArrayList(Lists.reverse(list));
    }
}
