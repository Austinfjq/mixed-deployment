package cn.harmonycloud.test;

import cn.harmonycloud.test.NodeScore;
import cn.harmonycloud.test.ResultNode;
import cn.harmonycloud.test.ResultPod;
import cn.harmonycloud.tools.Write2ES;

import java.util.ArrayList;

public class TestTask {

    public static void run() {

        ArrayList<ResultPod> resultPodsList = new ArrayList<>();
        ArrayList<ResultNode> resultNodesList = new ArrayList<>();

        ResultPod a = new ResultPod("wordpress",
                "wordpress-wp", "wordpress-9975cc66-4vxx9");

        ArrayList<NodeScore> nodeScoreList = new ArrayList<>();
        NodeScore nodeScore1 = new NodeScore("10.10.101.65-share", "10.10.102.31", 8);
        NodeScore nodeScore2 = new NodeScore("10.10.101.66-share", "10.10.101.66", 3);
        NodeScore nodeScore3 = new NodeScore("10.10.102.17-share", "10.10.102.17", 5);
        nodeScoreList.add(nodeScore1);
        nodeScoreList.add(nodeScore2);
        nodeScoreList.add(nodeScore3);

        ResultNode b = new ResultNode("wordpress", "wordpress-wp", nodeScoreList);


        System.out.println(Write2ES.run(a,"test"));
        System.out.println(Write2ES.run(b,"test"));

    }

    public static void main(String[] args) {
        run();
    }
}
