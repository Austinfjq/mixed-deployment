package cn.harmonycloud.schedulingalgorithm.dataobject;

import cn.harmonycloud.schedulingalgorithm.affinity.NodeSelector;

public class VolumeNodeAffinity {
    private NodeSelector required;

    public NodeSelector getRequired() {
        return required;
    }

    public void setRequired(NodeSelector required) {
        this.required = required;
    }
}
