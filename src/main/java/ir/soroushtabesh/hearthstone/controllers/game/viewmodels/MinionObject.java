package ir.soroushtabesh.hearthstone.controllers.game.viewmodels;

import ir.soroushtabesh.hearthstone.controllers.game.GameController;
import ir.soroushtabesh.hearthstone.models.cards.Minion;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class MinionObject extends CardObject {

    private final SimpleIntegerProperty hp = new SimpleIntegerProperty();
    private final SimpleIntegerProperty attackPower = new SimpleIntegerProperty();

    private final SimpleBooleanProperty sleep = new SimpleBooleanProperty(false);
    private final SimpleBooleanProperty dead = new SimpleBooleanProperty(false);

    private final SimpleBooleanProperty superHP = new SimpleBooleanProperty(false);
    private final SimpleBooleanProperty superAttackPower = new SimpleBooleanProperty(false);
    private final SimpleBooleanProperty taunt = new SimpleBooleanProperty(false);
    private final SimpleBooleanProperty immune = new SimpleBooleanProperty(false);
    private final SimpleBooleanProperty stealth = new SimpleBooleanProperty(false);
    private final SimpleBooleanProperty buffed = new SimpleBooleanProperty(false);
    private final SimpleBooleanProperty halo4 = new SimpleBooleanProperty(false);
    private final SimpleBooleanProperty canAttackHero = new SimpleBooleanProperty(true);

    public MinionObject(int playerId, GameController gameController, Minion cardModel) {
        super(playerId, gameController, cardModel);
        attackPower.set(cardModel.getAttackPower());
        hp.set(cardModel.getHp());
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

    public boolean hasTaunt() {
        return taunt.get();
    }

    public void setTaunt(boolean taunt) {
        this.taunt.set(taunt);
    }

    public BooleanProperty tauntProperty() {
        return taunt;
    }

    public boolean hasImmunity() {
        return immune.get();
    }

    public void setImmune(boolean immune) {
        this.immune.set(immune);
    }

    public BooleanProperty immuneProperty() {
        return immune;
    }

    public boolean hasStealth() {
        return stealth.get();
    }

    public void setStealth(boolean stealth) {
        this.stealth.set(stealth);
    }

    public BooleanProperty stealthProperty() {
        return stealth;
    }

    public boolean isBuffed() {
        return buffed.get();
    }

    public void setBuffed(boolean buffed) {
        this.buffed.set(buffed);
    }

    public BooleanProperty buffedProperty() {
        return buffed;
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

    public boolean getCanAttackHero() {
        return canAttackHero.get();
    }

    public void setCanAttackHero(boolean canAttackHero) {
        this.canAttackHero.set(canAttackHero);
    }

    public BooleanProperty canAttackHeroProperty() {
        return canAttackHero;
    }
}
