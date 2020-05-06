package ir.soroushtabesh.hearthstone.models;

import ir.soroushtabesh.hearthstone.models.cards.Minion;
import ir.soroushtabesh.hearthstone.models.cards.Spell;
import ir.soroushtabesh.hearthstone.models.cards.Weapon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Collection;

public class BriefCard {

    private Card card;
    private int price;
    private int mana;
    private int hp;
    private int attack;
    private String name;

    private BriefCard() {
    }

    public static BriefCard build(Card card) {
        BriefCard briefCard = new BriefCard();
        briefCard.card = card;
        briefCard.price = card.getPrice();
        briefCard.mana = card.getMana();
        briefCard.name = card.getCard_name();
        if (card instanceof Minion) {
            briefCard.hp = ((Minion) card).getHp();
            briefCard.attack = ((Minion) card).getAttackPower();
        } else if (card instanceof Weapon) {
            briefCard.hp = ((Weapon) card).getDurability();
            briefCard.attack = ((Weapon) card).getAttackPower();
        } else if (card instanceof Spell) {

        }
        return briefCard;
    }

    public static ObservableList<BriefCard> buildAll(Collection<Card> cards) {
        ObservableList<BriefCard> result = FXCollections.observableArrayList();
        cards.forEach(card -> result.add(build(card)));
        return result;
    }

    public String getName() {
        return name;
    }

    public Card getCard() {
        return card;
    }

    public int getPrice() {
        return price;
    }

    public int getMana() {
        return mana;
    }

    public int getHP() {
        return hp;
    }

    public int getAttack() {
        return attack;
    }
}
