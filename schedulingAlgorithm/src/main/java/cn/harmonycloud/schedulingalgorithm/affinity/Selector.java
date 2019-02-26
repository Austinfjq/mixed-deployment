package cn.harmonycloud.schedulingalgorithm.affinity;

import java.util.List;
import java.util.Map;

public interface Selector {
    // Matches returns true if this selector matches the given set of labels.
    boolean matches(Map<String, String> labels);

    // Empty returns true if this selector does not restrict the selection space.
    boolean isEmpty();

    // Add adds requirements to the Selector
    boolean add(Requirement... r);

    // Requirements converts this interface into Requirements to expose
    // more detailed selection information.
    List<Requirement> Requirements();
}
