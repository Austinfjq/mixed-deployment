package cn.harmonycloud.beans;

/**
 * @classname：SchedulableNode
 * @author：WANGYUZHONG
 * @date：2019/4/10 14:22
 * @description:TODO
 * @version:1.0
 **/
public class SchedulableNode {
    private String nodeHostName;
    private int score;

    public String getNodeHostName() {
        return nodeHostName;
    }

    public void setNodeHostName(String nodeHostName) {
        this.nodeHostName = nodeHostName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
