package com.fzw.threaddemo;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author fzw
 * @description
 * @date 2021-05-23
 **/
public class Demo04 {
    public static CompletableFuture<String> completableFuture = new CompletableFuture<>();

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("主线程开始:" + TimeUtil.currentDateTimeFormat());
        MyThread myThread = new MyThread();
        myThread.start();
        String result = completableFuture.get();
        System.out.println("子线程结果:" + result);
        System.out.println("主线程结束:" + TimeUtil.currentDateTimeFormat());
    }

    private static class MyThread extends Thread {
        @Override
        public void run() {
            try {
                System.out.println("子线程开始:" + TimeUtil.currentDateTimeFormat());
                Thread.sleep(3000);
                completableFuture.complete("success");
                System.out.println("子线程结束:" + TimeUtil.currentDateTimeFormat());
            } catch (InterruptedException e) {
                e.printStackTrace();
                completableFuture.complete("fail");
            }
        }
    }
}
