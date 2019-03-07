package cn.harmonycloud.schedulingalgorithm.predicate.impl.add;

import cn.harmonycloud.schedulingalgorithm.basic.Cache;
//import cn.harmonycloud.schedulingalgorithm.affinity.InternalSelector;
//import cn.harmonycloud.schedulingalgorithm.affinity.NodeSelectorTerm;
//import cn.harmonycloud.schedulingalgorithm.affinity.Requirement;
//import cn.harmonycloud.schedulingalgorithm.affinity.SelectOperation;
//import cn.harmonycloud.schedulingalgorithm.affinity.Selector;
//import cn.harmonycloud.schedulingalgorithm.affinity.TopologySelectorLabelRequirement;
//import cn.harmonycloud.schedulingalgorithm.affinity.TopologySelectorTerm;
//import cn.harmonycloud.schedulingalgorithm.dataobject.BindingInfo;
import cn.harmonycloud.schedulingalgorithm.dataobject.Node;
//import cn.harmonycloud.schedulingalgorithm.dataobject.PersistentVolume;
//import cn.harmonycloud.schedulingalgorithm.dataobject.PersistentVolumeClaim;
import cn.harmonycloud.schedulingalgorithm.dataobject.Pod;
//import cn.harmonycloud.schedulingalgorithm.dataobject.Quantity;
//import cn.harmonycloud.schedulingalgorithm.dataobject.StorageClass;
//import cn.harmonycloud.schedulingalgorithm.dataobject.Volume;
import cn.harmonycloud.schedulingalgorithm.predicate.PredicateRule;
//import cn.harmonycloud.schedulingalgorithm.utils.DOUtils;
//import cn.harmonycloud.schedulingalgorithm.utils.RuleUtil;
//import cn.harmonycloud.schedulingalgorithm.utils.SelectorUtil;
//import org.apache.commons.lang.StringUtils;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Objects;
//import java.util.Set;

