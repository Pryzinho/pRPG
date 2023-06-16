package br.pryzat.rpg.builds.skills.tamer.invocations;

import br.pryzat.rpg.api.characters.Character;
import br.pryzat.rpg.api.characters.classes.Beast;
import br.pryzat.rpg.api.characters.skills.Skill;

public class InvokeDavidDeer extends Skill {
    public InvokeDavidDeer(Character owner, int level) {
        super(owner, level);
        setUniqueId("invokedaviddeer");
        setDisplayName("&bCervo de David");
        setNeedMana(true);
        setNeedLife(false);
        if (level > 5) setLevel(5);
        setLevel(level);
        switch (level) {
            case 1:
                setCooldown(60 * 1000); // 60s
            case 2:
                setCooldown(55 * 1000);
            case 3:
                setCooldown(50 * 1000);
            case 4:
                setCooldown(45 * 1000);
            case 5:
                setCooldown(40 * 1000); // 40s
        }
        if (owner !=null)
        setManaCoust((int) (owner.getMaxMana() * 15 / 100));
    }

    @Override
    public void execute() {
        getOwner().getBeast().setType(Beast.Type.DAVID_DEER);
        if (getOwner().getBeast().isInvoked()) {
            getOwner().getBeast().despawn();
        }
        getOwner().getBeast().spawn();
        super.execute();
    }
}
