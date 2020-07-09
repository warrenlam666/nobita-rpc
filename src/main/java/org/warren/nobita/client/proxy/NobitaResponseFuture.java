package org.warren.nobita.client.proxy;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.warren.nobita.protocol.NobitaResponse;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Data
@NoArgsConstructor
public class NobitaResponseFuture {

    private boolean isDone;

    private volatile NobitaResponse response;

    private CountDownLatch countDownLatch = new CountDownLatch(1);

    public NobitaResponse get(){
        try {
            if (!isDone)
                countDownLatch.await();
            return response;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public NobitaResponse get(long timeout, TimeUnit unit){
        try {
            if (!isDone)
                countDownLatch.await(timeout, unit);
            if (!isDone)
                setResWithTimeout();
        }catch (Exception e){
            e.printStackTrace();
        }
        return this.response;
    }

    public void setRes(NobitaResponse res){
        //变量类型声明为volatile禁止指令重排序
        this.response = res;
        countDownLatch.countDown();
        this.isDone = true;
    }

    public void setResWithTimeout(){
        this.response = NobitaResponse.builder().timeout(true).build();
        countDownLatch.countDown();
        this.isDone = true;
    }

}
