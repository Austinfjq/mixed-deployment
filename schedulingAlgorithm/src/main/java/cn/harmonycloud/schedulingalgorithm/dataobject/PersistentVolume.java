package cn.harmonycloud.schedulingalgorithm.dataobject;

import java.util.Map;

public class PersistentVolume {
    private VolumeNodeAffinity nodeAffinity;// PersistentVolume.Spec.NodeAffinity
    private String name; // name
    private Quantity resourceStorageCapacity; // Spec.Capacity[v1.ResourceStorage]
    private String volumeMode;
    //  ObjectMeta.DeletionTimeStamp
    private Boolean claimRefNull; //  claimRefExist = Spec.ClaimRef == null
    private String claimRefUID; //  Spec.ClaimRef.UID
    private String claimRefName; //  Spec.ClaimRef.Name
    private String claimRefNamespace; //  Spec.ClaimRef.Namespace
    private String statusPhase; //  Status.Phase
    private Map<String, String> labels; // Labels
    private String betaStorageClassAnnotation;//  Annotations[v1.BetaStorageClassAnnotation]
    private String storageClassName; //  Spec.StorageClassName
    private String[] accessModes;//  Spec.AccessModes

    public String[] getAccessModes() {
        return accessModes;
    }

    public void setAccessModes(String[] accessModes) {
        this.accessModes = accessModes;
    }

    public String getStorageClassName() {
        return storageClassName;
    }

    public void setStorageClassName(String storageClassName) {
        this.storageClassName = storageClassName;
    }

    public String getBetaStorageClassAnnotation() {
        return betaStorageClassAnnotation;
    }

    public void setBetaStorageClassAnnotation(String betaStorageClassAnnotation) {
        this.betaStorageClassAnnotation = betaStorageClassAnnotation;
    }

    public Map<String, String> getLabels() {
        return labels;
    }

    public void setLabels(Map<String, String> labels) {
        this.labels = labels;
    }

    public Boolean getClaimRefNull() {
        return claimRefNull;
    }

    public void setClaimRefNull(Boolean claimRefNull) {
        this.claimRefNull = claimRefNull;
    }

    public String getStatusPhase() {
        return statusPhase;
    }

    public void setStatusPhase(String statusPhase) {
        this.statusPhase = statusPhase;
    }

    public String getClaimRefNamespace() {
        return claimRefNamespace;
    }

    public void setClaimRefNamespace(String claimRefNamespace) {
        this.claimRefNamespace = claimRefNamespace;
    }

    public String getClaimRefName() {
        return claimRefName;
    }

    public void setClaimRefName(String claimRefName) {
        this.claimRefName = claimRefName;
    }

    public String getClaimRefUID() {
        return claimRefUID;
    }

    public void setClaimRefUID(String claimRefUID) {
        this.claimRefUID = claimRefUID;
    }

    public Quantity getResourceStorageCapacity() {
        return resourceStorageCapacity;
    }

    public void setResourceStorageCapacity(Quantity resourceStorageCapacity) {
        this.resourceStorageCapacity = resourceStorageCapacity;
    }

    public String getVolumeMode() {
        return volumeMode;
    }

    public void setVolumeMode(String volumeMode) {
        this.volumeMode = volumeMode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public VolumeNodeAffinity getNodeAffinity() {
        return nodeAffinity;
    }

    public void setNodeAffinity(VolumeNodeAffinity nodeAffinity) {
        this.nodeAffinity = nodeAffinity;
    }
}

