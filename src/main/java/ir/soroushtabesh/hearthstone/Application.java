package ir.soroushtabesh.hearthstone;

import ir.soroushtabesh.hearthstone.cli.CLIManager;
import ir.soroushtabesh.hearthstone.cli.activities.Collections;
import ir.soroushtabesh.hearthstone.controllers.PlayerManager;
import ir.soroushtabesh.hearthstone.models.beans.*;
import ir.soroushtabesh.hearthstone.models.beans.cards.HeroPower;
import ir.soroushtabesh.hearthstone.models.beans.cards.Minion;
import ir.soroushtabesh.hearthstone.models.beans.scripts.Dummy;
import ir.soroushtabesh.hearthstone.util.DBUtil;
import ir.soroushtabesh.hearthstone.util.HashUtil;
import ir.soroushtabesh.hearthstone.util.Logger;

public class Application {
    public static void main(String[] args) {
//        initiate();
//        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);

//        Logger.log("application", "start");
//        CLIManager cliManager = CLIManager.getInstance();
//        cliManager.initializeProcessor();
//        cliManager.startActivity(new StartPage());
//        cliManager.fireUp();
//        Logger.log("application", "shutdown");

        tester();
        /*
        for deploy:
        1- enable banner
        2- disable sql log
        3- write help
        4- erase debug shit
         */
    }

    public static void tester() {
        CLIManager cliManager = CLIManager.getInstance();
        cliManager.initializeProcessor();
        PlayerManager.getInstance().authenticate("akbar", "akbar");
        cliManager.startActivity(new Collections());
        cliManager.fireUp();
    }

    public static void initiate() {
        Logger.log("test", "test", Log.Severity.WARNING);
        Player player = new Player("akbar", HashUtil.hash("akbar"));
        Script script = new Dummy();
        HeroPower heroPower = new HeroPower();
        Hero hero = new Hero();
        Deck deck = new Deck(hero, player);
        Minion minion = new Minion();
        Minion minion1 = new Minion();

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

        DBUtil.pushSingleObject(player);
    }

}
