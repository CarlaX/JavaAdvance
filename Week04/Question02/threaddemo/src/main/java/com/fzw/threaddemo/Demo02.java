package com.fzw.threaddemo;

/**
 * @author fzw
 * @description
 * @date 2021-05-23
 **/
public class Demo02 {
    public volatile static boolean finish = false;
    public static String result = null;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("主线程开始:" + TimeUtil.currentDateTimeFormat());
        MyThread myThread = new MyThread();
        Thread thread = new Thread(myThread);
        thread.start();
        while (!finish) {
        }
        System.out.println("子线程结果:" + result);
        System.out.println("主线程结束:" + TimeUtil.currentDateTimeFormat());
    }

    private static class MyThread implements Runnable {
        @Override
        public void run() {
            try {
                System.out.println("子线程开始:" + TimeUtil.currentDateTimeFormat());
                Thread.sleep(3000);
                result = "success";
                finish = true;
                System.out.println("子线程结束:" + TimeUtil.currentDateTimeFormat());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
