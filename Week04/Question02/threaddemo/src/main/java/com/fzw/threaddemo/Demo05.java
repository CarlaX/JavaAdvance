package com.fzw.threaddemo;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author fzw
 * @description
 * @date 2021-05-23
 **/
public class Demo05 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("主线程开始:" + TimeUtil.currentDateTimeFormat());
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("子线程开始:" + TimeUtil.currentDateTimeFormat());
                Thread.sleep(3000);
                System.out.println("子线程结束:" + TimeUtil.currentDateTimeFormat());
                return "success";
            } catch (InterruptedException e) {
                e.printStackTrace();
                return "fail";
            }
        });
        String result = completableFuture.get();
        System.out.println("子线程结果:" + result);
        System.out.println("主线程结束:" + TimeUtil.currentDateTimeFormat());
    }

}
