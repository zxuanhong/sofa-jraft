package com.alipay.sofa.jraft.example.cluster;

import com.alipay.sofa.jraft.JRaftUtils;
import com.alipay.sofa.jraft.Node;
import com.alipay.sofa.jraft.RaftGroupService;
import com.alipay.sofa.jraft.conf.Configuration;
import com.alipay.sofa.jraft.entity.PeerId;
import com.alipay.sofa.jraft.example.counter.rpc.CounterGrpcHelper;
import com.alipay.sofa.jraft.option.NodeOptions;
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

        CompletableFuture<Node> nodeOne = CompletableFuture.supplyAsync(() -> createNode(8081, JRaftUtils.getPeerId("localhost:8081")));
        nodeOne.join();
        CompletableFuture<Node> nodeTwo = CompletableFuture.supplyAsync(() -> createNode(8082, JRaftUtils.getPeerId("localhost:8082")));
        nodeTwo.join();
        CompletableFuture<Node> nodeThree = CompletableFuture.supplyAsync(() -> createNode(8083, JRaftUtils.getPeerId("localhost:8083")));
        nodeThree.join();
    }

    public static Node createNode(int nodeId, final PeerId serverId) {
        try {
            Configuration conf = JRaftUtils.getConfiguration("localhost:8081,localhost:8082,localhost:8083");
            File file = new File("./data/" + nodeId);
            FileUtils.forceMkdir(file);
            NodeOptions nodeOptions = new NodeOptions();
            nodeOptions.setInitialConf(conf);
            // here use same RPC server for raft and business. It also can be seperated generally
            final RpcServer rpcServer = RaftRpcServerFactory.createRaftRpcServer(serverId.getEndpoint());
            // GrpcServer need init marshaller
            CounterGrpcHelper.initGRpc();
            CounterGrpcHelper.setRpcServer(rpcServer);
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
            RaftGroupService raftGroupService = new RaftGroupService("test", serverId, nodeOptions, rpcServer);
            fsm.setStart(raftGroupService.getRaftNode());

            return raftGroupService.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
