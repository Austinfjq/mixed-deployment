package cn.harmonycloud.schedulingalgorithm.dataobject;

public class PersistentVolumeClaimVolumeSource {
    private String claimName;

    public String getClaimName() {
        return claimName;
    }

    public void setClaimName(String claimName) {
        this.claimName = claimName;
    }
}
