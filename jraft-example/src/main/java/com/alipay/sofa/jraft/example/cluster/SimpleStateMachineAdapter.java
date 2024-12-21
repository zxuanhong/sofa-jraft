package com.alipay.sofa.jraft.example.cluster;

import com.alipay.sofa.jraft.Iterator;
import com.alipay.sofa.jraft.Node;
import com.alipay.sofa.jraft.Status;
import com.alipay.sofa.jraft.conf.Configuration;
import com.alipay.sofa.jraft.core.StateMachineAdapter;
import com.alipay.sofa.jraft.core.TimerManager;
import com.alipay.sofa.jraft.entity.LeaderChangeContext;
import com.alipay.sofa.jraft.entity.Task;
import com.alipay.sofa.jraft.error.RaftException;

import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

/**
 * @author zxuanhong
 * @date 2024-12-21 22:08
 * @since
 */
public class SimpleStateMachineAdapter extends StateMachineAdapter {
    private Node start;
    private boolean leader;

    private final TimerManager timerManager = new TimerManager(2, "business");

    public void setStart(final Node start) {
        this.start = start;
        sendLog();
    }


    private void sendLog() {
        timerManager.scheduleAtFixedRate(() -> {
            if (leader) {
                long startTime = System.currentTimeMillis();
                for (int i = 0; i < 10000; i++) {
                    Task task = new Task();
                    task.setData(ByteBuffer.wrap((start.getGroupId() + "-测试数据").getBytes()));
                    start.apply(task);
                }
                long endTime = System.currentTimeMillis();
                System.out.println("-----耗时------" + (endTime - startTime) / 1000 + "s");
            }
        }, 60, 1, TimeUnit.SECONDS);
    }

    @Override
    public void onApply(Iterator iter) {
        if (iter.hasNext()) {
            ByteBuffer next = iter.next();
            System.out.println("----收到数据----" + new String(next.array()));
        }
    }

    @Override
    public void onLeaderStart(long term) {
        this.leader = true;
        super.onLeaderStart(term);
    }

    @Override
    public void onLeaderStop(Status status) {
        super.onLeaderStop(status);
    }

    @Override
    public void onError(RaftException e) {
        super.onError(e);
    }

    @Override
    public void onStopFollowing(LeaderChangeContext ctx) {
        super.onStopFollowing(ctx);
    }

    @Override
    public void onStartFollowing(LeaderChangeContext ctx) {
        super.onStartFollowing(ctx);
    }

    @Override
    public void onConfigurationCommitted(Configuration conf) {
        super.onConfigurationCommitted(conf);
    }
}
