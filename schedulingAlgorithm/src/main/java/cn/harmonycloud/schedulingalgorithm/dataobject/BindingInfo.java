package cn.harmonycloud.schedulingalgorithm.dataobject;

public class BindingInfo {
    private PersistentVolume pv;
    private PersistentVolumeClaim pvc;

    public PersistentVolumeClaim getPvc() {
        return pvc;
    }

    public void setPvc(PersistentVolumeClaim pvc) {
        this.pvc = pvc;
    }

    public PersistentVolume getPv() {
        return pv;
    }

    public void setPv(PersistentVolume pv) {
        this.pv = pv;
    }


}
