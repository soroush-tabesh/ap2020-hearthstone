package ir.soroushtabesh.hearthstone.controllers.game.viewmodels;

import ir.soroushtabesh.hearthstone.controllers.game.GameController;
import ir.soroushtabesh.hearthstone.models.Card;
import ir.soroushtabesh.hearthstone.models.cards.Minion;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class MinionObject extends CardObject {

    private final IntegerProperty hp = new SimpleIntegerProperty();
    private final IntegerProperty attackPower = new SimpleIntegerProperty();

    private final BooleanProperty sleep = new SimpleBooleanProperty(true);
    private final BooleanProperty dead = new SimpleBooleanProperty(false);

    private final BooleanProperty superHP = new SimpleBooleanProperty(false);
    private final BooleanProperty superAttackPower = new SimpleBooleanProperty(false);
    private final BooleanProperty taunt = new SimpleBooleanProperty(false);
    private final BooleanProperty halo1 = new SimpleBooleanProperty(false);
    private final BooleanProperty halo2 = new SimpleBooleanProperty(false);
    private final BooleanProperty halo3 = new SimpleBooleanProperty(false);
    private final BooleanProperty halo4 = new SimpleBooleanProperty(false);

    public MinionObject(int playerId, GameController gameController, Card cardModel) {
        super(playerId, gameController, cardModel);
        attackPower.set(((Minion) cardModel).getAttackPower());
        hp.set(((Minion) cardModel).getHp());
    }

    public int getHp() {
        return hp.get();
    }

    public void setHp(int hp) {
        this.hp.set(hp);
    }

    public IntegerProperty hpProperty() {
        return hp;
    }

    public int getAttackPower() {
        return attackPower.get();
    }

    public void setAttackPower(int attackPower) {
        this.attackPower.set(attackPower);
    }

    public IntegerProperty attackPowerProperty() {
        return attackPower;
    }

    public boolean isSleep() {
        return sleep.get();
    }

    public void setSleep(boolean sleep) {
        this.sleep.set(sleep);
    }

    public BooleanProperty sleepProperty() {
        return sleep;
    }

    public boolean isDead() {
        return dead.get();
    }

    public void setDead(boolean dead) {
        this.dead.set(dead);
    }

    public BooleanProperty deadProperty() {
        return dead;
    }

    public boolean isSuperHP() {
        return superHP.get();
    }

    public void setSuperHP(boolean superHP) {
        this.superHP.set(superHP);
    }

    public BooleanProperty superHPProperty() {
        return superHP;
    }

    public boolean isSuperAttackPower() {
        return superAttackPower.get();
    }

    public void setSuperAttackPower(boolean superAttackPower) {
        this.superAttackPower.set(superAttackPower);
    }

    public BooleanProperty superAttackPowerProperty() {
        return superAttackPower;
    }

    public boolean isTaunt() {
        return taunt.get();
    }

    public void setTaunt(boolean taunt) {
        this.taunt.set(taunt);
    }

    public BooleanProperty tauntProperty() {
        return taunt;
    }

    public boolean isHalo1() {
        return halo1.get();
    }

    public void setHalo1(boolean halo1) {
        this.halo1.set(halo1);
    }

    public BooleanProperty halo1Property() {
        return halo1;
    }

    public boolean isHalo2() {
        return halo2.get();
    }

    public void setHalo2(boolean halo2) {
        this.halo2.set(halo2);
    }

    public BooleanProperty halo2Property() {
        return halo2;
    }

    public boolean isHalo3() {
        return halo3.get();
    }

    public void setHalo3(boolean halo3) {
        this.halo3.set(halo3);
    }

    public BooleanProperty halo3Property() {
        return halo3;
    }

    public boolean isHalo4() {
        return halo4.get();
    }

    public void setHalo4(boolean halo4) {
        this.halo4.set(halo4);
    }

    public BooleanProperty halo4Property() {
        return halo4;
    }
}
