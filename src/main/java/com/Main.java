/*
 * Copyright (C) Lowe's Companies, Inc. All rights reserved.
 * This file is for internal use only at Lowe's Companies, Inc.
 */

package com;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.model.User;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

/**
 * @author msharan
 */

public class Main {

    public static ObjectMapper objectMapper;
    private static final List<User> users = Lists.newArrayList();

    static {
        objectMapper = new ObjectMapper();
    }

    @SneakyThrows
    public static void main(String[] args) {
        String fileName = "Users.json";
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        String json = new String(Files.readAllBytes(file.toPath()));
        System.out.println(json);
        List<User> userData = objectMapper.readValue(json, new TypeReference<>() {
        });
        users.addAll(userData);

        Client client = new Client(users);
        client.runAll();
    }
}
