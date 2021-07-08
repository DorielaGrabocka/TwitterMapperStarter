package filters;

import twitter4j.Status;

import java.util.ArrayList;
import java.util.List;

public abstract class FilterWithTwoChildren implements Filter{

    protected Filter leftChild, rightChild;

    public FilterWithTwoChildren(Filter leftChild, Filter rightChild) {
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    @Override
    public abstract boolean matches(Status s);


    @Override
    public List<String> terms() {
        List<String> terms = new ArrayList<>();
        terms.addAll(rightChild.terms());
        terms.addAll(leftChild.terms());
        return terms;
    }
}
