package cn.harmonycloud.schedulingalgorithm.affinity;

import java.util.Set;

public class AffinityTermProperties {
    private Set<String> namespaces;
    private Selector selector;

    public AffinityTermProperties(Set<String> namespaces, Selector selector) {
        super();
        this.namespaces = namespaces;
        this.selector = selector;
    }

    public Set<String> getNamespaces() {
        return namespaces;
    }

    public void setNamespaces(Set<String> namespaces) {
        this.namespaces = namespaces;
    }

    public Selector getSelector() {
        return selector;
    }

    public void setSelector(Selector selector) {
        this.selector = selector;
    }
}
