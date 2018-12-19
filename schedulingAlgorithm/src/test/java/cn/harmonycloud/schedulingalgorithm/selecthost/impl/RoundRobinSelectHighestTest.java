package cn.harmonycloud.schedulingalgorithm.selecthost.impl;

import cn.harmonycloud.schedulingalgorithm.dataobject.HostPriority;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class RoundRobinSelectHighestTest {
    @Test
    public void test() {
        RoundRobinSelectHighest rule = new RoundRobinSelectHighest();
        for (int i = 0; i < 100; i++) {
            List<HostPriority> list = new ArrayList<>();
            list.add(new HostPriority("1", 0.5D));
            list.add(new HostPriority("2", 0.9D));
            list.add(new HostPriority("3", 1.0D));
            list.add(new HostPriority("4", 1.0D));
            list.add(new HostPriority("5", 1.0D));
            System.out.println(rule.selectHost(list).getHost());
        }
    }
}
