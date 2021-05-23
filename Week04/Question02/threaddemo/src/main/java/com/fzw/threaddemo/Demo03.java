package com.fzw.threaddemo;

import java.util.concurrent.*;

/**
 * @author fzw
 * @description
 * @date 2021-05-23
 **/
public class Demo03 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("主线程开始:" + TimeUtil.currentDateTimeFormat());
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(1);
        MyThread myThread = new MyThread();
        Future<String> future = fixedThreadPool.submit(myThread);
        String result = future.get();
        System.out.println("子线程结果:" + result);
        System.out.println("主线程结束:" + TimeUtil.currentDateTimeFormat());
        fixedThreadPool.shutdown();
    }

    private static class MyThread implements Callable<String> {

        @Override
        public String call() throws Exception {
            try {
                System.out.println("子线程开始:" + TimeUtil.currentDateTimeFormat());
                Thread.sleep(3000);
                System.out.println("子线程结束:" + TimeUtil.currentDateTimeFormat());
                return "success";
            } catch (InterruptedException e) {
                e.printStackTrace();
                return "fail";
            }
        }
    }
}
