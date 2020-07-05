package ir.soroushtabesh.hearthstone.controllers.game.scripts.custom;

import ir.soroushtabesh.hearthstone.controllers.game.scripts.MinionBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.GameObject;

import java.util.ArrayList;
import java.util.List;

public class MinionBehaviorList extends MinionBehavior {

    private final List<MinionBehavior> minionBehaviors = new ArrayList<>();

    public MinionBehaviorList add(MinionBehavior minionBehavior) {
        minionBehaviors.add(minionBehavior);
        return this;
    }

    @Override
    public void battleCry(GameObject gameObject) {
        super.battleCry(gameObject);
        minionBehaviors.forEach(minionBehavior -> minionBehavior.battleCry(gameObject));
    }

    @Override
    public void deathRattle() {
        super.deathRattle();
        minionBehaviors.forEach(MinionBehavior::deathRattle);
    }

    @Override
    public void onDamageTaken() {
        super.onDamageTaken();
        minionBehaviors.forEach(MinionBehavior::onDamageTaken);
    }

    @Override
    public void onAttackDone() {
        super.onAttackDone();
        minionBehaviors.forEach(MinionBehavior::onAttackDone);
    }

    @Override
    public void onAttackEffect(GameObject gameObject) {
        super.onAttackEffect(gameObject);
        minionBehaviors.forEach(minionBehavior -> minionBehavior.onAttackEffect(gameObject));
    }

    @Override
    public void onTurnEnd() {
        super.onTurnEnd();
        minionBehaviors.forEach(MinionBehavior::onTurnEnd);
    }

    @Override
    public void onScriptAdded() {
        super.onScriptAdded();
        minionBehaviors.forEach(MinionBehavior::onScriptAdded);
    }

    @Override
    public void onScriptRemoved() {
        super.onScriptRemoved();
        minionBehaviors.forEach(MinionBehavior::onScriptRemoved);
    }

    @Override
    public void onTurnStart() {
        super.onTurnStart();
        minionBehaviors.forEach(MinionBehavior::onTurnStart);
    }
}
