package com.fzw.threaddemo;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author fzw
 * @description
 * @date 2021-05-24
 **/
public class Demo06 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("主线程开始:" + TimeUtil.currentDateTimeFormat());
        FutureTask<String> futureTask = new FutureTask<>(() -> {
            System.out.println("子线程开始:" + TimeUtil.currentDateTimeFormat());
            try {
                Thread.sleep(3000);
                System.out.println("子线程结束:" + TimeUtil.currentDateTimeFormat());
                return "success";
            } catch (InterruptedException e) {
                e.printStackTrace();
                return "fail";
            }
        });
        Thread thread = new Thread(futureTask);
        thread.start();
        String result = futureTask.get();
        System.out.println("子线程结果:" + result);
        System.out.println("主线程结束:" + TimeUtil.currentDateTimeFormat());
    }

    private static class MyThread implements Runnable {

        private String result;

        public MyThread(String result) {
            this.result = result;
        }

        @Override
        public void run() {

        }
    }

}
