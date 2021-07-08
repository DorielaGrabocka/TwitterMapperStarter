package filters;

import twitter4j.Status;


public class OrFilter extends FilterWithTwoChildren{


    public OrFilter(Filter leftChild, Filter rightChild) {
        super(leftChild, rightChild);
    }

    @Override
    public boolean matches(Status s) {
        return rightChild.matches(s) || leftChild.matches(s);
    }

    @Override
    public String toString() {
        return "("+leftChild.toString()+" or "+rightChild.toString()+")";
    }
}
