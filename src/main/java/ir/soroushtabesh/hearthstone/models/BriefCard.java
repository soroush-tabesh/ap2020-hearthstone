package ir.soroushtabesh.hearthstone.models;

import ir.soroushtabesh.hearthstone.controllers.PlayerManager;
import ir.soroushtabesh.hearthstone.models.cards.Minion;
import ir.soroushtabesh.hearthstone.models.cards.Spell;
import ir.soroushtabesh.hearthstone.models.cards.Weapon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Collection;

public class BriefCard {

    private final Card card;
    private int price;
    private int mana;
    private int hp;
    private int attack;
    private String name;

    private BriefCard(Card card) {
        this.card = card;
    }

    public static BriefCard build(Card card) {
        BriefCard briefCard = new BriefCard(card);
        briefCard.refresh();
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

    public int getAmount() {
        return PlayerManager.getInstance().getPlayer().getOwnedAmount(card);
    }

    public void refresh() {
        price = card.getPrice();
        mana = card.getMana();
        name = card.getName();
        if (card instanceof Minion) {
            hp = ((Minion) card).getHp();
            attack = ((Minion) card).getAttackPower();
        } else if (card instanceof Weapon) {
            hp = ((Weapon) card).getDurability();
            attack = ((Weapon) card).getAttackPower();
        } else if (card instanceof Spell) {

        }
    }

}
