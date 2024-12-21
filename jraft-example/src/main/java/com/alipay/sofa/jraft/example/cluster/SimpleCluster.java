package com.alipay.sofa.jraft.example.cluster;

import com.alipay.sofa.jraft.JRaftUtils;
import com.alipay.sofa.jraft.Node;
import com.alipay.sofa.jraft.RaftGroupService;
import com.alipay.sofa.jraft.conf.Configuration;
import com.alipay.sofa.jraft.entity.PeerId;
import com.alipay.sofa.jraft.option.NodeOptions;
import com.alipay.sofa.jraft.option.RaftOptions;
import com.alipay.sofa.jraft.rpc.RaftRpcServerFactory;
import com.alipay.sofa.jraft.rpc.RpcServer;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.concurrent.CompletableFuture;

/**
 * @author zxuanhong
 * @date 2024-12-21 22:04
 * @since
 */
public class SimpleCluster {
    public static void main(String[] args) {
        final RpcServer rpcServerOne = RaftRpcServerFactory.createRaftRpcServer(JRaftUtils.getPeerId("localhost:8081").getEndpoint());
        rpcServerOne.init(null);
        final RpcServer rpcServerTwo = RaftRpcServerFactory.createRaftRpcServer(JRaftUtils.getPeerId("localhost:8082").getEndpoint());
        rpcServerTwo.init(null);
        final RpcServer rpcServerThree = RaftRpcServerFactory.createRaftRpcServer(JRaftUtils.getPeerId("localhost:8083").getEndpoint());
        rpcServerThree.init(null);
        for (int i = 0; i < 1; i++) {
            int finalI = i;
            CompletableFuture<Node> nodeOne = CompletableFuture.supplyAsync(() -> createNode("data", "test", finalI, 8081, JRaftUtils.getPeerId("localhost:8081"), rpcServerOne));
            nodeOne.join();
            CompletableFuture<Node> nodeTwo = CompletableFuture.supplyAsync(() -> createNode("data", "test", finalI, 8082, JRaftUtils.getPeerId("localhost:8082"), rpcServerTwo));
            nodeTwo.join();
            CompletableFuture<Node> nodeThree = CompletableFuture.supplyAsync(() -> createNode("data", "test", finalI, 8083, JRaftUtils.getPeerId("localhost:8083"), rpcServerThree));
            nodeThree.join();
        }
    }

    public static Node createNode(String path, String raftGroupName, int partitionId, int nodeId, final PeerId serverId, RpcServer rpcServer) {
        try {
            Configuration conf = JRaftUtils.getConfiguration("localhost:8081,localhost:8082,localhost:8083");
            File file = new File(path + File.separator + nodeId + File.separator + raftGroupName + File.separator + partitionId + File.separator + serverId.toString());
            FileUtils.forceMkdir(file);
            NodeOptions nodeOptions = new NodeOptions();
//            nodeOptions.setApplyTaskMode(ApplyTaskMode.Blocking);
            nodeOptions.setInitialConf(conf);
            RaftOptions raftOptions = new RaftOptions();
            raftOptions.setDisruptorBufferSize(1024 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2);
            nodeOptions.setRaftOptions(raftOptions);
            nodeOptions.setSnapshotIntervalSecs(60);
            // here use same RPC server for raft and business. It also can be seperated generally
            // 初始化状态机
            SimpleStateMachineAdapter fsm = new SimpleStateMachineAdapter();
            // 设置状态机到启动参数
            nodeOptions.setFsm(fsm);
            // 设置存储路径
            // 日志, 必须
            nodeOptions.setLogUri(file.getPath() + File.separator + "log");
            // 元信息, 必须
            nodeOptions.setRaftMetaUri(file.getPath() + File.separator + "raft_meta");
            // snapshot, 可选, 一般都推荐
            nodeOptions.setSnapshotUri(file.getPath() + File.separator + "snapshot");
            // 初始化 raft group 服务框架
            RaftGroupService raftGroupService = new RaftGroupService(raftGroupName + "-" + partitionId, serverId, nodeOptions, rpcServer, true);

            Node start = raftGroupService.start(false);
            fsm.setStart(start);
            return start;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
