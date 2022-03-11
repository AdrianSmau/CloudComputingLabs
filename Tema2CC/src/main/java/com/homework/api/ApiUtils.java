package com.homework.api;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ApiUtils {
    private ApiUtils() {
    }

    public static List<String> splitQuery(String query) {
        if (query == null || "".equals(query)) {
            return Collections.emptyList();
        }

        String[] componentsRaw = query.split("&")[0].split("/");
        return componentsRaw.length > 3 ? Arrays.asList(Arrays.copyOfRange(componentsRaw, 1, 4)) : Arrays.asList(Arrays.copyOfRange(componentsRaw, 1, 3));
    }
}
