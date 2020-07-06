package scripts;

import ir.soroushtabesh.hearthstone.controllers.game.scripts.MinionBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.GameObject;
import ir.soroushtabesh.hearthstone.models.ScriptModel;

import java.util.ArrayList;
import java.util.List;

public class MinionBehaviorList extends MinionBehavior {

    private transient final List<MinionBehavior> minionBehaviors = new ArrayList<>();
    private final List<ScriptModel> scriptModels = new ArrayList<>();

    public MinionBehaviorList add(MinionBehavior minionBehavior) {
        minionBehaviors.add(minionBehavior);
        scriptModels.add(new ScriptModel(minionBehavior));
        return this;
    }

    @Override
    public void onScriptAdded() {
        super.onScriptAdded();
        scriptModels.forEach(scriptModel -> minionBehaviors.add((MinionBehavior) scriptModel.getScript(getGameController())));
        minionBehaviors.forEach(minionBehavior -> {
            minionBehavior.setGameController(getGameController());
            minionBehavior.setOwnerObject(getOwnerObject());
            minionBehavior.setPlayerController(getPlayerController());
        });
        minionBehaviors.forEach(MinionBehavior::onScriptAdded);
    }

    @Override
    public boolean battleCry(GameObject gameObject) {
        super.battleCry(gameObject);
        minionBehaviors.forEach(minionBehavior -> minionBehavior.battleCry(gameObject));
        return true;
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
    public void onScriptRemoved() {
        super.onScriptRemoved();
        minionBehaviors.forEach(MinionBehavior::onScriptRemoved);
    }

    @Override
    public void onTurnStart() {
        super.onTurnStart();
        minionBehaviors.forEach(MinionBehavior::onTurnStart);
    }

    @Override
    public void onCardPlay() {
        super.onCardPlay();
        minionBehaviors.forEach(MinionBehavior::onCardPlay);
    }
}
