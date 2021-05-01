/*
 * Copyright (C) Lowe's Companies, Inc. All rights reserved.
 * This file is for internal use only at Lowe's Companies, Inc.
 */

package com;

import com.google.common.collect.Lists;
import com.model.User;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author msharan
 */

public class Client {

    List<User> userList;

    public Client(List<User> userList) {
        this.userList = userList;
    }

    public void runAll() {
        printAllUsers();
        printUsersAbove30();
        getNamesOfPeople();
        getAllAges();
        getAllAgesSum();
        getNamesUpperCaseWhereAgeMoreThan30();
        getNameAsKeyAndAgeAsValue();
        getCommaSeparatedUpperCaseNameAgedMoreThan30();
        groupUsersWithAgesEvenAndOdd(); //single parameter based grouping
        groupUsersBasedOnNames(); //multiple parameter based grouping
        groupAgesBasedOnNames();
        getUserNamesCount();
        getUserNamesCount1();
        getAllAgesSum1();
        getMaxAge();
        getMaxAge1();
        groupOnAgeGetNamesWhereLengthMoreThan10();
        test();
    }

    private void test() {
        System.out.println("test");
        List<Integer> numbers = Lists.newArrayList(1,2,3,4,5);
        System.out.println(numbers.stream()
                .map(num -> num * 2) // one to one
                .collect(Collectors.toList()));
        System.out.println(numbers.stream()
                .flatMap(num -> Lists.newArrayList(num+1, num-1).stream()) // one to many
                .collect(Collectors.toList()));
        System.out.println(userList.stream()
                .map(User::getName)
                .flatMap(name -> Stream.of(((String) name).split(" ")))
                .collect(Collectors.toList())
        );

        System.out.println(userList.stream()
            .collect(Collectors.groupingBy(User::getAge,
                    Collectors.flatMapping(user -> Stream.of(user.getName().split(" ")), Collectors.toList()))));

        System.out.println(userList.stream()
                .collect(Collectors.groupingBy(User::getAge,
                        Collectors.mapping(user -> user.getName().toUpperCase(),
                        Collectors.flatMapping(name -> Stream.of(name.split(" ")), Collectors.toList())))));
    }

    private void groupOnAgeGetNamesWhereLengthMoreThan10() {
        System.out.println("groupOnAgeGetNamesWhereLengthMoreThan10");
        System.out.println(userList.stream()
            .collect(Collectors.groupingBy(User::getAge,
                    Collectors.mapping(User::getName,
                            Collectors.filtering(userName -> userName.length() > 20, Collectors.toList()))
                    )
            )
        );
    }

