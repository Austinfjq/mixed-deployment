package cn.harmonycloud.schedulingalgorithm.dataobject;

public class Volume {
    private PersistentVolumeClaimVolumeSource PersistentVolumeClaim;

    public PersistentVolumeClaimVolumeSource getPersistentVolumeClaim() {
        return PersistentVolumeClaim;
    }

    public void setPersistentVolumeClaim(PersistentVolumeClaimVolumeSource persistentVolumeClaim) {
        PersistentVolumeClaim = persistentVolumeClaim;
    }
}
