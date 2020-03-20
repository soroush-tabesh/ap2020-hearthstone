package ir.soroushtabesh.hearthstone.util;

import ir.soroushtabesh.hearthstone.models.beans.*;
import ir.soroushtabesh.hearthstone.models.beans.cards.Minion;
import ir.soroushtabesh.hearthstone.models.beans.scripts.Dummy;
import ir.soroushtabesh.hearthstone.models.beans.scripts.HeroPower;
import org.hibernate.Session;

import java.io.File;

public class Seeding {
    public static void initiate1() {
        Logger.log("test", "test", Log.Severity.WARNING);
        try (Session session = DBUtil.openSession()) {
            Player player = new Player("akbar", HashUtil.hash("akbar"));
            Script script = new Dummy();
            HeroPower heroPower = new HeroPower();
            Hero hero = new Hero();
            Deck deck = new Deck(hero, player);
            Minion minion = new Minion();
            Minion minion1 = new Minion();

            DBUtil.pushSingleObject(player, session);
            DBUtil.pushSingleObject(script, session);
            DBUtil.pushSingleObject(heroPower, session);
            DBUtil.pushSingleObject(hero, session);
            DBUtil.pushSingleObject(deck, session);
            DBUtil.pushSingleObject(minion, session);
            DBUtil.pushSingleObject(minion1, session);

            player.addDeck(deck);
            player.addOpenHero(hero);
            player.addOwnedCard(minion);
            player.addOwnedCard(minion1);

            hero.addSpecialCard(minion);
            hero.setHeroClass(Hero.HeroClass.DRUID);
            hero.setHeroPower(heroPower);
            hero.setHp(40);
            hero.setName("Akbar");
            hero.setSpecialPower(script);

            deck.addCard(minion);
            deck.addCard(minion1);

            minion.setCard_name("Ghozmit");
            minion.setAttackPower(2);
            minion.setHp(5);
            minion.setMinionClass(Minion.MinionClass.BEAST);
            minion.setDescription("Khafan");
            minion.setHeroClass(hero.getHeroClass());
            minion.setMana(3);
            minion.setPrice(12);
            minion.setRarity(Card.Rarity.RARE);
            minion.setScript(script);

            minion1.setCard_name("Peshgel");
            minion1.setAttackPower(3);
            minion1.setHp(1);
            minion1.setMinionClass(Minion.MinionClass.BEAST);
            minion1.setDescription("Oskol");
            minion1.setHeroClass(hero.getHeroClass());
            minion1.setMana(1);
            minion1.setPrice(3);
            minion1.setRarity(Card.Rarity.EPIC);
            minion1.setScript(script);

            DBUtil.pushSingleObject(player, session);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void seed() {

    }

    public static void initiate() {
        File file = new File("./gamedata.mv.db");
        if (!file.exists())
            seed();
    }
}