    private void getMaxAge1() {
        System.out.println("getMaxAge1");
//        System.out.println(userList.stream()
//                .collect(Collectors.maxBy(Comparator.comparing(User::getAge)))); // this returns the person object
        String result = userList.stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.maxBy(Comparator.comparing(User::getAge)),
                        user -> user.map(User::getName).orElse("")
                        )
                );
        // this returns the person name with a map later
        System.out.println(result);
    }

    private void getMaxAge() {
        System.out.println("getMaxAge");
        System.out.println(userList.stream()
                .mapToInt(User::getAge)
                .max().getAsInt());
        // max returns an optional unlike sum
    }

    private void getAllAgesSum1() {
        System.out.println("getAllAgesSum1");
        System.out.println(userList.stream()
            .mapToInt(User::getAge)
            .sum());
    }

    private void getUserNamesCount1() {
        System.out.println("getUserNamesCount1");
        Map<String, Integer> byName = userList.stream()
                .collect(Collectors.groupingBy(User::getName,
                        Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
                        // Collectors.collectingAndThen - first collects using collector and then uses a function
                ));
        System.out.println(byName);
    }

    private void getUserNamesCount() {
        System.out.println("getUserNamesCount");
        Map<String, Long> byName = userList.stream()
                .collect(Collectors.groupingBy(User::getName, Collectors.counting())
                       // Collectors.groupingBy - uses a function and then collects using collector
                );
        System.out.println(byName);
    }

    private void groupAgesBasedOnNames() {
        System.out.println("groupAgesBasedOnNames");
        Map<String, List<Integer>> byName = userList.stream()
                .collect(Collectors.groupingBy(User::getName,
                        Collectors.mapping(User::getAge, Collectors.toList()))
                );
        System.out.println(byName);
    }

    private void groupUsersBasedOnNames() {
        System.out.println("groupUsersBasedOnNames");
        Map<String, List<User>> byName = userList.stream()
            //.collect(Collectors.groupingBy(user -> user.getName()));
            .collect(Collectors.groupingBy(User::getName));
        System.out.println(byName);
    }

    private void groupUsersWithAgesEvenAndOdd() {
        System.out.println("groupPeopleWithAgesEvenAndOdd");
        System.out.println(userList.stream()
            .collect(Collectors.partitioningBy(user -> user.getAge() % 2 == 0)));
        // partitioning by works if there is a division based on a boolean
    }

    private void getCommaSeparatedUpperCaseNameAgedMoreThan30() {
        System.out.println("getCommaSeperatedUpperCaseNameAgedMoreThan30");
        System.out.println(userList.stream()
            .filter(user -> user.getAge() > 30)
                .map(User::getName)
                .map(String::toUpperCase)
                .collect(Collectors.joining(","))
        );
    }

    private void getNameAsKeyAndAgeAsValue() {
        System.out.println("getNameAsKeyAndAgeAsValue");
        System.out.println(userList.stream()
                //.collect(Collectors.toMap(user -> user.getName(), user -> user.getAge())));
                .collect(Collectors.toUnmodifiableMap(User::getName, User::getAge)));
    }

    private void getNamesUpperCaseWhereAgeMoreThan30() {
        System.out.println("Getting names in upper case where age is more than 30");
        /**
         * Option 1
        List<String> namesOlderThan30 = Lists.newArrayList();
        userList.stream()
                .filter(user -> user.getAge() > 30) //pure function
                .map(User::getName) //pure function
                .map(String::toUpperCase) //pure function
                .forEach(name -> namesOlderThan30.add(name)); //impure function
                // this is impure function because it's mutating - shared mutability.
                // It's taking an external list and mutates that.
                // If this was parallelized (parallelStream), the list could return different values
                // Hence always avoid shared mutability
        */
        /**
         * Option 2
         * To reduce shared mutability as like earlier case, try to use rather local mutability

        List<String> namesOlderThan30 = userList.stream()
                .filter(user -> user.getAge() > 30) //pure function
                .map(User::getName) //pure function
                .map(String::toUpperCase) //pure function
                .reduce(
                        new ArrayList<String>(),
                        (names, name) -> {
                            names.add(name); // local mutability
                            return names;
                        },
                        (names1, names2) -> {
                            names1.addAll(names2); // local mutability, not changing a shared global variable
                            return names1;
                        }
                );
        System.out.println(namesOlderThan30);
         */
        /**
         * Options 3
         * Option 2 is too verbose, rather use a library to do the same
         */
        System.out.println(userList.stream()
                .filter(user -> user.getAge() > 30) //pure function
                .map(User::getName) //pure function
                .map(String::toUpperCase)
                //.collect(Collectors.toList()));
                .collect(Collectors.toUnmodifiableList())); // use this in case immutable list is required
        //Collector is thread safe
    }

    private void getAllAgesSum() {
        System.out.println("Getting all ages sum");
        System.out.println(userList.stream()
                .map(User::getAge)
                //.reduce(0, (total, age) -> total + age));
                .reduce(0, Integer::sum));
    }

    private void getAllAges() {
        System.out.println("Getting all ages");
        userList.stream()
                .map(User::getAge)
                .forEach(System.out::println);
    }

    private void getNamesOfPeople() {
        System.out.println("Get People names");
        userList.stream()
                .map(User::getName)
                .forEach(System.out::println);
    }

    private void printUsersAbove30() {
        System.out.println("Users above 30");
        userList.stream()
                .filter(user -> user.getAge() > 30)
                .forEach(System.out::println);
    }

    public void printAllUsers() {
        System.out.println("All users");
        userList.forEach(System.out::println);
    }
}
