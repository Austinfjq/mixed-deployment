package cn.harmonycloud.schedulingalgorithm.dataobject;

import cn.harmonycloud.schedulingalgorithm.affinity.LabelSelector;

import java.util.Map;

public class PersistentVolumeClaim {
    private String name;
    private String namespace;
    private String volumeName;
    private Map<String, String> annotations;
    private String storageClassName;
    private Quantity resourceStorageRequest;//Spec.Resources.Requests["storage"]
    private LabelSelector labelSelector;
    private String VolumeMode;
    private String UID;
    private String[] accessModes;

    public String[] getAccessModes() {
        return accessModes;
    }

    public void setAccessModes(String[] accessModes) {
        this.accessModes = accessModes;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getVolumeMode() {
        return VolumeMode;
    }

    public void setVolumeMode(String volumeMode) {
        VolumeMode = volumeMode;
    }

    public LabelSelector getLabelSelector() {
        return labelSelector;
    }

    public void setLabelSelector(LabelSelector labelSelector) {
        this.labelSelector = labelSelector;
    }

    public Quantity getResourceStorageRequest() {
        return resourceStorageRequest;
    }

    public void setResourceStorageRequest(Quantity resourceStorageRequest) {
        this.resourceStorageRequest = resourceStorageRequest;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getVolumeName() {
        return volumeName;
    }

    public Map<String, String> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(Map<String, String> annotations) {
        this.annotations = annotations;
    }

    public void setVolumeName(String volumeName) {
        this.volumeName = volumeName;
    }

    public String getStorageClassName() {
        return storageClassName;
    }

    public void setStorageClassName(String storageClassName) {
        this.storageClassName = storageClassName;
    }
}
