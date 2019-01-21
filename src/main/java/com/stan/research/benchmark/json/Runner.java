package com.stan.research.benchmark.json;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.stan.research.benchmark.inter.Executable;
import org.springframework.util.StopWatch;

import java.util.HashMap;
import java.util.Random;


public class Runner {


    public static void main(String[] args) throws Exception {

        StopWatch watch = new StopWatch("Json test");

        StopWatch watchAll = new StopWatch("total time");

        HashMap<String, String> data = new HashMap<>();

        int keyCount = 100;
        int runCount = 1000000;

        for (int i = 0; i < keyCount; i++) {
            data.put(String.format("key%d", new Random().nextInt()), String.format("val%d", new Random().nextInt()));
        }

        watchAll.start("total time");

        watch.start("fastJson test");
//        for (int i = 0; i < runCount; i++) {
//            JSONObject.toJSON(data);
//        }

        run(input -> JSONObject.toJSON(input), runCount, data);
        watch.stop();


        watch.start("Jackson test");

//        for (int i = 0; i < runCount; i++) {
//            ObjectMapper objectMapper = new ObjectMapper();
//            objectMapper.writeValueAsString(data);
//        }

        run(input -> new Executable() {
            @Override
            public Object execute(Object input) throws Exception {
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.writeValueAsString(input);
            }
        }, runCount, data);

        watch.stop();


        watch.start("Gson test");

//        for (int i = 0; i < runCount; i++) {
//            Gson gson = new Gson();
//            gson.toJson(data);
//        }

        run(input -> new Executable() {
            @Override
            public Object execute(Object input) {
                Gson gson = new Gson();
                return gson.toJson(input);
            }
        }, runCount, data);

        watch.stop();

        watchAll.stop();


        System.out.println(watch.prettyPrint());
        System.out.println(watchAll.prettyPrint());

    }

    private static void run(Executable executable, int count, Object data) throws Exception {
        for (int i = 0; i < count; i++) {
            executable.execute(data);
        }
    }

}
