package br.pryzat.rpg.api.characters.skills;

import java.util.List;

public class Branches {
    private List<Branch> branches;

    public Branches(List<Branch> branches) {
        this.branches = branches;
    }

    public Branch getBranch(int index) {
        return branches.get(index - 1);
    }
    public List<Branch> toList(){
        return branches;
    }
}
