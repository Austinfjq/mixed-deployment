package cn.harmonycloud.schedulingalgorithm.dataobject;

import cn.harmonycloud.schedulingalgorithm.affinity.TopologySelectorTerm;

import java.util.List;

public class StorageClass {
    private String volumeBindingMode;
    private String provisioner;
    private List<TopologySelectorTerm> allowedTopologies;

    public List<TopologySelectorTerm> getAllowedTopologies() {
        return allowedTopologies;
    }

    public void setAllowedTopologies(List<TopologySelectorTerm> allowedTopologies) {
        this.allowedTopologies = allowedTopologies;
    }

    public String getProvisioner() {
        return provisioner;
    }

    public void setProvisioner(String provisioner) {
        this.provisioner = provisioner;
    }

    public String getVolumeBindingMode() {
        return volumeBindingMode;
    }

    public void setVolumeBindingMode(String volumeBindingMode) {
        this.volumeBindingMode = volumeBindingMode;
    }
}