public class CheckVolumeBindingPredicate implements PredicateRule {
    @Override
    public boolean predicate(Pod pod, Node node, Cache cache) {
        // 已弃用
        return true;
//        if (node == null) {
//            return false;
//        }
//        try {
//            boolean[] temp = findPodVolumes(pod, node);
//            // unboundSatisfied = temp[0], boundSatisfied = temp[1]
//            return temp[0] && temp[1];
//        } catch (Exception e) {
//            return false;
//        }
    }

//    private boolean[] findPodVolumes(Pod pod, Node node) {
//        String podName = DOUtils.getPodFullName(pod);
//        boolean unboundVolumesSatisfied = true;
//        boolean boundVolumesSatisfied = true;
//
//        GetPodVolumesResult res = getPodVolumes(pod);
//        if (!res.unboundClaimsImmediate.isEmpty()) {
//            return new boolean[]{false, false};
//        }
//        if (!res.boundClaims.isEmpty()) {
//            boundVolumesSatisfied = checkBoundClaims(res.boundClaims, node, podName);
//        }
//        if (!res.unboundClaims.isEmpty()) {
//            FindMatchingVolumesResult res1 = findMatchingVolumes(pod, res.unboundClaims, node);
//            List<PersistentVolumeClaim> claimsToProvision = res1.claimsToProvision;
//            unboundVolumesSatisfied = res1.unboundVolumesSatisfied;
//            if (!unboundVolumesSatisfied) {
//                try {
//                    unboundVolumesSatisfied = checkVolumeProvisions(pod, claimsToProvision, node);
//                } catch (Exception e) {
//                    return new boolean[]{false, false};
//                }
//            }
//        }
//        return new boolean[]{unboundVolumesSatisfied, boundVolumesSatisfied};
//    }
//
//    class GetPodVolumesResult {
//        List<PersistentVolumeClaim> boundClaims;
//        List<BindingInfo> unboundClaims; // claimsToBind
//        List<PersistentVolumeClaim> unboundClaimsImmediate;
//    }
//
//    class FindMatchingVolumesResult {
//        boolean unboundVolumesSatisfied;
//        List<PersistentVolumeClaim> claimsToProvision;
//    }
//
//    class IsVolumeBoundResult {
//        boolean volumeBound;
//        PersistentVolumeClaim pvc;
//
//        IsVolumeBoundResult(boolean volumeBound, PersistentVolumeClaim pvc) {
//            this.volumeBound = volumeBound;
//            this.pvc = pvc;
//        }
//    }
//
//    private GetPodVolumesResult getPodVolumes(Pod pod) {
//        GetPodVolumesResult res = new GetPodVolumesResult();
//        res.boundClaims = new ArrayList<>();
//        res.unboundClaims = new ArrayList<>();
//        res.unboundClaimsImmediate = new ArrayList<>();
//        List<Volume> volumes = new ArrayList<>(); // TO DO get volumes of pod, Spec.Volumes
//        for (Volume vol : volumes) {
//            IsVolumeBoundResult res1 = isVolumeBound(pod.getNamespace(), vol);
//            if (res1.pvc == null) {
//                continue;
//            }
//            if (res1.volumeBound) {
//                res.boundClaims.add(res1.pvc);
//            } else {
//                boolean delayBinding = shouldDelayBinding(res1.pvc);
//                // Prebound PVCs are treated as unbound immediate binding
//                if (delayBinding && StringUtils.isEmpty(res1.pvc.getVolumeName())) {
//                    // Scheduler path
//                    BindingInfo bi = new BindingInfo();
//                    bi.setPvc(res1.pvc);
//                    res.unboundClaims.add(bi);
//                } else {
//                    // Immediate binding should have already been bound
//                    res.unboundClaimsImmediate.add(res1.pvc);
//                }
//            }
//        }
//        return res;
//    }
//
//    private IsVolumeBoundResult isVolumeBound(String namespace, Volume vol) {
//        if (vol.getPersistentVolumeClaim() == null) {
//            return new IsVolumeBoundResult(true, null);
//        }
//        String pvcName = vol.getPersistentVolumeClaim().getClaimName();
//        return isPVCBound(namespace, pvcName);
//    }
//
//    private static final String annBindCompleted = "pv.kubernetes.io/bind-completed";
//
//    private IsVolumeBoundResult isPVCBound(String namespace, String pvcName) {
//        PersistentVolumeClaim claim = new PersistentVolumeClaim();
//        claim.setName(pvcName);
//        claim.setNamespace(namespace);
//        String pvcKey = getPVCName(claim);
//        PersistentVolumeClaim pvc = new PersistentVolumeClaim();// TO DO pvc = cache.pvcMap.get(pvcKey)。pvcMap相当于k8s /pkg/controller/volume/persistentvolume/scheduler_binder.go的volumeBinder的pvcCache
//        if (pvc == null) {
//            return new IsVolumeBoundResult(false, null);
//        }
//        String pvName = pvc.getVolumeName();
//        if (!StringUtils.isEmpty(pvName)) {
//            if (pvc.getAnnotations().containsKey(annBindCompleted)) {
//                return new IsVolumeBoundResult(true, pvc);
//            } else {
//                return new IsVolumeBoundResult(false, pvc);
//            }
//        }
//        return new IsVolumeBoundResult(false, null);
//    }
//
//    private String getPVCName(PersistentVolumeClaim pvc) {
//        return pvc.getNamespace() + "/" + pvc.getName();
//    }
//
//    private static final String annSelectedNode = "volume.kubernetes.io/selected-node";
//    private static final String VolumeBindingWaitForFirstConsumer = "WaitForFirstConsumer";
//
//    private boolean shouldDelayBinding(PersistentVolumeClaim claim) {
//        if (claim.getAnnotations().containsKey(annSelectedNode)) {
//            return false;
//        }
//        String className = getPersistentVolumeClaimClass(claim);
//        if (StringUtils.isEmpty(className)) {
//            return false;
//        }
//        StorageClass clazz = new StorageClass();// TO DO ctrl.classLister.Get(className);
//        if (clazz == null || clazz.getVolumeBindingMode() == null) {
//            return false;
//        }
//        return VolumeBindingWaitForFirstConsumer.equals(clazz.getVolumeBindingMode());
//    }
//
//    private static final String BetaStorageClassAnnotation = "volume.beta.kubernetes.io/storage-class";
//
//    private String getPersistentVolumeClaimClass(PersistentVolumeClaim claim) {
//        if (claim.getAnnotations().containsKey(BetaStorageClassAnnotation)) {
//            return claim.getAnnotations().get(BetaStorageClassAnnotation);
//        }
//        if (claim.getStorageClassName() != null) {
//            return claim.getStorageClassName();
//        }
//        return null;
//    }
//
//    private boolean checkBoundClaims(List<PersistentVolumeClaim> claims, Node node, String podName) {
//        for (PersistentVolumeClaim pvc : claims) {
//            String pvName = pvc.getVolumeName();
//            PersistentVolume pv = new PersistentVolume();//TO DO pvCache.GetPV(pvName)
//            if (pv == null) {
//                return false;
//            }
//            Map<String, String> nodeLabels = new HashMap<>();// TO DO node.Labels
//            if (!checkVolumeNodeAffinity(pv, nodeLabels)) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    private boolean checkVolumeNodeAffinity(PersistentVolume pv, Map<String, String> nodeLabels) {
//        if (pv.getNodeAffinity() == null) {
//            return true;
//        }
//        if (pv.getNodeAffinity().getRequired() != null) {
//            List<NodeSelectorTerm> terms = pv.getNodeAffinity().getRequired().getNodeSelectorTerms();
//            return RuleUtil.matchNodeSelectorTerms(terms, nodeLabels, new HashMap<>());
//        }
//        return true;
//    }
//
//    private FindMatchingVolumesResult findMatchingVolumes(Pod pod, List<BindingInfo> claimsToBind, Node node) {
//        FindMatchingVolumesResult res = new FindMatchingVolumesResult();
//        String podName = getPodName(pod);
//        claimsToBind.sort((x, y) -> x.getPvc().getResourceStorageRequest().compareTo(y.getPvc().getResourceStorageRequest()));
//        Map<String, PersistentVolume> chosenPVs = new HashMap<>();
//        res.unboundVolumesSatisfied = true;
//        List<BindingInfo> matchedClaims = new ArrayList<>();
//        for (BindingInfo bindingInfo : claimsToBind) {
//            // Get storage class name from each PVC
//            String storageClassName = bindingInfo.getPvc().getStorageClassName();
//            List<PersistentVolume> allPVs = new ArrayList<>(); // TO DO allPVs := b.pvCache.ListPVs(storageClassName)
//            String pvcName = getPVCName(bindingInfo.getPvc());
//
//            // Find a matching PV
//            bindingInfo.setPv(findMatchingVolume(bindingInfo.getPvc(), allPVs, node, chosenPVs, true));
//            if (bindingInfo.getPv() == null) {
//                res.claimsToProvision.add(bindingInfo.getPvc());
//                res.unboundVolumesSatisfied = false;
//                continue;
//            }
//
//            // matching PV needs to be excluded so we don't select it again
//            chosenPVs.put(bindingInfo.getPv().getName(), bindingInfo.getPv());
//            matchedClaims.add(bindingInfo);
//        }
//        if (!matchedClaims.isEmpty()) {
//            // TO DO update pod binding cache. It seems that we do not need this cache.
//            // Mark cache with all the matches for each PVC for this node
//            // b.podBindingCache.UpdateBindings(pod, node.Name, matchedClaims)
//        }
//        return res;
//    }
//
//    private String getPodName(Pod pod) {
//        return pod.getNamespace() + "/" + pod.getPodName();
//    }
//
//    private PersistentVolume findMatchingVolume(PersistentVolumeClaim claim, List<PersistentVolume> volumes, Node node,
//                                                Map<String, PersistentVolume> excludedVolumes, boolean delayBinding) {
//        PersistentVolume smallestVolume = null;
//        Quantity smallestVolumeQty = null;
//        Quantity requestedQty = claim.getResourceStorageRequest();
//        String requestedClass = getPersistentVolumeClaimClass(claim);
//
//        Selector selector = null;
//        if (claim.getLabelSelector() != null) {
//            Selector internalSelector;
//            try {
//                internalSelector = RuleUtil.labelSelectorAsSelector(claim.getLabelSelector());
//            } catch (Exception e) {
//                return null;
//            }
//            if (internalSelector == null) {
//                return null;
//            }
//            selector = internalSelector;
//        }
//
//        // Go through all available volumes with two goals:
//        // - find a volume that is either pre-bound by user or dynamically
//        //   provisioned for this claim. Because of this we need to loop through
//        //   all volumes.
//        // - find the smallest matching one if there is no volume pre-bound to
//        //   the claim.
//        for (PersistentVolume volume : volumes) {
//            if (excludedVolumes.containsKey(volume.getName())) {
//                continue;
//            }
//
//            Quantity volumeQty = volume.getResourceStorageCapacity();
//
//            // check if volumeModes do not match (feature gate protected)
//            boolean isMismatch = checkVolumeModeMismatches(claim, volume);
//            if (isMismatch) {
//                continue;
//            }
//            // TO DO 检查全局开关StorageObjectInUseProtection和DeletionTimeStamp
//            // check if PV's DeletionTimeStamp is set, if so, skip this volume.
////            if utilfeature.DefaultFeatureGate.Enabled(features.StorageObjectInUseProtection) {
////                if volume.ObjectMeta.DeletionTimestamp != nil {
////                    continue
////                }
////            }
//            boolean nodeAffinityValid = true;
//            if (node != null) {
//                // Scheduler path, check that the PV NodeAffinity
//                // is satisfied by the node
//                Map<String, String> nodeLabels = new HashMap<>(); // TO DO node labels
//                nodeAffinityValid = checkNodeAffinity(volume, nodeLabels);
//            }
//
//            if (isVolumeBoundToClaim(volume, claim)) {
//                // this claim and volume are pre-bound; return
//                // the volume if the size request is satisfied,
//                // otherwise continue searching for a match
//                if (volumeQty.compareTo(requestedQty) < 0) {
//                    continue;
//                }
//
//                // If PV node affinity is invalid, return no match.
//                // This means the prebound PV (and therefore PVC)
//                // is not suitable for this node.
//                if (!nodeAffinityValid) {
//                    return null;
//                }
//                return volume;
//            }
//
//            if (node == null && delayBinding) {
//                // PV controller does not bind this claim.
//                // Scheduler will handle binding unbound volumes
//                // Scheduler path will have node != nil
//                continue;
//            }
//
//            // filter out:
//            // - volumes in non-available phase
//            // - volumes bound to another claim
//            // - volumes whose labels don't match the claim's selector, if specified
//            // - volumes in Class that is not requested
//            // - volumes whose NodeAffinity does not match the node
//            if (!volumeAvailable.equals(volume.getStatusPhase())) {
//                // We ignore volumes in non-available phase, because volumes that
//                // satisfies matching criteria will be updated to available, binding
//                // them now has high chance of encountering unnecessary failures
//                // due to API conflicts.
//                continue;
//            } else if (!volume.getClaimRefNull()) {
//                continue;
//            } else if (selector != null && !selector.matches(volume.getLabels())) {
//                continue;
//            }
//            if (!Objects.equals(requestedClass, getPersistentVolumeClass(volume))) {
//                continue;
//            }
//            if (!nodeAffinityValid) {
//                continue;
//            }
//
//            if (node != null) {
//                // Scheduler path
//                // Check that the access modes match
//                if (!checkAccessModes(claim, volume)) {
//                    continue;
//                }
//            }
//
//            if (volumeQty.compareTo(requestedQty) >= 0) {
//                if (smallestVolume == null || smallestVolumeQty.compareTo(volumeQty) > 0) {
//                    smallestVolume = volume;
//                    smallestVolumeQty = volumeQty;
//                }
//            }
//        }
//
//        if (smallestVolume != null) {
//            // Found a matching volume
//            return smallestVolume;
//        }
//        return null;
//    }
//
//    private boolean checkAccessModes(PersistentVolumeClaim claim, PersistentVolume volume) {
//        Set<String> pvModesSet = new HashSet<>(Arrays.asList(volume.getAccessModes()));
//        for (String mode : claim.getAccessModes()) {
//            if (!pvModesSet.contains(mode)) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    private String getPersistentVolumeClass(PersistentVolume volume) {
//        if (volume.getBetaStorageClassAnnotation() != null) {
//            return volume.getBetaStorageClassAnnotation();
//        } else {
//            return volume.getStorageClassName();
//        }
//    }
//
//    private static final String volumeAvailable = "available";
//
//    private boolean checkNodeAffinity(PersistentVolume pv, Map<String, String> nodeLabels) {
//        return checkVolumeNodeAffinity(pv, nodeLabels);
//    }
//
//    private boolean isVolumeBoundToClaim(PersistentVolume volume, PersistentVolumeClaim claim) {
//        if (!claim.getName().equals(volume.getClaimRefName())) {
//            return false;
//        }
//        if (!claim.getNamespace().equals(volume.getClaimRefNamespace())) {
//            return false;
//        }
//        if (volume.getClaimRefUID() != null && claim.getUID().equals(volume.getClaimRefUID())) {
//            return false;
//        }
//        return true;
//    }
//
//    private static final String persistentVolumeFilesystem = "Filesystem";
//
//    private boolean checkVolumeModeMismatches(PersistentVolumeClaim pvc, PersistentVolume pv) {
//        // TO DO : 全局开关
//        // if !utilfeature.DefaultFeatureGate.Enabled(features.BlockVolume) {
//        //		return false, nil
//        //	}
//        String requestedVolumeMode = persistentVolumeFilesystem;
//        if (pvc.getVolumeMode() != null) {
//            requestedVolumeMode = pvc.getVolumeMode();
//        }
//        String pvVolumeMode = persistentVolumeFilesystem;
//        if (pv.getVolumeMode() != null) {
//            pvVolumeMode = pv.getVolumeMode();
//        }
//        return !requestedVolumeMode.equals(pvVolumeMode);
//    }
//
//    private static final String notSupportedProvisioner = "kubernetes.io/no-provisioner";
//    private boolean checkVolumeProvisions(Pod pod, List<PersistentVolumeClaim> claimsToProvision, Node node) throws Exception {
//        String podName = getPodName(pod);
//        List<PersistentVolumeClaim> provisionedClaims = new ArrayList<>();
//        for (PersistentVolumeClaim claim : claimsToProvision) {
//            String pvcName = getPVCName(claim);
//            String className = getPersistentVolumeClaimClass(claim);
//            if (className == null) {
//                throw new Exception();
//            }
//            StorageClass clazz = new StorageClass();//TO DO ctrl.classLister.Get(className)
//            if (clazz == null) {
//                throw new Exception();
//            }
//            String provisioner = clazz.getProvisioner();
//            if (provisioner == null || notSupportedProvisioner.equals(provisioner)) {
//                return false;
//            }
//            // Check if the node can satisfy the topology requirement in the class
//            Map<String, String> nodeLabels = new HashMap<>(); //to do node label
//            if (matchTopologySelectorTerms(clazz.getAllowedTopologies(), nodeLabels)) {
//                return false;
//            }
//            provisionedClaims.add(claim);
//        }
//        return true;
//    }
//
//    private boolean matchTopologySelectorTerms(List<TopologySelectorTerm> topologySelectorTerms, Map<String, String> lbls) {
//        if (topologySelectorTerms.isEmpty()) {
//            return true;
//        }
//
//        for (TopologySelectorTerm req : topologySelectorTerms) {
//            if (req.getMatchLabelExpressions().isEmpty()) {
//                continue;
//            }
//            try {
//                Selector labelSelector = topologySelectorRequirementsAsSelector(req.getMatchLabelExpressions());
//                if (labelSelector == null || !labelSelector.matches(lbls)) {
//                    continue;
//                }
//            } catch (Exception e) {
//                continue;
//            }
//            return true;
//        }
//        return false;
//    }
//
//    private Selector topologySelectorRequirementsAsSelector(List<TopologySelectorLabelRequirement> tsm) {
//        if (tsm.isEmpty()) {
//            return RuleUtil.newNothingSelector();
//        }
//        Selector selector = new InternalSelector();
//        for (TopologySelectorLabelRequirement expr : tsm) {
//            try {
//                Requirement r = SelectorUtil.newRequirement(expr.getKey(), SelectOperation.In, expr.getValues());
//                selector.add(r);
//            } catch (Exception e) {
//                return null;
//            }
//        }
//        return selector;
//    }
}
